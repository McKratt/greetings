package net.bakaar.greetings.rest.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.net.URL;

/**
 * Inspired by RFC 7807 (https://tools.ietf.org/html/rfc7807).
 */
@Getter
@Setter
public class JsonProblemDetail {

    @NonNull
    private URL type;
    @NonNull
    private String title;
    @NonNull
    private Integer statusCode;
}
