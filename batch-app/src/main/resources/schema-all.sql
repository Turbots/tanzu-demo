DROP TABLE loan_rate IF EXISTS;

CREATE TABLE loan_rate  (
    loan_number BIGINT IDENTITY NOT NULL PRIMARY KEY,
    new_loan_rate DOUBLE
);