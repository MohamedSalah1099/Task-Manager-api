-- V4__Create_refresh_tokens.sql
-- Creates the refresh_tokens table.
--
-- Why store refresh tokens in the database?
--   Unlike access tokens (stateless JWTs), refresh tokens are stored in the DB so we can:
--   1. Invalidate them on logout (just delete the row).
--   2. Prevent reuse of stolen refresh tokens.
--   3. Enforce one refresh token per user at a time.
--
-- The relationship is ONE-TO-ONE: each user has at most one active refresh token.
-- When a new refresh token is issued (on login or refresh), the old one is replaced.
--
-- Why VARCHAR(512) for the token?
--   UUID v4 is 36 chars. We use a random UUID string, so 512 gives plenty of room.

CREATE TABLE refresh_tokens (
    id          BIGSERIAL    PRIMARY KEY,
    token       VARCHAR(512) NOT NULL UNIQUE,
    expiry_date TIMESTAMP    NOT NULL,

    -- One-to-one: each user has at most one refresh token at a time.
    -- ON DELETE CASCADE: if user is deleted, their refresh token is also deleted.
    user_id     BIGINT       NOT NULL UNIQUE,
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Index on token value because we look up tokens by value on every refresh request.
CREATE INDEX idx_refresh_tokens_token   ON refresh_tokens (token);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);