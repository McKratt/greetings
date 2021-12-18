package net.bakaar.greetings.stat.message;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bakaar.greetings.message.GreetingsMessage;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.message.handler.GreetingMessagePayloadHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.support.Acknowledgment;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(classes = StatMessageConfiguration.class,
        properties = {
                "greetings.message.topic=test"
        })
@EnableAutoConfiguration
@PactTestFor(providerName = "greetings-message-service", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V3)
class StatConsumerMessagePactIT {

    private final UUID identifier = UUID.randomUUID();
    private final ObjectMapper jsonMapper = new ObjectMapper();
    @Autowired
    GreetingsMessageProcessor processor;
    @Autowired
    @Qualifier("greetingCreatedPayloadHandler")
    GreetingMessagePayloadHandler handler;
    @MockBean
    StatApplicationService service;

    @Pact(consumer = "stat-service")
    MessagePact createPact(MessagePactBuilder builder) {
        PactDslJsonBody payload = new PactDslJsonBody()
                .stringMatcher("type", "^https://bakaar.net/greetings/events/.*", "https://bakaar.net/greetings/events/greeting-created")
                .stringType("payload", "{\"identifier\":\"" + identifier + "\"}")
                .asBody();
        return builder
                .expectsToReceive("A greetings created message")
                .withContent(payload)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void message_should_be_sent(List<Message> messages) throws IOException {
        // Given
        assertThat(processor).as("Processor should not be null").isNotNull();
        assertThat(messages).hasSize(1);
        given(service.handle(any())).willReturn(Mono.empty());
        var message = jsonMapper.readValue(messages.get(0).getContents().valueAsString(), GreetingsMessage.class);
        // When
        processor.processMessage(message, mock(Acknowledgment.class));
        // Then
        var captor = ArgumentCaptor.forClass(GreetingCreated.class);
        verify(service).handle(captor.capture());
        var event = captor.getValue();
        assertThat(event).isNotNull().extracting(GreetingCreated::identifier).isEqualTo(identifier);
    }
}
