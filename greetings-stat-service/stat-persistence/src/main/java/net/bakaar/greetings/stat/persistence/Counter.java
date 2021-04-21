package net.bakaar.greetings.stat.persistence;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Accessors(chain = true)
@Getter
@Table("T_COUNTER")
public class Counter {


    @Id
    @Column("PK_T_COUNTER")
    private long id;
    @Column("S_NAME")
    private String name;
    @Column("L_COUNT")
    private long count = 0;
}
