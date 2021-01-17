package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static net.bakaar.greetings.domain.GreetingType.ANNIVERSARY;
import static org.assertj.core.api.Assertions.assertThat;

class DomainToEntityMapperTest {

    private final DomainToEntityMapper mapper = new DomainToEntityMapper();

    @Test
    void mapToEntity_should_fill_all_fields() {
        // Given
        var type = "christmas";
        var name = "Toto";
        var greeting = Greeting.of(type).to(name).build();
        // When
        var entity = mapper.mapToEntity(greeting);
        // Then
        assertThat(entity.getId()).isNull();
        assertThat(entity.getIdentifier()).isEqualTo(greeting.getIdentifier().toString());
        assertThat(entity.getName()).isEqualTo(greeting.getName());
        assertThat(entity.getType()).isEqualTo(greeting.getType().name());
        assertThat(entity.getCreatedAt()).isNull();
    }

    @Test
    void mapToEntity_should_use_existing_entity() {
        // Given
        var type = "christmas";
        var name = "Toto";
        var greeting = Greeting.of(type).to(name).build();
        var previousEntity = new GreetingJpaEntity();
        var id = 123L;
        previousEntity.setId(id);
        previousEntity.setIdentifier(greeting.getIdentifier().toString());
        previousEntity.setName(name);
        LocalDateTime creationTime = LocalDateTime.now();
        previousEntity.setCreatedAt(creationTime);
        // When
        var entity = mapper.mapToEntity(greeting, previousEntity);
        // Then
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getIdentifier()).isEqualTo(greeting.getIdentifier().toString());
        assertThat(entity.getName()).isEqualTo(greeting.getName());
        assertThat(entity.getType()).isEqualTo(greeting.getType().name());
        assertThat(entity.getCreatedAt()).isEqualTo(creationTime);
    }

    @Test
    void mapToDomain_should_create_domain_object() {
        // Given
        var identifier = UUID.randomUUID().toString();
        var name = "Charline";
        var type = "ANNIVERSARY";
        var id = 123L;
        var entity = new GreetingJpaEntity();
        entity.setType(type);
        entity.setIdentifier(identifier);
        entity.setName(name);
        entity.setId(id);
        // When
        var domain = mapper.mapToDomain(entity);
        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getIdentifier()).isEqualTo(UUID.fromString(identifier));
        assertThat(domain.getType()).isSameAs(ANNIVERSARY);
        assertThat(domain.getName()).isEqualTo(name);
    }
}