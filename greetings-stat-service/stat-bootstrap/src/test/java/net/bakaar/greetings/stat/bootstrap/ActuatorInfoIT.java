package net.bakaar.greetings.stat.bootstrap;

import net.bakaar.greetings.stat.persistence.CounterRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT,
        properties = {"greetings.message.topic=''"})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {R2dbcAutoConfiguration.class, DataSourceAutoConfiguration.class})
class ActuatorInfoIT {
    @MockBean
    private CounterRepository repository;
    @MockBean
    private Flyway flyway;
    @Autowired
    private TestRestTemplate template;

    @Test
    void should_return_version_number() {
        // Arrange
        // Act
        var response = template.getForEntity("/actuator/info", String.class);
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"version\":\"2.0.0\"");
    }
}
