CREATE TABLE IF NOT EXISTS transferencias (
    id BIGSERIAL NOT NULL,
    numero_transacao TEXT NOT NULL,
    conta_id BIGSERIAL NOT NULL,
    conta_destino TEXT NOT NULL,
    data_criacao DATETIME NOT NULL,
    data_atualizacao DATETIME NOT NULL,
    status TEXT NOT NULL,
    CONSTRAINT pk_transferencias PRIMARY KEY (id),
    CONSTRAINT fk_conta_id FOREIGN KEY (conta_id) REFERENCES contas (id)
);