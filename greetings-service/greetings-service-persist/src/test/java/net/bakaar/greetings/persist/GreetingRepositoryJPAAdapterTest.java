package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GreetingRepositoryJPAAdapterTest {

    @Mock
    private DomainToEntityMapper mapper;
    @Mock
    private GreetingJpaRepository jpaRepository;
    @InjectMocks
    private GreetingRepositoryJPAAdapter adapter;

    @Test
    void put_should_map_to_entity_and_save_it() {
        // Given
        Greeting greeting = mock(Greeting.class);
        GreetingJpaEntity jpaEntity = mock(GreetingJpaEntity.class);
        given(mapper.mapToEntity(greeting)).willReturn(jpaEntity);
        given(jpaRepository.save(any())).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        given(mapper.mapToDomain(jpaEntity)).willReturn(greeting);
        // When
        Greeting returned = adapter.put(greeting);
        // Then
        verify(mapper).mapToEntity(greeting);
        verify(jpaRepository).save(jpaEntity);
        verify(mapper).mapToDomain(jpaEntity);
        assertThat(returned).isSameAs(greeting);
    }
}