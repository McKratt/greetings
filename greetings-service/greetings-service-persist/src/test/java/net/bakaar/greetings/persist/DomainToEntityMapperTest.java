package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.Test;

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
        GreetingJpaEntity entity = mapper.mapToEntity(greeting);
        // Then
        assertThat(entity.getId()).isNull();
        assertThat(entity.getIdentifier()).isEqualTo(greeting.getIdentifier().toString());
        assertThat(entity.getName()).isEqualTo(greeting.getName());
        assertThat(entity.getType()).isEqualTo(greeting.getType().name());
        assertThat(entity.getCreatedAt()).isNull();
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
        Greeting domain = mapper.mapToDomain(entity);
        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getIdentifier()).isEqualTo(UUID.fromString(identifier));
        assertThat(domain.getType()).isSameAs(ANNIVERSARY);
        assertThat(domain.getName()).isEqualTo(name);
    }
}