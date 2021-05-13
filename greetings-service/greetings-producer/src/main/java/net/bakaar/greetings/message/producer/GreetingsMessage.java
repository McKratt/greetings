package net.bakaar.greetings.message.producer;

import java.net.URI;

public record GreetingsMessage(URI type, String payload) {

}
