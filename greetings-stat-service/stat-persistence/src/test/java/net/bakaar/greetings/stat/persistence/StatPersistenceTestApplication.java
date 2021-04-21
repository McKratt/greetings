package net.bakaar.greetings.stat.persistence;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

import javax.annotation.PostConstruct;

@SpringBootApplication(proxyBeanMethods = false)
public class StatPersistenceTestApplication {

    @Autowired
    private ConnectionFactory connectionFactory;

    @PostConstruct
    void template() {
        var template = new R2dbcEntityTemplate(connectionFactory);
        template.getDatabaseClient().sql("CREATE TABLE T_COUNTER" +
                "(PK_ID NUMBER PRIMARY KEY," +
                "S_NAME VARCHAR(255)," +
                "L_COUNT NUMBER)")
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
