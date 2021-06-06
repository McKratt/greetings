package net.bakaar.greetings.persist;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "T_GREETINGS")
public class GreetingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_T_GREETINGS")
    private long id;

    @Column(name = "S_IDENTIFIER", nullable = false, updatable = false)
    private String identifier;

    @Column(name = "S_NAME", nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "FK_TYPE", referencedColumnName = "PK_T_TYPES", nullable = false)
    private GreetingTypeJpaEntity type;

    @Column(name = "TS_CREATEDAT", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


}
