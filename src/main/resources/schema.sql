begin;

DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS email_tokens CASCADE;
DROP TABLE IF EXISTS user_parser_settings CASCADE;
DROP TABLE IF EXISTS element_locator CASCADE;
DROP TABLE IF EXISTS parser_results CASCADE;

CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR UNIQUE
);


CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(120) NOT NULL,
    activation_status VARCHAR NOT NULL,
    telegram_id VARCHAR UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id int NOT NULL,
    role_id int NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (ID)
);

CREATE TABLE IF NOT EXISTS email_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(36) UNIQUE,
    expiration_date TIMESTAMP,
    user_id BIGINT,
    token_type VARCHAR,
    FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE IF NOT EXISTS user_parser_settings (
    id BIGSERIAL PRIMARY KEY,
    first_page_url VARCHAR NOT NULL,
    num_of_pages_to_parse INT NOT NULL,
    class_name VARCHAR NOT NULL,
    tag_name VARCHAR NOT NULL,
    css_selector_next_page VARCHAR NOT NULL,
    header TEXT[] NOT NULL,
    output_file_type VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS element_locator (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR NOT NULL,
    path_to_locator VARCHAR NOT NULL,
    extra_pointer VARCHAR,
    user_parser_settings_id BIGINT REFERENCES user_parser_settings (id)
);

CREATE TABLE IF NOT EXISTS parser_results (
    id BIGSERIAL PRIMARY KEY,
    user_parser_settings_id BIGINT REFERENCES user_parser_settings (id),
    user_id BIGINT REFERENCES users (id),
    link_to_download VARCHAR
);

end;