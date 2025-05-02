CREATE DATABASE IF NOT EXISTS sample;

USE sample;

CREATE TABLE IF NOT EXISTS properties
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    application    VARCHAR(255)                 NOT NULL,
    profile        VARCHAR(128)                 NOT NULL,
    label          VARCHAR(20) DEFAULT 'master' NOT NULL,
    property_key   VARCHAR(256)                 NOT NULL,
    property_value VARCHAR(128)                 NOT NULL,
    description    VARCHAR(255)                 NULL,
    updated_at     TIMESTAMP                    NOT NULL,
    created_at     TIMESTAMP                    NOT NULL
);
