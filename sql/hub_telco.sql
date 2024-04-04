CREATE TABLE IF NOT EXISTS telco_transaction
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
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
    card_type VARCHAR(32),
    card_seri VARCHAR(32),
    card_code VARCHAR(32),
    state INT,
    error INT DEFAULT 0,
    hub_callback_url VARCHAR(2048) DEFAULT '',
    provider_callback_data VARCHAR(2048) DEFAULT '{}',
    expired_time BIGINT  DEFAULT 0,
    create_time BIGINT,
    update_time BIGINT  DEFAULT 0
);
CREATE UNIQUE INDEX telco_transaction_client_index ON telco_transaction (client, client_transaction_id);
CREATE UNIQUE INDEX telco_transaction_provider_index ON telco_transaction (provider, provider_transaction_id);
CREATE UNIQUE INDEX telco_transaction_card_index ON telco_transaction (card_type, card_seri, card_code);
CREATE INDEX telco_transaction_state_error_index ON telco_transaction (state, error);
CREATE INDEX telco_transaction_create_time_index ON telco_transaction (create_time);