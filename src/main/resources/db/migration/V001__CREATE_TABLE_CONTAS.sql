CREATE TABLE IF NOT EXISTS contas (
    id BIGSERIAL NOT NULL,
    agencia TEXT NOT NULL,
    conta TEXT NOT NULL,
    saldo numeric NOT NULL,
    data_criacao DATETIME NOT NULL,
    ativo bool NOT NULL,
    CONSTRAINT pk_contas PRIMARY KEY (id)
);