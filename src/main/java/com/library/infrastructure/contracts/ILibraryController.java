package com.library.infrastructure.contracts;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

public interface ILibraryController {

    Uni<Response> get(HttpHeaders httpHeaders, String path);
    Uni<Response> post(HttpHeaders httpHeaders, JsonObject requestBody, String path);
    Uni<Response> put(HttpHeaders httpHeaders, JsonObject requestBody, String path);
    Uni<Response> delete(HttpHeaders httpHeaders, String path);

}
