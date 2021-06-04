CREATE TABLE T_GREETINGS
(
    PK_T_GREETINGS SERIAL PRIMARY KEY,
    S_IDENTIFIER   varchar(36) NOT NULL,
    S_NAME         varchar(20) NOT NULL,
    FK_TYPE        INTEGER     NOT NULL,
    TS_CREATEDAT   timestamp   NOT NULL
);

CREATE UNIQUE INDEX I_IDENTIFIER_GREETINGS on T_GREETINGS (S_IDENTIFIER);

CREATE TABLE T_TYPES
(
    PK_T_TYPES SERIAL PRIMARY KEY,
    S_NAME     VARCHAR(36) NOT NULL
);