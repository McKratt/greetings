package net.bakaar.greetings.persist;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@Entity(name = "T_GREETINGS")
public class GreetingJpaEntity {

    @Id
    @Column(name = "PK_T_GREETINGS")
    @GeneratedValue(strategy = SEQUENCE, generator = "SEQ_PK_T_GREETINGS")
    private Long id;

    @Column(name = "S_IDENTIFIER", nullable = false, updatable = false)
    private String identifier;

    @Column(name = "S_NAME", nullable = false)
    private String name;

    @Column(name = "S_TYPE", nullable = false)
    private String type;

    @Column(name = "TS_CREATEDAT", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


}
