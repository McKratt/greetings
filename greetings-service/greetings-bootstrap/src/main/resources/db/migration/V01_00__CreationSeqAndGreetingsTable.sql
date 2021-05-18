CREATE TABLE T_GREETINGS
(
    PK_T_GREETINGS SERIAL PRIMARY KEY,
    S_IDENTIFIER   varchar(36) NOT NULL,
    S_NAME         varchar(20) NOT NULL,
    S_TYPE         varchar(15) NOT NULL,
    TS_CREATEDAT   timestamp   NOT NULL
);