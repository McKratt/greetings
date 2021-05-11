package net.bakaar.greetings.stat.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.net.URI;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record GreetingMessage(URI type, String payload) {
}
