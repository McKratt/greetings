package net.bakaar.greetings.stat.rest.glue;


import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebClient
public class StatRestSpringCucumberContextConfiguration {
    @MockBean
    private GreetingsRepository repository;
    @MockBean
    private StatRepository statRepository;
}
