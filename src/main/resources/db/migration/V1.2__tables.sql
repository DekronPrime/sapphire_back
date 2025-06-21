CREATE TABLE employees (
    id BIGINT NOT NULL CONSTRAINT employees_pk PRIMARY KEY,
    username VARCHAR(50) NOT NULL
        CONSTRAINT employees_username_uq UNIQUE,
    password TEXT NOT NULL
        CONSTRAINT employees_password_uq UNIQUE,
    role VARCHAR(50) NOT NULL DEFAULT 'EMPLOYEE' CHECK ( role in ('EMPLOYEE', 'ADMIN') ),
    full_name VARCHAR(255) NOT NULL,
    position VARCHAR(100) NOT NULL DEFAULT 'TRAINEE' CHECK ( position in
        (
        'ADMIN', 'MANAGER', 'TRAINEE', 'HAIRDRESSER', 'STYLIST', 'COLORIST', 'MAKEUP_ARTIST', 'MASSEUR',
             'NAIL_TECHNICIAN', 'COSMETOLOGIST', 'RECEPTIONIST', 'CLEANER'
        )
    ),
    phone_number VARCHAR(20) NOT NULL
        CONSTRAINT employees_phone_number_uq UNIQUE,
    email VARCHAR(255) NOT NULL
        CONSTRAINT employees_email_uq UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' CHECK ( status in ('ACTIVE', 'INACTIVE', 'DELETED') )
);

CREATE TABLE suppliers (
    id BIGINT NOT NULL CONSTRAINT suppliers_pk PRIMARY KEY,
    name VARCHAR(255) NOT NULL
        CONSTRAINT suppliers_name_uq UNIQUE,
    phone_number VARCHAR(20) NOT NULL
        CONSTRAINT suppliers_phone_number_uq UNIQUE,
    email VARCHAR(255)
        CONSTRAINT suppliers_email_uq UNIQUE,
    address VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255) NOT NULL,
    rating DOUBLE PRECISION NOT NULL,
    last_supply_date DATE
);

CREATE TABLE materials (
    id BIGINT NOT NULL CONSTRAINT materials_pk PRIMARY KEY,
    name VARCHAR(100) NOT NULL CONSTRAINT materials_name_uq UNIQUE,
    unit VARCHAR(50) NOT NULL DEFAULT 'ONES' CHECK ( unit in ('ML', 'GR', 'ONES') ),
    quantity INT NOT NULL,
    price NUMERIC(7, 2) NOT NULL,
    category VARCHAR(100) NOT NULL DEFAULT 'SKIN_CARE' CHECK ( category in
        ('HAIR_CARE', 'NAIL_CARE', 'SKIN_CARE', 'TOOLS', 'OTHER')
    ),
    description VARCHAR(500),
    amount INT NOT NULL DEFAULT 0 CHECK (amount >= 0),
    min_amount_threshold INT NOT NULL DEFAULT 3 CHECK (min_amount_threshold >= 1),
    enough_amount_threshold INT NOT NULL DEFAULT 10 CHECK (enough_amount_threshold > min_amount_threshold)
);

CREATE TABLE supplier_materials
(
    supplier_id BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    CONSTRAINT supplier_materials_pk PRIMARY KEY (supplier_id, material_id)
);

CREATE TABLE supplies (
    id BIGINT NOT NULL CONSTRAINT supplies_pk PRIMARY KEY,
    material_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    amount INT NOT NULL,
    supply_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price NUMERIC(8, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' CHECK ( status in ('PENDING', 'CONFIRMED', 'RETURNED') ),
    note VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE usage (
    id BIGINT NOT NULL CONSTRAINT usage_pk PRIMARY KEY,
    material_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    amount_used INT NOT NULL,
    usage_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comment VARCHAR(500)
);