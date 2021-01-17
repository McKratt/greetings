package net.bakaar.greetings.rest;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateGreetingCommandDTO {
    private String newType;
}