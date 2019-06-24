DROP TABLE PAYROLL IF EXISTS;

CREATE TABLE PAYROLL  (
    transaction_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    person_identification VARCHAR(20),
    currency VARCHAR(20),
    tx_ammount DOUBLE,
    account_type VARCHAR(20),
    account_id VARCHAR(20),
    tx_description VARCHAR(20),
    first_last_name VARCHAR(20)
);
