package net.bakaar.greetings.stat.rest;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.stat.application.StatApplicationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/rest/api/v1/stats")
@RequiredArgsConstructor
public class StatRestController {

    private final StatApplicationService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GreetingsStatsJson> getAllStats() {
        return service.retrieveGreetingsStats().
                map(stats -> new GreetingsStatsJson(stats.getCounters()));
    }
}
