package net.bakaar.greetings.message;

import java.net.URI;

public record GreetingsMessage(URI type, String payload) {

}
