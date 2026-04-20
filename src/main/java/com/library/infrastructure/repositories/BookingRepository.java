package com.library.infrastructure.repositories;

import com.booking.grpc.Booking;
import com.booking.grpc.MutinyBookingServiceGrpc;
import com.library.infrastructure.contracts.IBookingRepository;
import com.library.model.booking.BookModel;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BookingRepository implements IBookingRepository {

    @GrpcClient("booking")
    MutinyBookingServiceGrpc.MutinyBookingServiceStub client;

    @Override
    public Uni<Booking.BookingResponse> get(String uuid) {
        return client.getAll(Booking.BookingRequestPaged.newBuilder()
                .setUuid(uuid)
                .setPage("0")
                .build());
    }

    @Override
    public Uni<Booking.BookingResponse> get(String uuid, String id) {
        return client.get(Booking.BookingRequestId.newBuilder()
                .setUuid(uuid)
                .setId(id)
                .build());
    }

    @Override
    public Uni<Booking.BookingResponse> post(String uuid, BookModel body) {
        return client.post(Booking.BookingRequest.newBuilder()
                .setUuid(uuid)
                .setName(body.getName())
                .setId(body.getId())
                .setAuthor(body.getAuthor())
                .setPrice(body.getPrice())
                .setLanguage(body.getLanguage())
                .setPages(body.getPages())
                .setFormat(body.getFormat())
                .build());
    }

    @Override
    public Uni<Booking.BookingResponse> put(String uuid, String id, BookModel body) {
        return client.post(Booking.BookingRequest.newBuilder()
                .setUuid(uuid)
                .setName(body.getName())
                .setId(id)
                .setAuthor(body.getAuthor())
                .setPrice(body.getPrice())
                .setLanguage(body.getLanguage())
                .setPages(body.getPages())
                .setFormat(body.getFormat())
                .build());
    }

    @Override
    public Uni<Booking.BookingResponse> delete(String uuid, String id) {
        return client.delete(Booking.BookingRequestId.newBuilder()
                .setUuid(uuid)
                .setId(id)
                .build());
    }
}
