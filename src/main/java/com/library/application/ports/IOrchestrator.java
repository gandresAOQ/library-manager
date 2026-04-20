package com.library.application.ports;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;

public interface IOrchestrator {

    Uni<JsonObject> execute(String uuid, JsonObject requestBody, String path, String method);

}
