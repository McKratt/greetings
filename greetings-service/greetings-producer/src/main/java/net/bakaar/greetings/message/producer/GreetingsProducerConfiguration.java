package net.bakaar.greetings.message.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.bakaar.greetings.domain.event.EventEmitter;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@EnableKafka
@Configuration
@EnableConfigurationProperties(GreetingsProducerProperties.class)
public class GreetingsProducerConfiguration {

    //TODO put that in bootstrap, this is not the producer that know the actual configuration of Kafka
//    @Bean
//    NewTopic greetingTopic(GreetingsProducerProperties properties) {
//        return new NewTopic(properties.getTopicName(), 1, (short) 1);
//    }

    @Bean
    EventEmitter eventEmitter(GreetingsProducerProperties properties, KafkaTemplate<String, GreetingsMessage> template) {
        return new DirectEventEmitterAdapter(properties, createMapper(), template);
    }

    @Bean
    DefaultKafkaProducerFactoryCustomizer producerFactoryCustomizer() {
        return producerFactory -> {
            Map<String, Object> properties = Map.of(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                    KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            producerFactory.updateConfigs(properties);
        };
    }

    private ObjectMapper createMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        return mapper;
    }
}
