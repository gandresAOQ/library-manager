package com.library.interceptors;

import com.library.model.errors.LibraryException;
import com.library.model.errors.LibraryExceptionResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LibraryExceptionMapper implements ExceptionMapper<LibraryException> {
    @Override
    public Response toResponse(LibraryException e) {
        return LibraryExceptionResponse.builder()
                .message(e.getMessage())
                .statusCode(e.getStatusCode())
                .httpStatus(e.getHttpStatus())
                .httpStatusMessage(e.getHttpStatusMessage())
                .build()
                .buildResponse();
    }
}
