CREATE TABLE IF NOT EXISTS parcelas (
    id BIGSERIAL NOT NULL,
    valor numeric NOT NULL,
    transferencia_id BIGSERIAL NOT NULL,
    data_criacao DATETIME NOT NULL,
    data_agendada DATE NOT NULL,
    data_transacao DATETIME,
    status TEXT NOT NULL,
    CONSTRAINT pk_parcelas PRIMARY KEY (id),
    CONSTRAINT fk_transferencia_id FOREIGN KEY (transferencia_id) REFERENCES transferencias (id)
);