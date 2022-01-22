package net.bakaar.greetings.stat.persistence;

import io.r2dbc.spi.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.test.StepVerifier;


@SpringBootApplication(proxyBeanMethods = false)
@EnableTransactionManagement
public class StatPersistenceTestApplication {

    @Autowired
    private ConnectionFactory connectionFactory;

    @PostConstruct
    void template() {
        var template = new R2dbcEntityTemplate(connectionFactory);
        template.getDatabaseClient().sql("CREATE TABLE T_COUNTER" +
                "(PK_T_COUNTER SERIAL PRIMARY KEY," +
                "S_NAME VARCHAR(255)," +
                "L_COUNT NUMBER)")
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
