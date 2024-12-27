-- Crear la base de datos
CREATE DATABASE SampleDB;

-- Cambiar al contexto de la base de datos recién creada
USE SampleDB;

-- Crear la tabla
CREATE TABLE Files (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(255) NOT NULL,
    inputFile NVARCHAR(255),
    outputFile NVARCHAR(255),
    transformFile NVARCHAR(255)
);

-- Insertar datos iniciales
INSERT INTO Files (name, inputFile, outputFile, transformFile)
VALUES
('File1', 'input1.txt', 'output1.txt', 'transform1.txt'),
('File2', 'input2.txt', 'output2.txt', 'transform2.txt'),
('File3', 'input3.txt', 'output3.txt', 'transform3.txt');