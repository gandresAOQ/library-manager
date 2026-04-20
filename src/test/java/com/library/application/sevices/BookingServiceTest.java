package com.library.application.sevices;

import com.booking.grpc.Booking;
import com.library.application.services.BookingService;
import com.library.infrastructure.contracts.IBookingRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BookingServiceTest {

    @Inject
    BookingService service;

    @InjectMock
    IBookingRepository bookingRepository;

    private static final String UUID = "test-uuid";
    private static final String ID = "book-1";

    private Booking.BookingResponse grpcResponse;

    @BeforeEach
    void setUp() {
        grpcResponse = Booking.BookingResponse.newBuilder().setStatus(200).setPayload("ok").build();
        Mockito.reset(bookingRepository);
    }

    @Test
    void get_shouldReturnMappedJsonObject() {
        Mockito.when(bookingRepository.get(UUID)).thenReturn(Uni.createFrom().item(grpcResponse));

        JsonObject result = service.get(UUID).await().indefinitely();

        assertEquals(200, result.getInteger("status"));
        assertEquals("ok", result.getString("payload"));
    }

    @Test
    void getById_shouldReturnMappedJsonObject() {
        Mockito.when(bookingRepository.get(UUID, ID)).thenReturn(Uni.createFrom().item(grpcResponse));

        JsonObject result = service.get(UUID, ID).await().indefinitely();

        assertEquals(200, result.getInteger("status"));
        assertEquals("ok", result.getString("payload"));
    }

    @Test
    void post_shouldReturnMappedJsonObject() {
        Mockito.when(bookingRepository.post(Mockito.eq(UUID), Mockito.any()))
                .thenReturn(Uni.createFrom().item(grpcResponse));

        JsonObject body = new JsonObject()
                .put("id", "b-1").put("name", "Clean Code").put("author", "Robert Martin")
                .put("price", "25.00").put("language", "English").put("pages", "431").put("format", "Hardcover");

        JsonObject result = service.post(UUID, body).await().indefinitely();

        assertEquals(200, result.getInteger("status"));
        assertEquals("ok", result.getString("payload"));
    }

    @Test
    void put_shouldReturnMappedJsonObject() {
        Mockito.when(bookingRepository.put(Mockito.eq(UUID), Mockito.eq(ID), Mockito.any()))
                .thenReturn(Uni.createFrom().item(grpcResponse));

        JsonObject body = new JsonObject().put("name", "Refactoring").put("author", "Martin Fowler");

        JsonObject result = service.put(UUID, ID, body).await().indefinitely();

        assertEquals(200, result.getInteger("status"));
        assertEquals("ok", result.getString("payload"));
    }

    @Test
    void delete_shouldReturnMappedJsonObject() {
        Mockito.when(bookingRepository.delete(UUID, ID)).thenReturn(Uni.createFrom().item(grpcResponse));

        JsonObject result = service.delete(UUID, ID).await().indefinitely();

        assertEquals(200, result.getInteger("status"));
        assertEquals("ok", result.getString("payload"));
    }

    @Test
    void get_shouldPropagateFailure() {
        Mockito.when(bookingRepository.get(UUID))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("gRPC error")));

        Uni<JsonObject> result = service.get(UUID);

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> result.await().indefinitely());
    }
}