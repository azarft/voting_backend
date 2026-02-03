CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE voting_sessions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE options (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES voting_sessions(id),
    text VARCHAR(255) NOT NULL
);

CREATE TABLE votes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    session_id BIGINT NOT NULL REFERENCES voting_sessions(id),
    option_id BIGINT NOT NULL REFERENCES options(id),
    timestamp TIMESTAMP NOT NULL,
    UNIQUE (user_id, session_id)
);

-- Insert admin user
INSERT INTO users (email, role, verified) VALUES ('argen.azanov@alatoo.edu.kg', 'ADMIN', true);
