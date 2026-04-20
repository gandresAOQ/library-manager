package com.library.application.orchestrator;

import com.library.application.ports.IOrchestrator;
import com.library.application.ports.IService;
import com.library.model.errors.LibraryException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.inject.Inject;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.RestResponse;

@ApplicationScoped
public class Orchestrator implements IOrchestrator {

    @Inject
    @Any
    Instance<IService> services;

    @ConfigProperty(name = "com.library.base.path")
    String basePath;

    @Override
    public Uni<JsonObject> execute(String uuid, JsonObject requestBody, String path, String method) {
        String[] splitPath = path.split("/");
        String domain = splitPath[0].toUpperCase();
        String id = splitPath.length > 1 ? splitPath[1] : null;

        IService service = services.select(NamedLiteral.of(domain)).get();

        if ((method.equals("PUT") || method.equals("DELETE")) && id == null) {
            throwException(method);
        }

        if (method.equals("GET") && id == null) {
            return service.get(uuid);
        }

        return switch (method) {
            case "GET" -> service.get(uuid, id);
            case "POST" -> service.post(uuid, requestBody);
            case "PUT" -> service.put(uuid, id, requestBody);
            case "DELETE" -> service.delete(uuid, id);
            default -> null;
        };

    }

    private void throwException(String method) {
        throw LibraryException
                .builder()
                .message(String.format("%s resource must have ID", method))
                .statusCode(RestResponse.Status.BAD_REQUEST.getReasonPhrase())
                .httpStatus(RestResponse.Status.BAD_REQUEST.getStatusCode())
                .httpStatusMessage("Bad Request")
                .build();
    }
}
