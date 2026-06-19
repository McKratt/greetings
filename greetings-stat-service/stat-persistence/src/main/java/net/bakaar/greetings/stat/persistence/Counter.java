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
@Table("t_counter")
public class Counter {
    @Id
    @Column("pk_t_counter")
    private long id;
    @Column("s_name")
    private String name;
    @Column("l_count")
    private long count = 0;
}
