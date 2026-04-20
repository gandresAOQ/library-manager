package com.library.infrastructure.controllers;

import com.library.application.ports.IOrchestrator;
import com.library.infrastructure.contracts.ILibraryController;
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

import static com.library.model.Constants.UUID_HEADER;

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
        return orchestrator.execute(uuid, null, path, "GET")
                .onItem().transform(jsonObject -> Response.ok(jsonObject).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build());
    }

    @Override
    @POST
    @Path("{path: .+}")
    public Uni<Response> post(HttpHeaders httpHeaders, JsonObject requestBody, @PathParam("path") String path) {
        String uuid = httpHeaders.getHeaderString(UUID_HEADER);
        return orchestrator.execute(uuid, requestBody, path, "POST")
                .onItem().transform(jsonObject -> Response.ok(jsonObject).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build());
    }

    @Override
    @PUT
    @Path("{path: .+}")
    public Uni<Response> put(HttpHeaders httpHeaders, JsonObject requestBody, @PathParam("path") String path) {
        String uuid = httpHeaders.getHeaderString(UUID_HEADER);
        return orchestrator.execute(uuid, requestBody, path, "PUT")
                .onItem().transform(jsonObject -> Response.ok(jsonObject).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build());
    }

    @Override
    @DELETE
    @Path("{path: .+}")
    public Uni<Response> delete(HttpHeaders httpHeaders, @PathParam("path") String path) {
        String uuid = httpHeaders.getHeaderString(UUID_HEADER);
        return orchestrator.execute(uuid, null, path, "DELETE")
                .onItem().transform(jsonObject -> Response.ok(jsonObject).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build());
    }
}
