package com.library.infrastructure.repositories;

import com.booking.grpc.Booking;
import com.booking.grpc.MutinyBookingServiceGrpc;
import com.library.model.booking.BookModel;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BookingRepositoryTest {

    private static final String SERVER_NAME = "booking-test";

    private Server server;
    private ManagedChannel channel;
    private BookingRepository repository;
    private FakeBookingService fakeService;

    @BeforeEach
    void setUp() throws IOException {
        fakeService = new FakeBookingService();
        server = InProcessServerBuilder.forName(SERVER_NAME)
                .directExecutor()
                .addService(fakeService)
                .build()
                .start();
        channel = InProcessChannelBuilder.forName(SERVER_NAME)
                .directExecutor()
                .build();
        repository = new BookingRepository();
        repository.client = MutinyBookingServiceGrpc.newMutinyStub(channel);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        server.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void get_shouldCallGetAllWithCorrectRequest() {
        fakeService.nextGetAllResponse = Booking.BookingResponse.newBuilder().setStatus(200).setPayload("all").build();

        Booking.BookingResponse result = repository.get("uuid-1").await().indefinitely();

        assertEquals(fakeService.nextGetAllResponse, result);
        assertEquals("uuid-1", fakeService.capturedGetAllRequest.getUuid());
        assertEquals("0", fakeService.capturedGetAllRequest.getPage());
    }

    @Test
    void getById_shouldCallGetWithCorrectRequest() {
        fakeService.nextGetResponse = Booking.BookingResponse.newBuilder().setStatus(200).setPayload("one").build();

        Booking.BookingResponse result = repository.get("uuid-1", "book-1").await().indefinitely();

        assertEquals(fakeService.nextGetResponse, result);
        assertEquals("uuid-1", fakeService.capturedGetRequest.getUuid());
        assertEquals("book-1", fakeService.capturedGetRequest.getId());
    }

    @Test
    void post_shouldCallPostWithAllFieldsMapped() {
        fakeService.nextPostResponse = Booking.BookingResponse.newBuilder().setStatus(201).build();
        BookModel body = BookModel.builder()
                .id("b-1").name("Clean Code").author("Robert Martin")
                .price("25.00").language("English").pages("431").format("Hardcover")
                .build();

        Booking.BookingResponse result = repository.post("uuid-1", body).await().indefinitely();

        assertEquals(fakeService.nextPostResponse, result);
        Booking.BookingRequest req = fakeService.capturedPostRequest;
        assertEquals("uuid-1", req.getUuid());
        assertEquals("b-1", req.getId());
        assertEquals("Clean Code", req.getName());
        assertEquals("Robert Martin", req.getAuthor());
        assertEquals("25.00", req.getPrice());
        assertEquals("English", req.getLanguage());
        assertEquals("431", req.getPages());
        assertEquals("Hardcover", req.getFormat());
    }

    @Test
    void put_shouldUsePathIdOverridingBodyId() {
        fakeService.nextPostResponse = Booking.BookingResponse.newBuilder().setStatus(200).build();
        BookModel body = BookModel.builder()
                .id("ignored").name("Refactoring").author("Martin Fowler")
                .price("30.00").language("English").pages("448").format("Paperback")
                .build();

        Booking.BookingResponse result = repository.put("uuid-1", "path-id", body).await().indefinitely();

        assertEquals(fakeService.nextPostResponse, result);
        Booking.BookingRequest req = fakeService.capturedPostRequest;
        assertEquals("uuid-1", req.getUuid());
        assertEquals("path-id", req.getId());
        assertEquals("Refactoring", req.getName());
        assertEquals("Martin Fowler", req.getAuthor());
    }

    @Test
    void delete_shouldCallDeleteWithCorrectRequest() {
        fakeService.nextDeleteResponse = Booking.BookingResponse.newBuilder().setStatus(204).build();

        Booking.BookingResponse result = repository.delete("uuid-1", "book-1").await().indefinitely();

        assertEquals(fakeService.nextDeleteResponse, result);
        assertEquals("uuid-1", fakeService.capturedDeleteRequest.getUuid());
        assertEquals("book-1", fakeService.capturedDeleteRequest.getId());
    }

    static class FakeBookingService extends MutinyBookingServiceGrpc.BookingServiceImplBase {

        Booking.BookingRequestPaged capturedGetAllRequest;
        Booking.BookingRequestId capturedGetRequest;
        Booking.BookingRequest capturedPostRequest;
        Booking.BookingRequestId capturedDeleteRequest;

        Booking.BookingResponse nextGetAllResponse;
        Booking.BookingResponse nextGetResponse;
        Booking.BookingResponse nextPostResponse;
        Booking.BookingResponse nextDeleteResponse;

        @Override
        public Uni<Booking.BookingResponse> getAll(Booking.BookingRequestPaged request) {
            capturedGetAllRequest = request;
            return Uni.createFrom().item(nextGetAllResponse);
        }

        @Override
        public Uni<Booking.BookingResponse> get(Booking.BookingRequestId request) {
            capturedGetRequest = request;
            return Uni.createFrom().item(nextGetResponse);
        }

        @Override
        public Uni<Booking.BookingResponse> post(Booking.BookingRequest request) {
            capturedPostRequest = request;
            return Uni.createFrom().item(nextPostResponse);
        }

        @Override
        public Uni<Booking.BookingResponse> delete(Booking.BookingRequestId request) {
            capturedDeleteRequest = request;
            return Uni.createFrom().item(nextDeleteResponse);
        }
    }
}