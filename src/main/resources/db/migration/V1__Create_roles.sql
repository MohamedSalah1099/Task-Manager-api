-- V1__Create_roles.sql
-- Creates the roles table.
--
-- A role defines what a user is allowed to do.
-- Examples: ROLE_USER, ROLE_ADMIN
--
-- Why BIGSERIAL?
--   BIGSERIAL is PostgreSQL's auto-incrementing 64-bit integer.
--   It automatically generates: 1, 2, 3, ... for each new row.
--   We use BIGSERIAL (64-bit) instead of SERIAL (32-bit) for future-proofing.
--
-- Why a separate roles table instead of a column on users?
--   Normalization (3NF): role names are stored once, not repeated per user.
--   If we rename ROLE_USER to something else, we change one row, not millions.

CREATE TABLE roles (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(50)  NOT NULL UNIQUE
);

-- Indexes on 'name' because we frequently query: WHERE name = 'ROLE_USER'
CREATE INDEX idx_roles_name ON roles (name);

-- Seed the two roles our application needs.
-- These are inserted as part of the migration so every environment
-- (dev, staging, production) always has the correct roles from day one.
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');