package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GreetingRepositoryJPAAdapterTest {

    private final UUID identifier = UUID.randomUUID();
    @Mock
    private DomainToEntityMapper mapper;
    @Mock
    private GreetingJpaRepository jpaRepository;
    @InjectMocks
    private GreetingRepositoryJPAAdapter adapter;
    @Mock
    private Greeting greeting;
    @Mock
    private GreetingJpaEntity jpaEntity;

    @Test
    void find_should_call_repository() {
        // Given
        given(jpaRepository.findByIdentifier(identifier.toString())).willReturn(Optional.of(jpaEntity));
        given(mapper.mapToDomain(jpaEntity)).willReturn(greeting);
        // When
        var foundGreeting = adapter.find(identifier);
        // Then
        assertThat(foundGreeting).isNotEmpty().get().isEqualTo(greeting);
        verify(jpaRepository).findByIdentifier(identifier.toString());
        verify(mapper).mapToDomain(jpaEntity);
    }

    @Test
    void find_should_return_empty_if_not_found() {
        // Given
        given(jpaRepository.findByIdentifier(identifier.toString())).willReturn(Optional.empty());
        // When
        var foundGreeting = adapter.find(identifier);
        // Then
        assertThat(foundGreeting).isEmpty();
        verify(jpaRepository).findByIdentifier(identifier.toString());
        verify(mapper, never()).mapToDomain(any());
    }

    @Nested
    class Put {

        @BeforeEach
        void setUp() {
            given(greeting.getIdentifier()).willReturn(identifier);
            given(jpaRepository.save(any())).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            given(mapper.mapToDomain(jpaEntity)).willReturn(greeting);
        }

        @Test
        void put_should_map_to_entity_and_save_it() {
            // Given
            given(mapper.mapToEntity(greeting)).willReturn(jpaEntity);
            given(jpaRepository.findByIdentifier(identifier.toString())).willReturn(Optional.empty());
            // When
            var returned = adapter.put(greeting);
            // Then
            verify(mapper).mapToEntity(greeting);
            verify(jpaRepository).save(jpaEntity);
            verify(mapper).mapToDomain(jpaEntity);
            assertThat(returned).isSameAs(greeting);
        }

        @Test
        void put_should_check_if_greeting_already_exists_use_the_existing_one() {
            // Given
            given(jpaRepository.findByIdentifier(identifier.toString())).willReturn(Optional.of(jpaEntity));
            given(mapper.mapToEntity(greeting, jpaEntity)).willReturn(jpaEntity);
            // When
            var returned = adapter.put(greeting);
            // Then
            verify(mapper).mapToEntity(greeting, jpaEntity);
            verify(jpaRepository).save(jpaEntity);
            verify(mapper).mapToDomain(jpaEntity);
            assertThat(returned).isSameAs(greeting);
        }
    }
}