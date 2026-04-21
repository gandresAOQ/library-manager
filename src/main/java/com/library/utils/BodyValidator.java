package com.library.utils;

import com.library.model.errors.LibraryException;
import io.vertx.core.json.JsonObject;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Arrays;

public class BodyValidator {

    private static final String REQUIRED_PROPERTIES = "id:String,name:String,author:String,price:Double,language:String,pages:Integer,format:String";

    public static void validateRequestBody(String method, JsonObject requestBody) {

        boolean isRequestBodyValid = Arrays.stream(REQUIRED_PROPERTIES.split(",")).allMatch(propertyAndValue -> {
            String property = propertyAndValue.split(":")[0];
            String type = propertyAndValue.split(":")[1];
            return requestBody.containsKey(property)
                    && requestBody.getValue(property) != null
                    && requestBody.getValue(property).getClass().getSimpleName().equals(type);
        });


        if (!isRequestBodyValid) {
            throw LibraryException.builder()
                    .message("Body is now allowed")
                    .statusCode(RestResponse.Status.BAD_REQUEST.getReasonPhrase())
                    .httpStatus(400)
                    .httpStatusMessage("Bad Request")
                    .build();
        }

    }

}
