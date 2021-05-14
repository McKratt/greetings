package net.bakaar.greetings.message.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bakaar.greetings.domain.event.GreetingsEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectEventEmitterAdapterTest {

    @Mock
    private ObjectMapper jsonMapper;
    @Mock
    private KafkaTemplate<String, GreetingsMessage> template;
    @Mock
    private GreetingsProducerProperties properties;
    @InjectMocks
    private DirectEventEmitterAdapter adapter;
    @Mock
    private GreetingsEvent event;

    @Test
    void should_transform_payload_to_json_and_call_kafka() throws JsonProcessingException {
        // Given
        var type = "https://bakaar.net/greetings/events/greeting-created";
        var payload = "I'm a payload";
        given(jsonMapper.writeValueAsString(event)).willReturn(payload);
        var topic = "MyTopic";
        given(properties.getTopicName()).willReturn(topic);
        // When
        adapter.emit(event);
        // Then
        var captor = ArgumentCaptor.forClass(GreetingsMessage.class);
        verify(template).send(eq(topic), captor.capture());
        var message = captor.getValue();
        assertThat(message).isNotNull();
        assertThat(message.type().toString()).isEqualTo(type);
        assertThat(message.payload()).isSameAs(payload);
    }

    @Test
    void should_throw_a_runtimeException() throws JsonProcessingException {
        // Given
        var e = mock(JsonProcessingException.class);
        given(jsonMapper.writeValueAsString(any())).willThrow(e);
        // When
        var thrown = catchThrowable(() -> adapter.emit(event));
        // Then
        verify(template, never()).send(any(), any());
        assertThat(thrown).isInstanceOf(ProducerException.class);
        assertThat(thrown.getCause()).isSameAs(e);
    }
}
