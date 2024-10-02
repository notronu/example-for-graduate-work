-- liquibase formatted sql

-- changeset sasha:1
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(50) NOT NULL,
                       password VARCHAR(30) NOT NULL,
                       first_name VARCHAR(30),
                       last_name VARCHAR(30),
                       phone VARCHAR(15),
                       image VARCHAR(255),
                       role VARCHAR(20)
);

CREATE TABLE ads
(
    id          SERIAL PRIMARY KEY,
    price       INTEGER,
    title       VARCHAR(255),
    description TEXT,
    image       VARCHAR(255),
    user_id     INTEGER REFERENCES users (id)
);

CREATE TABLE comments
(
    id         SERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    text       TEXT,
    user_id    INTEGER REFERENCES users (id),
    ads_id INTEGER REFERENCES ads (id)
);

CREATE TABLE images
(
    id serial NOT NULL,
    size bigint,
    content_type character varying(255),
    path character varying(255),

    CONSTRAINT images_pkey PRIMARY KEY (id),
    CONSTRAINT ad_id_unique UNIQUE (ad_id)
);

ALTER TABLE images
    ADD CONSTRAINT ad_pk_fkey FOREIGN KEY (ad_id)
        REFERENCES ads (pk)
        ON DELETE CASCADE
;