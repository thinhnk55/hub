CREATE TABLE IF NOT EXISTS bank_transaction
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
    provider_transaction_id VARCHAR(256),
    bank_transaction_id VARCHAR(256),
    provider_bank_code VARCHAR(64) DEFAULT '',
    bank_code VARCHAR(64),
    bank_short_name VARCHAR(64),
    bank_full_name VARCHAR(1024),
    bank_owner VARCHAR(1024) DEFAULT '',
    bank_account VARCHAR(64) DEFAULT '',
    message VARCHAR(1024) DEFAULT '',
    state INT,
    error INT DEFAULT 0,
    hub_callback_url VARCHAR(2048) DEFAULT '',
    provider_callback_data VARCHAR(2048) DEFAULT '{}',
    expired_time BIGINT  DEFAULT 0,
    create_time BIGINT,
    update_time BIGINT  DEFAULT 0
);
CREATE UNIQUE INDEX bank_transaction_client_index ON bank_transaction (client, client_transaction_id ASC);
CREATE UNIQUE INDEX bank_transaction_provider_index ON bank_transaction (provider, provider_transaction_id ASC);
CREATE INDEX bank_transaction_message_index ON bank_transaction (message);
CREATE INDEX bank_transaction_state_error_index ON bank_transaction (state, error);
CREATE INDEX bank_transaction_create_time_index ON bank_transaction (create_time);