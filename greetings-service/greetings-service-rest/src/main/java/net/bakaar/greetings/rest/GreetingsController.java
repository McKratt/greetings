package net.bakaar.greetings.rest;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.application.GreetingApplicationService;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.UpdateGreetingCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(path = "/rest/api/v1/greetings")
@RequiredArgsConstructor
public class GreetingsController {

    private final GreetingApplicationService applicationService;

    private final GreetingToMessageMapper mapper;

    @PostMapping
//            (produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GreetingMessage> createGreeting(@RequestBody CreateGreetingCommand command) {
        Greeting createdGreeting = applicationService.createGreeting(command);
        return ResponseEntity
                .created(fromCurrentRequest().path("/{identifier}").buildAndExpand(createdGreeting.getIdentifier()).toUri())
                .body(mapper.mapToMessage(createdGreeting));
    }

    @PutMapping("/{identifier}")
    public ResponseEntity<GreetingMessage> updateGreeting(@PathVariable UUID identifier,
                                                          @RequestBody UpdateGreetingCommandDTO command) {
        return ResponseEntity.ok(mapper.mapToMessage(
                applicationService.changeType(new UpdateGreetingCommand(identifier, command.getNewType())
                )));
    }
}
