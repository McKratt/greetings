CREATE TABLE t_greetings
(
    pk_t_greetings SERIAL PRIMARY KEY,
    s_identifier   text      NOT NULL,
    s_name         text      NOT NULL,
    fk_type        INTEGER   NOT NULL,
    ts_createdat   timestamp NOT NULL
);

CREATE UNIQUE INDEX i_identifier_greetings on t_greetings (s_identifier);

CREATE TABLE t_types
(
    pk_t_types SERIAL PRIMARY KEY,
    s_name     text NOT NULL
);