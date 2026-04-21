package com.library.application.services;

import com.library.application.ports.IService;
import com.library.infrastructure.contracts.IBookingRepository;
import com.library.model.booking.BookModel;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Named("BOOKINGS")
public class BookingService implements IService {

    @Inject
    IBookingRepository bookingRepository;

    @Override
    public Uni<JsonObject> get(String uuid) {
        log.debug("[{}] get all bookings", uuid);
        return bookingRepository.get(uuid)
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }

    @Override
    public Uni<JsonObject> get(String uuid, String id) {
        log.debug("[{}] get booking id={}", uuid, id);
        return bookingRepository.get(uuid, id)
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }

    @Override
    public Uni<JsonObject> post(String uuid, JsonObject body) {
        log.debug("[{}] post booking", uuid);
        return bookingRepository.post(uuid, BookModel.toBookModel(body))
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }

    @Override
    public Uni<JsonObject> put(String uuid, String id, JsonObject body) {
        log.debug("[{}] put booking id={}", uuid, id);
        return bookingRepository.put(uuid, id, BookModel.toBookModel(body))
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }

    @Override
    public Uni<JsonObject> delete(String uuid, String id) {
        log.debug("[{}] delete booking id={}", uuid, id);
        return bookingRepository.delete(uuid, id)
                .onItem().transform(res -> new JsonObject()
                        .put("status", res.getStatus())
                        .put("payload", res.getPayload()));
    }
}
