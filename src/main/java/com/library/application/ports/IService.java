package com.library.application.ports;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;

public interface IService {

    Uni<JsonObject> get(String uuid);
    Uni<JsonObject> get(String uuid, String id);
    Uni<JsonObject> post(String uuid, JsonObject body);
    Uni<JsonObject> put(String uuid, String id, JsonObject body);
    Uni<JsonObject> delete(String uuid, String id);

}
