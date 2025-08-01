-- Agregar columna notas a la tabla trabajos
ALTER TABLE trabajos ADD COLUMN notas VARCHAR(255);

-- Actualizar registros existentes con un valor por defecto
UPDATE trabajos SET notas = 'Sin notas iniciales';



