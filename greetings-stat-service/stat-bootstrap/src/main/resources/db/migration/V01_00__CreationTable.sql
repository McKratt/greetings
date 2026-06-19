CREATE TABLE t_counter
(
    pk_t_counter SERIAL PRIMARY KEY NOT NULL,
    s_name TEXT NOT NULL UNIQUE,
    l_count      INTEGER            NOT NULL
);