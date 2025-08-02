-- Creación de tablas
-- V1__init.sql
CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    rfc VARCHAR(13),
    email VARCHAR(100),
    telefono VARCHAR(20),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE trabajos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    estado_pago VARCHAR(20) NOT NULL,
    fecha_trabajo DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    cliente_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_trabajo_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT chk_fechas CHECK (fecha_vencimiento >= fecha_trabajo),
    CONSTRAINT chk_monto_positivo CHECK (monto > 0)
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_trabajos_estado ON trabajos(estado_pago);
CREATE INDEX idx_trabajos_cliente ON trabajos(cliente_id);

-- Datos iniciales
INSERT INTO clientes (nombre, rfc, email, telefono) VALUES
('Cliente 1', 'XAXX010101000', 'cliente1@email.com', '555-123-4567'),
('Cliente 2', 'ABC123456789', 'cliente2@email.com', '555-987-6543');

INSERT INTO trabajos (descripcion, monto, estado_pago, fecha_trabajo, fecha_vencimiento, cliente_id) VALUES
('Mantenimiento preventivo', 1500.00, 'PENDIENTE', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1),
('Reparación de generador', 2500.00, 'PAGADO', CURDATE(), CURDATE(), 2),
('Instalación de equipo', 3500.00, 'PENDIENTE', DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 1);