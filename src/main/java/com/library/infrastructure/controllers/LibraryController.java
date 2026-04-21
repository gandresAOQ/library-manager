package com.library.infrastructure.controllers;

import com.library.application.ports.IOrchestrator;
import com.library.infrastructure.contracts.ILibraryController;
import com.library.utils.BodyValidator;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import static com.library.model.Constants.UUID_HEADER;

@Slf4j
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/library")
public class LibraryController implements ILibraryController {

    @Inject
    IOrchestrator orchestrator;

    @Override
    @GET
    @Path("{path: .+}")
    public Uni<Response> get(HttpHeaders httpHeaders, @PathParam("path") String path) {
        String uuid = httpHeaders.getHeaderString(UUID_HEADER);
        log.info("[{}] GET {}", uuid, path);
        return orchestrator.execute(uuid, null, path, "GET")
                .onItem().transform(jsonObject -> Response.ok(jsonObject).build())
                .onFailure().recoverWithItem(throwable -> {
                    log.error("[{}] GET {} failed: {}", uuid, path, throwable.getMessage());
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build();
                });
    }

    @Override
    @POST
    @Path("{path: .+}")
    public Uni<Response> post(HttpHeaders httpHeaders, JsonObject requestBody, @PathParam("path") String path) {
        BodyValidator.validateRequestBody("POST", requestBody);
        String uuid = httpHeaders.getHeaderString(UUID_HEADER);
        log.info("[{}] POST {}", uuid, path);
        return orchestrator.execute(uuid, requestBody, path, "POST")
                .onItem().transform(jsonObject -> Response.ok(jsonObject).build())
                .onFailure().recoverWithItem(throwable -> {
                    log.error("[{}] POST {} failed: {}", uuid, path, throwable.getMessage());
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build();
                });
    }

    @Override
    @PUT
    @Path("{path: .+}")
    public Uni<Response> put(HttpHeaders httpHeaders, JsonObject requestBody, @PathParam("path") String path) {
        BodyValidator.validateRequestBody("PUT", requestBody);
        String uuid = httpHeaders.getHeaderString(UUID_HEADER);
        log.info("[{}] PUT {}", uuid, path);
        return orchestrator.execute(uuid, requestBody, path, "PUT")
                .onItem().transform(jsonObject -> Response.ok(jsonObject).build())
                .onFailure().recoverWithItem(throwable -> {
                    log.error("[{}] PUT {} failed: {}", uuid, path, throwable.getMessage());
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build();
                });
    }

    @Override
    @DELETE
    @Path("{path: .+}")
    public Uni<Response> delete(HttpHeaders httpHeaders, @PathParam("path") String path) {
        String uuid = httpHeaders.getHeaderString(UUID_HEADER);
        log.info("[{}] DELETE {}", uuid, path);
        return orchestrator.execute(uuid, null, path, "DELETE")
                .onItem().transform(jsonObject -> Response.ok(jsonObject).build())
                .onFailure().recoverWithItem(throwable -> {
                    log.error("[{}] DELETE {} failed: {}", uuid, path, throwable.getMessage());
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build();
                });
    }
}
