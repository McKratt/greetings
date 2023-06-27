package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GreetingRepositoryJPAAdapterTest {

    @Mock
    private GreetingJpaRepository jpaRepository;
    @Mock
    private GreetingTypeJpaRepository typeJpaRepository;
    @InjectMocks
    private GreetingRepositoryJPAAdapter adapter;

    @Test
    void find_should_call_repository() {
        // Arrange
        var jpaEntity = new GreetingJpaEntity();
        var identifier = UUID.randomUUID();
        jpaEntity.setIdentifier(identifier.toString());
        var name = "Alice";
        jpaEntity.setName(name);
        var type = GreetingType.CHRISTMAS;
        var typeJpaEntity = new GreetingTypeJpaEntity();
        typeJpaEntity.setName(type.name());
        jpaEntity.setType(typeJpaEntity);
        given(jpaRepository.findByIdentifier(any())).willReturn(Optional.of(jpaEntity));

        // Act
        var foundGreeting = adapter.find(identifier);
        // Assert
        verify(jpaRepository).findByIdentifier(identifier.toString());
        assertThat(foundGreeting).isNotEmpty();
        var greeting = foundGreeting.get();
        assertThat(greeting.getIdentifier()).isEqualTo(identifier);
        assertThat(greeting.getName()).isEqualTo(name);
        assertThat(greeting.getType()).isSameAs(type);
    }

    @Test
    void find_should_return_empty_if_not_found() {
        // Arrange
        var identifier = UUID.randomUUID();
        given(jpaRepository.findByIdentifier(any())).willReturn(Optional.empty());
        // Act
        var foundGreeting = adapter.find(identifier);
        // Assert
        assertThat(foundGreeting).isEmpty();
        verify(jpaRepository).findByIdentifier(identifier.toString());
    }

    @Nested
    class Put {
        private final String name = "Albert";
        private final GreetingType type = GreetingType.BIRTHDAY;
        private final Greeting greeting = Greeting.of(type.name()).to(name).build();
        private final GreetingTypeJpaEntity typeEntity = new GreetingTypeJpaEntity();
        @Mock
        private GreetingJpaEntity jpaEntity;

        @BeforeEach
        void setUp() {
            typeEntity.setName(type.name());
            given(typeJpaRepository.findByName(any())).willReturn(Optional.of(typeEntity));
            given(jpaRepository.save(any())).willAnswer(answer -> answer.getArgument(0));
        }

        @Test
        void put_should_map_to_entity_and_save_it() {
            // Arrange

            // Act
            var returned = adapter.put(greeting);
            // Assert
            var jpaEntityCaptor = ArgumentCaptor.forClass(GreetingJpaEntity.class);
            verify(typeJpaRepository).findByName("BIRTHDAY");
            verify(jpaRepository).save(jpaEntityCaptor.capture());
            var capturedEntity = jpaEntityCaptor.getValue();
            assertThat(capturedEntity).isNotNull();
            assertThat(capturedEntity.getType()).isNotNull().isSameAs(typeEntity);
            assertThat(capturedEntity.getType().getName()).isEqualTo(type.name());
            assertThat(capturedEntity.getName()).isEqualTo(name);
            assertThat(capturedEntity.getIdentifier()).isEqualTo(greeting.getIdentifier().toString());
            assertThat(capturedEntity.getId()).isZero();
            assertThat(capturedEntity.getCreatedAt()).isBetween(LocalDateTime.now().minus(10, SECONDS), LocalDateTime.now());
            assertThat(returned).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(greeting);
        }

        @Test
        void put_should_check_if_greeting_already_exists_use_the_existing_one() {
            // Arrange
            given(jpaRepository.findByIdentifier(any())).willReturn(Optional.of(jpaEntity));
            // Act
            var returned = adapter.put(greeting);
            // Assert
            verify(jpaRepository).findByIdentifier(greeting.getIdentifier().toString());
            verify(typeJpaRepository).findByName("BIRTHDAY");
            verify(jpaRepository).save(jpaEntity);
            verify(jpaEntity).setType(typeEntity);
            assertThat(returned).isSameAs(greeting);
        }
    }
}