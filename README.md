# TallerCarPro

TallerCarPro es un sistema de gestión para talleres automotrices, diseñado para administrar clientes, vehículos, facturas, reparaciones y balances semanales. Actualmente, el proyecto utiliza Java con JDBC para interactuar con una base de datos PostgreSQL, siguiendo un diseño modular y robusto. **Está planificado escalar el proyecto a Spring Boot** para incorporar características avanzadas como APIs REST, inyección de dependencias y una arquitectura más escalable.

## Estado Actual y Futuro

- **Estado Actual**: El sistema está implementado en Java 17 con JDBC, proporcionando operaciones CRUD para entidades como clientes, vehículos, facturas y reparaciones. Utiliza PostgreSQL como base de datos y Maven para la gestión de dependencias.
- **Plan Futuro**: El proyecto migrará a **Spring Boot** para:
  - Implementar una API RESTful para interacción con clientes y frontend.
  - Utilizar Spring Data JPA para simplificar el acceso a datos.
  - Incorporar Spring Security para autenticación y autorización.
  - Facilitar la integración con frontend (e.g., React, Angular) y despliegue en la nube.

## Características

- **Gestión de Clientes**: Registro y actualización de información de clientes (nombre, correo, teléfono, identificación).
- **Gestión de Vehículos**: Administración de vehículos asociados a clientes (marca, modelo, año, placa, tipo).
- **Facturación**: Creación y consulta de facturas con costos de mano de obra, repuestos y otros, soportando pagos en efectivo y banco.
- **Reparaciones**: Gestión de reparaciones, incluyendo tipo, descripción, costo de mano de obra, mecánico asignado, estado y repuestos utilizados.
- **Balances Semanales**: Cálculo de ingresos por método de pago (efectivo y banco) para reportes financieros.
- **Base de Datos**: Integración con mysql para almacenamiento persistente.

## Tecnologías

- **Lenguaje**: Java 17
- **Base de Datos**: PostgreSQL 15
- **Gestión de Dependencias**: Maven
- **Acceso a Datos**: JDBC (actualmente), Spring Data JPA (futuro)
- **Framework Futuro**: Spring Boot
- **Otras Librerías**: PostgreSQL JDBC Driver

TallerCarPro/ ├── src/ │ ├── main/ │ │ ├── java/ │ │ │ ├── org/acardona/java/taller/ │ │ │ │ 
├── domain/ │ │ │ │ │ ├── Client.java │ │ │ │ │ ├── Vehicle.java │ │ │ │ │ ├── Invoice.java │ │ │ │ │ ├── Repair.java │ │ │ │ │ ├── Mechanic.java │ │ │ │ │ ├── SparePart.java │ │ │ 
│ │ ├── SupplierInvoice.java │ │ │ │ │ ├── Supplier.java │ │ │ │ │ ├── PaymentMethod.java │ │ │ │ ├── repository/ │ │ │ │ │ ├── Repository.java │ │ │ │ │ ├── jdbc/ │ │ │ │ │ │ ├── JdbcClientRepository.java │ │ │ │ │
│ ├── JdbcVehicleRepository.java │ │ │ │ │ │ ├── JdbcInvoiceRepository.java │ │ │ │ │ │ ├── JdbcRepairRepository.java │ │ │ │ ├── util/ │ │ │ │ │ ├── DatabaseConnection.java │ │ ├── resources/ │ │ │ ├── application.properties ├── pom.xml ├── README.md


## Requisitos Previos

- **Java**: JDK 17 o superior
- **Maven**: 3.8.x o superior
- **Mysql**: 15 o superior
- **Sistema Operativo**: Linux, Windows o macOS
- **Herramientas**:
  - `mysql` para administrar la base de datos
  - Git para clonar el repositorio


## Requisitos Previos

- **Java**: JDK 17 o superior
- **Maven**: 3.8.x o superior
- **MySQL**: 8.0 o superior
- **Sistema Operativo**: Linux, Windows o macOS
- **Herramientas**:
  - MySQL Client (`mysql`) para administrar la base de datos
  - Git para clonar el repositorio

## Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/your-username/TallerCarPro.git
cd TallerCarPro

2. Configurar MySQl

Inicia MySQL y crea una base de datos:

mysql -u root -p
Crea las tablas necesarias ejecutando el siguiente script SQL (schema.sql):

USE tallercarpro;

CREATE TABLE clients (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    identification VARCHAR(50) NOT NULL
);

