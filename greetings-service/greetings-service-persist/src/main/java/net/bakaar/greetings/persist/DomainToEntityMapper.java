package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import org.springframework.stereotype.Component;

@Component
public class DomainToEntityMapper {
    public GreetingJpaEntity mapToEntity(Greeting greeting) {
        var entity = new GreetingJpaEntity();
        entity.setIdentifier(greeting.getIdentifier().toString());
        entity.setName(greeting.getName());
        entity.setType(greeting.getType().name());
        return entity;
    }

    public Greeting mapToDomain(GreetingJpaEntity jpaEntity) {
        return Greeting.of(jpaEntity.getType()).to(jpaEntity.getName()).withIdentifier(jpaEntity.getIdentifier()).build();
    }
}
