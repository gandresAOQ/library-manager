package com.library.model.errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class LibraryException extends RuntimeException {

    private String message;
    private String statusCode;
    private Integer httpStatus;
    private String httpStatusMessage;

}
