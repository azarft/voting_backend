INSERT INTO users (email, role, verified) VALUES ('argen.azanov@alatoo.edu.kg', 'ADMIN', true) ON CONFLICT (email) DO NOTHING;
INSERT INTO users (email, role, verified) VALUES ('niyazhan.shabdanaliev@alatoo.edu.kg', 'ADMIN', true) ON CONFLICT (email) DO NOTHING;
