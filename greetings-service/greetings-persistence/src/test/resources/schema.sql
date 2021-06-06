create table T_TYPES
(
    PK_T_TYPES bigint auto_increment,
    S_NAME     varchar(255)
);

CREATE TABLE T_GREETINGS
(
    PK_T_GREETINGS bigint auto_increment,
    S_IDENTIFIER   varchar(36) NOT NULL,
    S_NAME         varchar(20) NOT NULL,
    FK_TYPE        bigint      NOT NULL,
    TS_CREATEDAT   timestamp   NOT NULL
);