-- Insert Homestay 1: Ronin Engineer Hotel
INSERT INTO public.homestays (name, description, type, host_id, status, phone_number, address, longitude, latitude,
                              geom, images, guests, bedrooms, bathrooms, extra_data, version, created_at, created_by,
                              updated_at, updated_by)
VALUES ('Engineer Hotel', 'A comfortable and central hotel for software engineers and travelers.', 1, 1, 1,
        '0362228388', '123 Nguyen Hue, District 1, Ho Chi Minh City', 105.85054073130831, 21.010219168557075,
        ST_Transform(ST_SetSRID(ST_MakePoint(105.85054073130831, 21.010219168557075), 4326), 3857),
        ARRAY ['https://example.com/image1.jpg', 'https://example.com/image2.jpg'], 2, 1, 1, '{}'::jsonb, 1,
        '2024-12-20 15:59:14.977+00', null, null, null);


-- Insert availability for Homestay 1
INSERT INTO public.homestay_availabilities (homestay_id, date, price, status)
VALUES (1, '2024-12-20', 20, 0),
       (1, '2024-12-21', 20, 0),
       (1, '2024-12-22', 20, 2),
       (1, '2024-12-23', 20, 0);

-- Insert Homestay 2: Saigon Central Hostel
INSERT INTO public.homestays (name, description, type, host_id, status, phone_number, address, longitude, latitude,
                              geom, images, guests, bedrooms, bathrooms, extra_data, version, created_at, created_by,
                              updated_at, updated_by)
VALUES ('Saigon Central Hostel', 'Modern and cozy hostel in central HCMC', 1, 2, 1, '0909111222',
        'Ben Thanh, District 1', 106.700424, 10.776889,
        ST_Transform(ST_SetSRID(ST_MakePoint(106.700424, 10.776889), 4326), 3857), null, 4, 2, 2, null, 1, NOW(), null,
        null, null);

-- Insert availability for Homestay 2
INSERT INTO public.homestay_availabilities (homestay_id, date, price, status)
VALUES (2, '2024-12-20', 30, 0),
       (2, '2024-12-21', 30, 0),
       (2, '2024-12-22', 30, 0),
       (2, '2024-12-23', 30, 0);

-- Insert Homestay 3: Hanoi Old Quarter Stay
INSERT INTO public.homestays (name, description, type, host_id, status, phone_number, address, longitude, latitude,
                              geom, images, guests, bedrooms, bathrooms, extra_data, version, created_at, created_by,
                              updated_at, updated_by)
VALUES ('Hanoi Old Quarter Stay', 'Historic home in the Old Quarter', 1, 3, 1, '0988123456', 'Hoan Kiem Lake',
        105.854444, 21.028511, ST_Transform(ST_SetSRID(ST_MakePoint(105.854444, 21.028511), 4326), 3857), null, 3, 2, 1,
        null, 1, NOW(), null, null, null);

-- Insert availability for Homestay 3
INSERT INTO public.homestay_availabilities (homestay_id, date, price, status)
VALUES (3, '2024-12-20', 25, 0),
       (3, '2024-12-21', 25, 1),
       (3, '2024-12-22', 25, 2),
       (3, '2024-12-23', 25, 0);

-- HomestayStatus: DRAFT(-1), INACTIVE(0), ACTIVE(1), CLOSED(2)
-- AvailabilityStatus: AVAILABLE(0), HELD(1), BOOKED(2), SERVED(3)