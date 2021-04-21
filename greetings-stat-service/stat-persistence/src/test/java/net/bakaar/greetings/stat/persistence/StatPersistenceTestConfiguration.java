package net.bakaar.greetings.stat.persistence;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

@TestConfiguration
@Import(StatPersistenceConfiguration.class)
public class StatPersistenceTestConfiguration {

    @Bean
    R2dbcEntityTemplate template(ConnectionFactory connectionFactory) {
        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);

        template.getDatabaseClient()
                .sql("CREATE TABLE T_COUNTER" +
                        "(PK_ID NUMBER PRIMARY KEY," +
                        "S_NAME VARCHAR(255)," +
                        "L_COUNT NUMBER)")
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        return template;
    }
}
