package net.bakaar.greetings.stat.rest.glue;


import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebClient
public class StatRestSpringCucumberContextConfiguration {
    @MockitoBean
    private GreetingsRepository repository;
    @MockitoBean
    private StatRepository statRepository;
}
