-- V2__Create_users.sql
-- Creates the users table.
--
-- A user is anyone who registers in the application.
-- Each user has exactly ONE role (many users -> one role).
--
-- Security notes:
--   - 'password' stores the BCrypt hash, NEVER the plain-text password.
--     BCrypt output looks like: $$2a$$10$$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
--   - 'email' and 'username' are unique so we can look up a user by either.
--   - 'enabled' allows us to disable accounts without deleting them.

CREATE TABLE users (
    id         BIGSERIAL     PRIMARY KEY,
    username   VARCHAR(50)   NOT NULL UNIQUE,
    email      VARCHAR(255)  NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,
    enabled    BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    -- Foreign key to roles table.
    -- ON DELETE RESTRICT means: you cannot delete a role that has users.
    -- This protects data integrity.
    role_id    BIGINT        NOT NULL,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE RESTRICT
);

-- Indexes for the columns we frequently search/filter by.
-- Without indexes, PostgreSQL does a full table scan for every query.
-- With indexes, lookups are O(log n) instead of O(n).
CREATE INDEX idx_users_email    ON users (email);
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_role_id  ON users (role_id);