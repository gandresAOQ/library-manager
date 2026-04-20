package com.library.model.errors;

import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LibraryExceptionResponse {

    private String message;
    private String statusCode;
    private Integer httpStatus;
    private String httpStatusMessage;


    public Response buildResponse() {
        return Response.status(this.httpStatus)
                .entity(this)
                .build();
    }

}
