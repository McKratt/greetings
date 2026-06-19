package net.bakaar.greetings.stat.bootstrap;

import net.bakaar.greetings.stat.domain.StatRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.r2dbc.autoconfigure.R2dbcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.assertj.WebTestClientResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT,
        properties = {"greetings.message.topic=''"})
@AutoConfigureWebTestClient
@EnableAutoConfiguration(exclude = {R2dbcAutoConfiguration.class, DataSourceAutoConfiguration.class})
class ActuatorInfoIT {
    @MockitoBean
    private StatRepository repository;
    @MockitoBean
    private Flyway flyway;
    @Autowired
    private WebTestClient template;

    @Test
    void should_return_version_number() {
        // Arrange
        // Act
        var response = WebTestClientResponse.from(template.get().uri("/actuator/info")
                .exchange().returnResult());
        // Assert
        assertThat(response).hasStatus(HttpStatus.OK).bodyText().contains("\"version\":\"2.1.0\"");
    }
}
