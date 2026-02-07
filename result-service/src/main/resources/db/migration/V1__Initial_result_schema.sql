CREATE TABLE IF NOT EXISTS voting_sessions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS options (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    session_id BIGINT NOT NULL,
    FOREIGN KEY (session_id) REFERENCES voting_sessions(id)
);

CREATE TABLE IF NOT EXISTS votes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    session_id BIGINT NOT NULL,
    option_id BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    UNIQUE (user_id, session_id),
    FOREIGN KEY (session_id) REFERENCES voting_sessions(id),
    FOREIGN KEY (option_id) REFERENCES options(id)
);
