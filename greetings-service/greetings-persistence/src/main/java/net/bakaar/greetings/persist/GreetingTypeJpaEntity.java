package net.bakaar.greetings.persist;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "T_TYPES")
public class GreetingTypeJpaEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_T_TYPES", unique = true, updatable = false, nullable = false)
    private long id;

    @Column(name = "S_NAME", nullable = false)
    private String name;
}