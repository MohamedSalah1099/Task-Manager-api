-- V3__Create_tasks.sql
-- Creates the tasks table.
--
-- A task belongs to exactly ONE user (many tasks -> one user).
-- Users can only see and modify their own tasks (ownership is enforced in Java, not SQL).
--
-- Why VARCHAR for status and priority instead of a PostgreSQL ENUM type?
--   PostgreSQL ENUMs are harder to modify (ALTER TYPE requires a table rewrite).
--   Using VARCHAR with a CHECK constraint is easier to evolve.
--   Our Java @Enumerated(EnumType.STRING) stores the enum name as a string.
--
-- Why TEXT for description?
--   TEXT in PostgreSQL has no size limit (up to 1GB).
--   VARCHAR(2000) would work too but TEXT is more flexible.

CREATE TABLE tasks (
    id          BIGSERIAL     PRIMARY KEY,
    title       VARCHAR(200)  NOT NULL,
    description TEXT,

    -- Stored as strings matching our Java enums: 'TODO', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'
    status      VARCHAR(30)   NOT NULL DEFAULT 'TODO',

    -- Stored as strings matching our Java enums: 'LOW', 'MEDIUM', 'HIGH', 'URGENT'
    priority    VARCHAR(30)   NOT NULL DEFAULT 'MEDIUM',

    category    VARCHAR(100),
    due_date    TIMESTAMP,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,

    -- Foreign key to users table.
    -- ON DELETE CASCADE means: if a user is deleted, all their tasks are deleted too.
    -- Per docs/03_DATABASE_DESIGN.md: "Cascade delete Tasks."
    user_id     BIGINT        NOT NULL,
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Indexes for the columns we filter and sort by most often.
-- These columns appear in WHERE clauses in GET /tasks?status=TODO&priority=HIGH
CREATE INDEX idx_tasks_user_id  ON tasks (user_id);
CREATE INDEX idx_tasks_status   ON tasks (status);
CREATE INDEX idx_tasks_priority ON tasks (priority);
CREATE INDEX idx_tasks_category ON tasks (category);
CREATE INDEX idx_tasks_due_date ON tasks (due_date);