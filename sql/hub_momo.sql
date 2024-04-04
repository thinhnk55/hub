CREATE TABLE IF NOT EXISTS momo_transaction
(
    code VARCHAR(8) PRIMARY KEY,
    client VARCHAR(32),
    client_transaction_id VARCHAR(64),
    client_callback_url VARCHAR(2048),
    request_amount INT,
    real_amount INT  DEFAULT 0,
    client_callback_count INT DEFAULT 0,
    client_callback_response VARCHAR(2048) DEFAULT '{}',
    provider VARCHAR(32) DEFAULT '',
    provider_transaction_response VARCHAR(2048) DEFAULT '{}',
    provider_transaction_id VARCHAR(64),
    momo_transaction_id VARCHAR(64),
    name VARCHAR(100) DEFAULT '',
    phone VARCHAR(20) DEFAULT '',
    message VARCHAR(1024) DEFAULT '',
    state INT,
    error INT DEFAULT 0,
    hub_callback_url VARCHAR(2048) DEFAULT '',
    provider_callback_data VARCHAR(2048) DEFAULT '{}',
    expired_time BIGINT  DEFAULT 0,
    create_time BIGINT,
    update_time BIGINT  DEFAULT 0
);
CREATE UNIQUE INDEX momo_transaction_client_index ON momo_transaction (client, client_transaction_id ASC);
CREATE UNIQUE INDEX momo_transaction_provider_index ON momo_transaction (provider, provider_transaction_id ASC);
CREATE INDEX momo_transaction_message_index ON momo_transaction (message ASC);