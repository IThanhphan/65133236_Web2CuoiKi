CREATE DATABASE boarding_house_management;
USE boarding_house_management;

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    role_id BIGINT NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_role
        FOREIGN KEY(role_id)
        REFERENCES roles(id)
);

CREATE TABLE rooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_code VARCHAR(20) NOT NULL UNIQUE,
    room_name VARCHAR(100) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    area DECIMAL(6,2),
    max_people INT,
    status ENUM(
        'AVAILABLE',
        'RENTED',
        'MAINTENANCE'
    ) DEFAULT 'AVAILABLE',

    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tenants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    citizen_id VARCHAR(20) NOT NULL UNIQUE,
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contracts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    contract_code VARCHAR(30) NOT NULL UNIQUE,

    room_id BIGINT NOT NULL,

    tenant_id BIGINT NOT NULL,

    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    deposit_amount DECIMAL(12,2) DEFAULT 0,

    monthly_rent DECIMAL(12,2) NOT NULL,

    status ENUM(
        'ACTIVE',
        'EXPIRED',
        'TERMINATED'
    ) DEFAULT 'ACTIVE',

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_contract_room
        FOREIGN KEY(room_id)
        REFERENCES rooms(id),

    CONSTRAINT fk_contract_tenant
        FOREIGN KEY(tenant_id)
        REFERENCES tenants(id)
);

CREATE TABLE invoices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    invoice_code VARCHAR(30) NOT NULL UNIQUE,

    contract_id BIGINT NOT NULL,

    billing_month INT NOT NULL,
    billing_year INT NOT NULL,

    room_fee DECIMAL(12,2) DEFAULT 0,

    electricity_fee DECIMAL(12,2) DEFAULT 0,

    water_fee DECIMAL(12,2) DEFAULT 0,

    internet_fee DECIMAL(12,2) DEFAULT 0,

    service_fee DECIMAL(12,2) DEFAULT 0,

    total_amount DECIMAL(12,2) NOT NULL,

    status ENUM(
        'PAID',
        'UNPAID'
    ) DEFAULT 'UNPAID',

    due_date DATE,

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_invoice_contract
        FOREIGN KEY(contract_id)
        REFERENCES contracts(id)
);

CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    invoice_id BIGINT NOT NULL,

    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,

    amount DECIMAL(12,2) NOT NULL,

    payment_method VARCHAR(50),

    note TEXT,

    CONSTRAINT fk_payment_invoice
        FOREIGN KEY(invoice_id)
        REFERENCES invoices(id)
);

INSERT INTO rooms
(
    room_code,
    room_name,
    price,
    area,
    max_people,
    status,
    description
)