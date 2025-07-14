-- Insert users first
INSERT INTO users (username, password, email, fullname, type, status, extra_data, version, created_at, created_by, updated_at, updated_by)
OVERRIDING SYSTEM VALUE
VALUES
    ('admin', 'adminpass', 'admin@example.com', 'Administrator', 1, 1, '{}', 1, NOW(), NULL, NOW(), NULL),
    ('john_doe', 'johnpass', 'john@example.com', 'John Doe', 2, 1, '{}', 1, NOW(), NULL, NOW(), NULL),
    ('jane_smith', 'janepass', 'jane@example.com', 'Jane Smith', 2, 1, '{}', 1, NOW(), NULL, NOW(), NULL);

-- Insert homestays
INSERT INTO homestays (name, description, type, host_id, status, phone_number, address, longitude, latitude,
                       geom, images, guests, bedrooms, bathrooms, extra_data, version, created_at, created_by,
                       updated_at, updated_by)
OVERRIDING SYSTEM VALUE
VALUES
    ('Engineer Hotel', 'A comfortable and central hotel for software engineers and travelers.', 1,
     2, 1, '0362228388', '123 Nguyen Hue, District 1, Ho Chi Minh City',
     105.85054073130831, 21.010219168557075,
     ST_Transform(ST_SetSRID(ST_MakePoint(105.85054073130831, 21.010219168557075), 4326), 3857),
     ARRAY['https://example.com/image1.jpg', 'https://example.com/image2.jpg'], 2, 1, 1, '{}'::jsonb, 1,
     NOW(), NULL, NOW(), NULL),

    ('Saigon Central Hostel', 'Modern and cozy hostel in central HCMC', 1,
     3, 1, '0909111222', 'Ben Thanh, District 1',
     106.700424, 10.776889,
     ST_Transform(ST_SetSRID(ST_MakePoint(106.700424, 10.776889), 4326), 3857),
     NULL, 4, 2, 2, NULL, 1, NOW(), NULL, NOW(), NULL),

    ('Hanoi Old Quarter Stay', 'Historic home in the Old Quarter', 1,
     1, 1, '0988123456', 'Hoan Kiem Lake',
     105.854444, 21.028511,
     ST_Transform(ST_SetSRID(ST_MakePoint(105.854444, 21.028511), 4326), 3857),
     NULL, 3, 2, 1, NULL, 1, NOW(), NULL, NOW(), NULL);

-- Insert availabilities for Engineer Hotel
INSERT INTO homestays_availabilities (homestay_id, date, price, status)
VALUES
    (1, '2025-12-20', 200000, 0),
    (1, '2025-12-21', 200000, 0),
    (1, '2025-12-22', 200000, 2),
    (1, '2025-12-23', 200000, 0);

-- Insert availabilities for Saigon Central Hostel
INSERT INTO homestays_availabilities (homestay_id, date, price, status)
VALUES
    (2, '2025-12-20', 300000, 0),
    (2, '2025-12-21', 300000, 0),
    (2, '2025-12-22', 300000, 0),
    (2, '2025-12-23', 300000, 0);

-- Insert availabilities for Hanoi Old Quarter Stay
INSERT INTO homestays_availabilities (homestay_id, date, price, status)
VALUES
    (3, '2025-12-20', 250000, 0),
    (3, '2025-12-21', 250000, 0),
    (3, '2025-12-22', 250000, 0),
    (3, '2025-12-23', 250000, 0);