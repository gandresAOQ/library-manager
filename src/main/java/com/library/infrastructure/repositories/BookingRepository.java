package com.library.infrastructure.repositories;

import com.booking.grpc.Booking;
import com.booking.grpc.MutinyBookingServiceGrpc;
import com.library.infrastructure.contracts.IBookingRepository;
import com.library.model.booking.BookModel;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class BookingRepository implements IBookingRepository {

    @GrpcClient("booking")
    MutinyBookingServiceGrpc.MutinyBookingServiceStub client;

    @Override
    public Uni<Booking.BookingResponse> get(String uuid) {
        log.debug("[{}] gRPC getAll page=0", uuid);
        return client.getAll(Booking.BookingRequestPaged.newBuilder()
                .setUuid(uuid)
                .setPage("0")
                .build());
    }

    @Override
    public Uni<Booking.BookingResponse> get(String uuid, String id) {
        log.debug("[{}] gRPC get id={}", uuid, id);
        return client.get(Booking.BookingRequestId.newBuilder()
                .setUuid(uuid)
                .setId(id)
                .build());
    }

    @Override
    public Uni<Booking.BookingResponse> post(String uuid, BookModel body) {
        log.debug("[{}] gRPC post name={}", uuid, body.getName());
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
        log.debug("[{}] gRPC put id={}", uuid, id);
        return client.put(Booking.BookingRequest.newBuilder()
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
        log.debug("[{}] gRPC delete id={}", uuid, id);
        return client.delete(Booking.BookingRequestId.newBuilder()
                .setUuid(uuid)
                .setId(id)
                .build());
    }
}
