
-- Datos de prueba corregidos
INSERT INTO trabajos (descripcion, monto, estado_pago, fecha_trabajo, fecha_vencimiento, cliente_id, notas)
VALUES
('Prueba vencimiento CORREGIDO', 2000.00, 'VENCIDO', '2023-10-01', '2023-10-15', 1, 'Prueba CORREGIDA'),
('Pago parcial', 3000.00, 'PENDIENTE', CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 5 DAY), 2, 'Prueba de pagos parciales'),
('Trabajo recién creado', 1500.00, 'PENDIENTE', CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 10 DAY), 1, 'Prueba de flujo normal');

-- Pagos iniciales CORREGIDOS
INSERT INTO pagos (monto, fecha_pago, trabajo_id)
VALUES
(1000.00, CURRENT_DATE(), 2);


