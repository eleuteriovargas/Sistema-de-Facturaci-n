
ALTER TABLE trabajos ADD COLUMN fecha_pago DATE;

CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    monto DECIMAL(10,2) NOT NULL,
    fecha_pago DATE NOT NULL,
    trabajo_id BIGINT NOT NULL,
    CONSTRAINT fk_pago_trabajo FOREIGN KEY (trabajo_id) REFERENCES trabajos(id)
);

CREATE INDEX idx_pagos_trabajo ON pagos(trabajo_id);


