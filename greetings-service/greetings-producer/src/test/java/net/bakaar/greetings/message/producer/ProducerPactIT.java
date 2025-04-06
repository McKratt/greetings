package net.bakaar.greetings.message.producer;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.event.EventEmitter;
import net.bakaar.greetings.domain.event.GreetingCreated;
import net.bakaar.greetings.message.GreetingsMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(classes = GreetingsProducerConfiguration.class)
@EnableAutoConfiguration
@Provider("greetings-message-service")
@PactFolder("pacts")
class ProducerPactIT {

    @Autowired
    EventEmitter emitter;

    @Autowired
    ObjectMapper jsonMapper; // This ObjectMapper is Used by the Kafka.JsonSerializer

    @MockitoBean
    KafkaTemplate<String, GreetingsMessage> template;

    @BeforeEach
    void before(PactVerificationContext context) {
        System.setProperty("pact.verifier.publishResults", "true");
        context.setTarget(new MessageTestTarget());
        assertThat(emitter).isNotNull();
    }

    @PactVerifyProvider("A greetings created message")
    String send_greeting_created_message() throws JsonProcessingException {
        var event = GreetingCreated.of(Greeting.of("birthday").to("toto").build());
        emitter.emit(event);
        var captor = ArgumentCaptor.forClass(GreetingsMessage.class);
        verify(template).send(any(), captor.capture());
        var message = captor.getValue();
        assertThat(message).isNotNull();
        return jsonMapper.writeValueAsString(message);
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
