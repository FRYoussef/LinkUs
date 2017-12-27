CREATE DATABASE almacenDb;
USE almacenDb;

CREATE TABLE Asignatura(
    Id int(5) AUTO_INCREMENT,
    Nombre varchar(30),
    CONSTRAINT PK_Asigantura_Id PRIMARY KEY(Id),
    CONSTRAINT U_Asignatura_Nombre UNIQUE (Nombre)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE TipoCliente(
    Tipo varchar(15),
    CONSTRAINT PK_TipoCliente_Tipo PRIMARY KEY(Tipo)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Cliente(
    Id int(6) AUTO_INCREMENT,
    Usuario varchar(15) NOT NULL,
    Contrasenna varchar(20) NOT NULL,
    Email varchar(30) NOT NULL,
    Tipo varchar(15) NOT NULL,
    CONSTRAINT PK_Cliente_Id PRIMARY KEY (Id),
    CONSTRAINT U_Cliente_Usuario UNIQUE (Usuario),
    CONSTRAINT U_Cliente_Email UNIQUE (Email)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE AsisteA(
    IdAsignatura int(5) AUTO_INCREMENT,
    IdCliente int(6),
    CONSTRAINT PK_AsisteA_IdAsignatura PRIMARY KEY (IdAsignatura, IdCliente),
    CONSTRAINT FK_AsisteA_IdAsignatura FOREIGN KEY (IdAsignatura) REFERENCES
        Asignatura (Id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_AsisteA_IdCliente FOREIGN KEY (IdCliente) REFERENCES
        Cliente (Id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Mensaje(
    Id int(8) AUTO_INCREMENT,
    Mensaje BLOB,
    CONSTRAINT PK_Mensaje_Id PRIMARY KEY (Id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE tieneEnBuzon(
    IdCliente int(6),
    IdMensaje int(8),
    CONSTRAINT PK_tieneEnBuzon_IdCliente_IdMensaje PRIMARY KEY (IdCliente, IdMensaje),
    CONSTRAINT FK_tieneEnBuzon_IdCliente FOREIGN KEY (IdCliente) REFERENCES
        Cliente (Id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_tieneEnBuzon_IdMensaje FOREIGN KEY (IdMensaje) REFERENCES
        Mensaje (Id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Asignatura` (`Id`, `Nombre`) VALUES
(2, 'Android'),
(5, 'Desarrollo de Interfaces'),
(4, 'Estructuras de Algoritmos'),
(1, 'Matematicas'),
(3, 'Programacion de Procesos');

INSERT INTO `TipoCliente` (`Tipo`) VALUES
('Alumno'),
('Profesor');

INSERT INTO `Cliente` (`Id`, `Usuario`, `Contrasenna`, `Email`, `Tipo`) VALUES
(1, 'Pepe', '1234', 'pepe@hotmail.com', 'Alumno'),
(2, 'Juan', '1234', 'juan@hotmail.es', 'Profesor'),
(3, 'Fernando', '1234', 'fer@gmail.com', 'Alumno');

INSERT INTO `AsisteA` (`IdAsignatura`, `IdCliente`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(3, 3),
(4, 1),
(4, 2),
(4, 3),
(5, 1),
(5, 2),
(5, 3);