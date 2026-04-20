package com.library.application.services;

import com.library.application.ports.IService;
import com.library.infrastructure.contracts.IBookingRepository;
import com.library.model.booking.BookModel;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
@Named("BOOKINGS")
public class BookingService implements IService {

    @Inject
    IBookingRepository bookingRepository;

    @Override
    public Uni<JsonObject> get(String uuid) {
        return bookingRepository.get(uuid)
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }

    @Override
    public Uni<JsonObject> get(String uuid, String id) {
        return bookingRepository.get(uuid, id)
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }

    @Override
    public Uni<JsonObject> post(String uuid, JsonObject body) {
        return bookingRepository.post(uuid, BookModel.toBookModel(body))
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }

    @Override
    public Uni<JsonObject> put(String uuid, String id, JsonObject body) {
        return bookingRepository.put(uuid, id, BookModel.toBookModel(body))
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }

    @Override
    public Uni<JsonObject> delete(String uuid, String id) {
        return bookingRepository.delete(uuid, id)
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }
}
