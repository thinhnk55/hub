CREATE TABLE IF NOT EXISTS hub_client
(
    client VARCHAR(32) PRIMARY KEY,
    secret_key VARCHAR(2048),
    access_key VARCHAR(2048),
    create_time BIGINT
);