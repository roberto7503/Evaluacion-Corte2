-- Creación de la base de datos
CREATE DATABASE papeleria_campus;

-- Creación del esquema
CREATE SCHEMA IF NOT EXISTS registros;

-- Creación de la tabla producto
CREATE TABLE IF NOT EXISTS registros.producto (
id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
codigo VARCHAR(10) NOT NULL UNIQUE,
nombre VARCHAR(80) NOT NULL,
categoria VARCHAR(20) NOT NULL,
precio NUMERIC(10, 2) NOT NULL,
stock INTEGER NOT NULL,

-- Código normalizado no vacío (entre 1 y 10 caracteres sin espacios)
CONSTRAINT chk_producto_codigo 
   CHECK (LENGTH(TRIM(codigo)) >= 1 AND LENGTH(TRIM(codigo)) <= 10),

-- Nombre entre 3 y 80 caracteres tras quitar espacios
CONSTRAINT chk_producto_nombre 
    CHECK (LENGTH(TRIM(nombre)) >= 3 AND LENGTH(TRIM(nombre)) <= 80),

-- Categoría restringida únicamente a las 3 válidas
CONSTRAINT chk_producto_categoria 
    CHECK (categoria IN ('Papelería', 'Tecnología', 'Accesorios')),

-- Precio positivo (0.01 a 99999999.99)
CONSTRAINT chk_producto_precio 
    CHECK (precio >= 0.01 AND precio <= 99999999.99),

-- Stock no negativo (0 a 2147483647)
CONSTRAINT chk_producto_stock 
    CHECK (stock >= 0 AND stock <= 2147483647)
);

-- Limpieza y reinicio del identificador autoincremental
TRUNCATE TABLE registros.producto RESTART IDENTITY;

-- Inserción de los 12 registros de prueba oficiales
INSERT INTO registros.producto (codigo, nombre, categoria, precio, stock) 
VALUES
('P001', 'Cuaderno universitario', 'Papelería', 65.00, 25),
('P002', 'Cuaderno cuadriculado', 'Papelería', 72.50, 12),
('P003', 'Lapicero azul', 'Papelería', 12.00, 80),
('P004', 'Resma de papel carta', 'Papelería', 185.00, 0),
('T001', 'Memoria USB 32 GB', 'Tecnología', 210.00, 8),
('T002', 'Mouse inalámbrico', 'Tecnología', 320.00, 6),
('T003', 'Teclado USB', 'Tecnología', 450.00, 4),
('T004', 'Cable HDMI', 'Tecnología', 180.00, 0),
('A001', 'Mochila escolar', 'Accesorios', 850.00, 5),
('A002', 'Estuche de lápices', 'Accesorios', 95.00, 18),
('A003', 'Botella reutilizable', 'Accesorios', 240.00, 0),
('A004', 'Regla metálica', 'Accesorios', 45.00, 20);