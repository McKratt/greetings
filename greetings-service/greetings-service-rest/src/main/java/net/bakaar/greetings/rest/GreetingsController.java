package net.bakaar.greetings.rest;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.application.GreetingApplicationService;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.rest.model.GreetingMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/rest/api/v1/greetings")
@RequiredArgsConstructor
public class GreetingsController {

    private final GreetingApplicationService applicationService;

    private final GreetingToMessageMapper mapper;

    @PostMapping(produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    GreetingMessage createGreeting(@RequestBody CreateGreetingCommand command) {
        return mapper.mapToMessage(applicationService.createGreeting(command));
    }
}
