package net.bakaar.greetings.stat.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.stat.application.StatApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(path = "/rest/api/v1/stats")
@RequiredArgsConstructor
public class StatRestController {

    private final StatApplicationService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<GreetingsStatsJson>> getAllStats() {
        return service.retrieveGreetingsStats()
                .log(log.getName())
                .map(stats -> {
                    var json = new GreetingsStatsJson(stats.getCounters());
                    if (stats.isEmpty()) {
                        return ResponseEntity.noContent().build();
                    } else {
                        return ResponseEntity.ok(json);
                    }
                });
    }
}
