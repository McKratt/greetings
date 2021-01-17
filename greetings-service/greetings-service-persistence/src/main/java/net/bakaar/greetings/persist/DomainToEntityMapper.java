package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import org.springframework.stereotype.Component;

@Component
public class DomainToEntityMapper {
    public GreetingJpaEntity mapToEntity(Greeting greeting) {
        GreetingJpaEntity entity = new GreetingJpaEntity();
        return mapToEntity(greeting, entity);
    }

    public Greeting mapToDomain(GreetingJpaEntity jpaEntity) {
        return Greeting.of(jpaEntity.getType()).to(jpaEntity.getName()).withIdentifier(jpaEntity.getIdentifier()).build();
    }

    public GreetingJpaEntity mapToEntity(Greeting greeting, GreetingJpaEntity entity) {
        entity.setIdentifier(greeting.getIdentifier().toString());
        entity.setName(greeting.getName());
        entity.setType(greeting.getType().name());
        return entity;
    }
}
