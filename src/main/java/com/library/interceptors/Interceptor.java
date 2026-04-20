package com.library.interceptors;

import com.library.model.errors.LibraryException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.RestResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.library.model.Constants.REQUIRED_HEADERS;

@Provider
public class Interceptor implements ContainerRequestFilter, ContainerResponseFilter {

    @ConfigProperty(name = "com.library.allowed-paths")
    String allowedPaths;

    @ConfigProperty(name = "com.library.base.path")
    String basePath;

    @SneakyThrows
    @Override
    public void filter(ContainerRequestContext containerRequestContext) {

        if (!isPathAllowed(containerRequestContext)) {
            throw LibraryException.builder()
                    .message("Path is now allowed")
                    .statusCode(RestResponse.Status.BAD_REQUEST.getReasonPhrase())
                    .httpStatus(400)
                    .httpStatusMessage("Bad Request")
                    .build();
        }

        if (!areOkHeaders(containerRequestContext)) {
            throw LibraryException.builder()
                    .message("Missing required headers")
                    .statusCode("MISSING_HEADERS")
                    .httpStatus(400)
                    .httpStatusMessage("Bad Request")
                    .build();
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {

    }

    private boolean areOkHeaders(ContainerRequestContext containerRequestContext) {
        MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
        return Arrays.stream(REQUIRED_HEADERS.split(",")).allMatch(headers::containsKey);
    }

    private boolean isPathAllowed(ContainerRequestContext containerRequestContext) {
        String path = containerRequestContext.getUriInfo().getPath().split(basePath)[1];
        System.out.println("allowedPaths");
        System.out.println("allowedPaths: " + allowedPaths);
        System.out.println("path: " + path);
        return Arrays.stream(allowedPaths.split(","))
                .anyMatch(regexPath -> Pattern.compile(regexPath, Pattern.CASE_INSENSITIVE).matcher(path).matches());
    }
}
