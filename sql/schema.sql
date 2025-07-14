-- Drop all in correct order
DROP TABLE IF EXISTS
    homestays_amenities,
    homestays_availabilities,
    amenities,
    bookings,
    profiles,
    users,
    homestays,
    wards,
    districts,
    provinces
    CASCADE;

-- Extensions
CREATE EXTENSION IF NOT EXISTS postgis;


-- Provinces
CREATE TABLE IF NOT EXISTS provinces
(
    id            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    province_name TEXT NOT NULL,
    place_id      TEXT,
    country_id    INTEGER
);

-- Districts
CREATE TABLE IF NOT EXISTS districts
(
    id            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    district_name TEXT NOT NULL,
    place_id      TEXT,
    province_id   INTEGER REFERENCES provinces (id)
);

-- Wards
CREATE TABLE IF NOT EXISTS wards
(
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ward_name   TEXT NOT NULL,
    place_id    TEXT,
    district_id INTEGER REFERENCES districts (id)
);

-- Users
CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username   TEXT     NOT NULL UNIQUE,
    password   TEXT     NOT NULL,
    email      TEXT     NOT NULL UNIQUE,
    fullname   TEXT,
    type       SMALLINT NOT NULL,
    status     SMALLINT NOT NULL,
    extra_data JSONB,
    version    BIGINT,
    created_at TIMESTAMPTZ,
    created_by BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by BIGINT
);

-- Profiles
CREATE TABLE IF NOT EXISTS profiles
(
    user_id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY REFERENCES users (id),
    avatar     TEXT,
    work       TEXT,
    about      TEXT,
    interests  TEXT[],
    status     SMALLINT,
    extra_data JSONB,
    version    BIGINT,
    created_at TIMESTAMPTZ,
    created_by BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by BIGINT
);

-- Homestays
CREATE TABLE IF NOT EXISTS homestays
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         TEXT NOT NULL,
    description  TEXT,
    type         INTEGER,
    host_id      BIGINT,
    status       INTEGER,
    phone_number TEXT,
    address      TEXT,
    longitude    DOUBLE PRECISION,
    latitude     DOUBLE PRECISION,
    geom         GEOMETRY(Point, 3857),
    images       TEXT[],
    guests       SMALLINT,
    bedrooms     SMALLINT,
    bathrooms    SMALLINT,
    extra_data   JSONB,
    version      BIGINT,
    created_at   TIMESTAMPTZ,
    created_by   BIGINT,
    updated_at   TIMESTAMPTZ,
    updated_by   BIGINT
);

CREATE INDEX IF NOT EXISTS idx_homestays_geom ON homestays USING gist (geom);

-- Bookings
CREATE TABLE IF NOT EXISTS bookings
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id       BIGINT     NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    homestay_id   BIGINT     NOT NULL REFERENCES homestays (id) ON DELETE CASCADE,
    checkin_date  DATE     NOT NULL,
    checkout_date DATE     NOT NULL,
    guests        SMALLINT NOT NULL,
    status        SMALLINT NOT NULL,
    subtotal      NUMERIC(12, 6),
    fee           NUMERIC(12, 6),
    discount      NUMERIC(12, 6),
    total_amount  NUMERIC  NOT NULL,
    price_detail  JSONB,
    currency      TEXT     NOT NULL,
    note          TEXT,
    request_id    TEXT     NOT NULL,
    version       SMALLINT,
    extra_data    JSONB,
    created_at    TIMESTAMPTZ,
    created_by    BIGINT,
    updated_at    TIMESTAMPTZ,
    updated_by    BIGINT
);

-- Homestay Availabilities
CREATE TABLE IF NOT EXISTS homestays_availabilities
(
    homestay_id BIGINT NOT NULL REFERENCES homestays (id) ON DELETE CASCADE,
    date        DATE NOT NULL,
    price       NUMERIC,
    status      SMALLINT,
    PRIMARY KEY (homestay_id, date)
);

-- Amenities
CREATE TABLE IF NOT EXISTS amenities
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    icon TEXT NOT NULL
);

-- Homestay Amenities (N-N)
CREATE TABLE IF NOT EXISTS homestays_amenities
(
    homestay_id BIGINT    NOT NULL REFERENCES homestays (id) ON DELETE CASCADE,
    amenity_id  INTEGER NOT NULL REFERENCES amenities (id),
    PRIMARY KEY (homestay_id, amenity_id)
);