CREATE TABLE vehicles (
    id VARCHAR(36) PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    license_plate VARCHAR(20) NOT NULL,
    type VARCHAR(50) NOT NULL,
    client_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE mechanics (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE suppliers (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_info TEXT
);

CREATE TABLE supplier_invoices (
    id VARCHAR(36) PRIMARY KEY,
    date DATETIME NOT NULL,
    supplier_id VARCHAR(36) NOT NULL,
    vehicle_id VARCHAR(36),
    total DOUBLE NOT NULL,
    description TEXT,
    paid BOOLEAN NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

CREATE TABLE spare_parts (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    cost DOUBLE NOT NULL,
    profit_percentage DOUBLE NOT NULL,
    supplier_invoice_id VARCHAR(36),
    FOREIGN KEY (supplier_invoice_id) REFERENCES supplier_invoices(id)
);

CREATE TABLE repairs (
    id VARCHAR(36) PRIMARY KEY,
    repair_type VARCHAR(100) NOT NULL,
    description TEXT,
    labor_cost DOUBLE NOT NULL,
    mechanic_labor_percentage DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    vehicle_id VARCHAR(36) NOT NULL,
    mechanic_id VARCHAR(36) NOT NULL,
    start_date DATETIME NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id)
);

CREATE TABLE repair_spare_parts (
    repair_id VARCHAR(36),
    spare_part_id VARCHAR(36),
    PRIMARY KEY (repair_id, spare_part_id),
    FOREIGN KEY (repair_id) REFERENCES repairs(id),
    FOREIGN KEY (spare_part_id) REFERENCES spare_parts(id)
);

CREATE TABLE invoices (
    id VARCHAR(36) PRIMARY KEY,
    date DATETIME NOT NULL,
    client_id VARCHAR(36) NOT NULL,
    vehicle_id VARCHAR(36) NOT NULL,
    labor_cost DOUBLE NOT NULL,
    spare_parts_cost DOUBLE NOT NULL,
    other_costs DOUBLE NOT NULL,
    total DOUBLE NOT NULL,
    is_vehicle_delivered BOOLEAN NOT NULL,
    payment_method ENUM('EFECTIVO', 'BANCO') NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

CREATE TABLE weekly_balances (
    id VARCHAR(36) PRIMARY KEY,
    week_start_date DATE NOT NULL,
    week_end_date DATE NOT NULL,
    efectivo_income DOUBLE NOT NULL,
    banco_income DOUBLE NOT NULL
);
uarda este script en un archivo schema.sql y ejecuta:

mysql -u root -p tallercarpro < schema.sql

3. Configurar Conexión a la Base de Datos

Edita src/main/resources/application.properties con las credenciales de tu base de datos:

db.url=jdbc:mysql://localhost:3306/tallercarpro
db.username=root
db.password=your_password

4. Actualizar Dependencias en pom.xml

Asegúrate de incluir el conector de MySQL en pom.xml:

<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>

5. Compilar el Proyecto

mvn clean install

Ejecución

Ejecuta una clase de prueba, como Main.java:

mvn exec:java -Dexec.mainClass="org.acardona.java.taller.Main"

Aquí está un ejemplo de clase principal para probar el sistema:

 ```java package org.acardona.java.taller;

import org.acardona.java.taller.domain.; import org.acardona.java.taller.repository.jdbc.;

import java.time.LocalDateTime;

public class Main { public static void main(String[] args) { JdbcClientRepository clientRepo = new JdbcClientRepository(); JdbcVehicleRepository vehicleRepo = new JdbcVehicleRepository(clientRepo); JdbcMechanicRepository mechanicRepo = new JdbcMechanicRepository(); JdbcSupplierRepository supplierRepo = new JdbcSupplierRepository(); JdbcSupplierInvoiceRepository supplierInvoiceRepo = new JdbcSupplierInvoiceRepository(vehicleRepo, supplierRepo); JdbcSparePartRepository sparePartRepo = new JdbcSparePartRepository(supplierInvoiceRepo); JdbcRepairRepository repairRepo = new JdbcRepairRepository(vehicleRepo, mechanicRepo, sparePartRepo, supplierInvoiceRepo);

    // Crear cliente
    Client client = new Client("John Doe", "john@example.com", "123456789", "ID123");
    clientRepo.save(client);

    // Crear vehículo
    Vehicle vehicle = new Vehicle("Toyota", "Corolla", 2020, "ABC123", "Sedan", client);
    vehicleRepo.save(vehicle);

    // Crear mecánico
    Mechanic mechanic = new Mechanic("Mike Smith", "mike@example.com");
    mechanicRepo.save(mechanic);

    // Crear proveedor
    Supplier supplier = new Supplier("AutoParts Inc.", "contact@autoparts.com");
    supplierRepo.save(supplier);

    // Crear factura de proveedor
    SupplierInvoice supplierInvoice = new SupplierInvoice(LocalDateTime.now(), supplier, vehicle, 50.0, "Filtro de aceite");
    supplierInvoiceRepo.save(supplierInvoice);

    // Crear repuesto
    SparePart sparePart = new SparePart("Filtro de aceite", "Filtro estándar", 20.0, 30.0, supplierInvoice);
    sparePartRepo.save(sparePart);

    // Crear reparación
    Repair repair = new Repair(
        "Cambio de aceite",
        "Cambio de aceite y filtro",
        50.0,
        60.0,
        "Pendiente",
        vehicle,
        mechanic,
        LocalDateTime.now()
    );
    repair.addSparePart(sparePart);
    repairRepo.save(repair);

    // Consultar reparación
    repairRepo.findById(repair.getId()).ifPresent(r -> {
        System.out.println("Reparación: " + r.getRepairType() + ", Vehículo: " + r.getVehicle().getBrand());
    });
}

}
