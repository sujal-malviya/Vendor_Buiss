-- =====================================================================
-- VendorHub demo data
--
-- Inserts 6 demo rows into every table. Safe to run again: it first
-- deletes the previous demo data, and it never touches your own rows.
-- Demo rows are recognised by their email domain: @demo.vendorhub.test
--
-- Run it (from the project folder):
--   psql -U postgres -h localhost -d vendor_onboarding -f scripts/demo-data.sql
--
-- Every demo account logs in with password:  password123
--   admin@demo.vendorhub.test    (role ADMIN - can edit dishes, event types, required items)
--   spice@demo.vendorhub.test, royal@demo.vendorhub.test, green@demo.vendorhub.test,
--   coastal@demo.vendorhub.test, punjab@demo.vendorhub.test   (role VENDOR)
--
-- Start the app once before running this, so Hibernate has created the tables.
-- =====================================================================

BEGIN;

-- ---------------------------------------------------------------------
-- 1. Remove old demo data (children first, because of foreign keys)
-- ---------------------------------------------------------------------
CREATE TEMP TABLE demo_vendor_ids ON COMMIT DROP AS
    SELECT id FROM vendors WHERE email LIKE '%@demo.vendorhub.test';

CREATE TEMP TABLE demo_profile_ids ON COMMIT DROP AS
    SELECT id FROM vendorprofile WHERE vendor_id IN (SELECT id FROM demo_vendor_ids);

DELETE FROM package_dish WHERE package_id IN (SELECT id FROM package WHERE vendor_id IN (SELECT id FROM demo_vendor_ids));
DELETE FROM package      WHERE vendor_id IN (SELECT id FROM demo_vendor_ids);
DELETE FROM vendor_dish  WHERE vendor_id IN (SELECT id FROM demo_vendor_ids);

DELETE FROM vendor_buissness_information WHERE vendor_id IN (SELECT id FROM demo_profile_ids);
DELETE FROM vendor_bank_detail           WHERE vendor_id IN (SELECT id FROM demo_profile_ids);
DELETE FROM vendor_service_area          WHERE vendor_id IN (SELECT id FROM demo_profile_ids);
DELETE FROM vendor_payment_plan          WHERE vendor_id IN (SELECT id FROM demo_profile_ids);
DELETE FROM order_policies               WHERE vendor_id IN (SELECT id FROM demo_profile_ids);
DELETE FROM vendor_media                 WHERE vendor_id IN (SELECT id FROM demo_profile_ids);
DELETE FROM vendorprofile                WHERE id IN (SELECT id FROM demo_profile_ids);
DELETE FROM vendors                      WHERE id IN (SELECT id FROM demo_vendor_ids);

-- ---------------------------------------------------------------------
-- 2. Shared lists (dish catalog, event types, required items)
--    Only inserted if a row with the same name doesn't exist yet.
-- ---------------------------------------------------------------------
INSERT INTO dish (name, description, category, cuisine, vegetarian, active)
SELECT d.name, d.description, d.category, d.cuisine, d.vegetarian, TRUE
FROM (VALUES
    ('Paneer Tikka',        'Cottage cheese cubes grilled in a tandoor',  'Starter',     'North Indian', TRUE),
    ('Chicken Biryani',     'Hyderabadi dum biryani with raita',          'Main Course', 'Hyderabadi',   FALSE),
    ('Dal Makhani',         'Slow-cooked black lentils with butter',      'Main Course', 'Punjabi',      TRUE),
    ('Masala Dosa',         'Crispy dosa with potato filling',            'Main Course', 'South Indian', TRUE),
    ('Fish Curry',          'Coconut-based Goan fish curry',              'Main Course', 'Goan',         FALSE),
    ('Gulab Jamun',         'Milk dumplings in sugar syrup',              'Dessert',     'North Indian', TRUE)
) AS d(name, description, category, cuisine, vegetarian)
WHERE NOT EXISTS (SELECT 1 FROM dish x WHERE x.name = d.name);

INSERT INTO event_type (name, description, active)
VALUES
    ('Wedding',          'Wedding ceremonies and receptions', TRUE),
    ('Birthday Party',   'Birthday celebrations',             TRUE),
    ('Corporate Event',  'Office parties and conferences',    TRUE),
    ('Engagement',       'Engagement and ring ceremonies',    TRUE),
    ('Housewarming',     'Griha pravesh and housewarming',    TRUE),
    ('Religious Event',  'Pooja, festivals and kirtans',      TRUE)
ON CONFLICT (name) DO NOTHING;

INSERT INTO required_item (name, description, unit, active)
SELECT r.name, r.description, r.unit, TRUE
FROM (VALUES
    ('Banquet Chairs',    'Cushioned chairs with covers',       'piece'),
    ('Round Tables',      '6-seater round tables with cloth',   'piece'),
    ('LED Lighting',      'Decorative LED light strings',       'set'),
    ('Diesel Generator',  '25 kVA power backup',                'unit'),
    ('Chafing Dishes',    'Steel food warmers for the buffet',  'piece'),
    ('Water Dispenser',   '20 litre water dispenser',           'unit')
) AS r(name, description, unit)
WHERE NOT EXISTS (SELECT 1 FROM required_item x WHERE x.name = r.name);

-- ---------------------------------------------------------------------
-- 3. Vendor accounts (password for all: password123, stored as a BCrypt hash)
-- ---------------------------------------------------------------------
INSERT INTO vendors (email, phone, password, role)
VALUES
    ('admin@demo.vendorhub.test',   '9000000001', '$2a$10$WHwImO//1Ebzt6sVw16qDutE6sUpYpZEbcjhg9F0hoQ.d.cYCHVH.', 'ADMIN'),
    ('spice@demo.vendorhub.test',   '9000000002', '$2a$10$WHwImO//1Ebzt6sVw16qDutE6sUpYpZEbcjhg9F0hoQ.d.cYCHVH.', 'VENDOR'),
    ('royal@demo.vendorhub.test',   '9000000003', '$2a$10$WHwImO//1Ebzt6sVw16qDutE6sUpYpZEbcjhg9F0hoQ.d.cYCHVH.', 'VENDOR'),
    ('green@demo.vendorhub.test',   '9000000004', '$2a$10$WHwImO//1Ebzt6sVw16qDutE6sUpYpZEbcjhg9F0hoQ.d.cYCHVH.', 'VENDOR'),
    ('coastal@demo.vendorhub.test', '9000000005', '$2a$10$WHwImO//1Ebzt6sVw16qDutE6sUpYpZEbcjhg9F0hoQ.d.cYCHVH.', 'VENDOR'),
    ('punjab@demo.vendorhub.test',  '9000000006', '$2a$10$WHwImO//1Ebzt6sVw16qDutE6sUpYpZEbcjhg9F0hoQ.d.cYCHVH.', 'VENDOR');

-- ---------------------------------------------------------------------
-- 4. One profile per vendor
-- ---------------------------------------------------------------------
INSERT INTO vendorprofile (vendor_name, vendor_address, vendor_id)
SELECT d.name, d.address, v.id
FROM (VALUES
    ('admin@demo.vendorhub.test',   'VendorHub Kitchen',     'Koramangala, Bangalore 560034'),
    ('spice@demo.vendorhub.test',   'Spice Route Caterers',  'Indiranagar, Bangalore 560038'),
    ('royal@demo.vendorhub.test',   'Royal Feast Catering',  'Banjara Hills, Hyderabad 500034'),
    ('green@demo.vendorhub.test',   'Green Leaf Veg Caterers','Kothrud, Pune 411038'),
    ('coastal@demo.vendorhub.test', 'Coastal Flavours',      'Panjim, Goa 403001'),
    ('punjab@demo.vendorhub.test',  'Punjab Da Dhaba Caterers','Sector 17, Chandigarh 160017')
) AS d(email, name, address)
JOIN vendors v ON v.email = d.email;

-- ---------------------------------------------------------------------
-- 5. Onboarding sections (one row per profile)
-- ---------------------------------------------------------------------
INSERT INTO vendor_buissness_information
    (buissness_name, contact_number, location_address, gst_number, fssai_number, years_of_buissness, vendor_id)
SELECT d.name, d.contact, d.address, d.gst, d.fssai, d.years, p.id
FROM (VALUES
    ('admin@demo.vendorhub.test',   'VendorHub Kitchen Pvt Ltd',   '9000000001', 'Koramangala, Bangalore',   '29AABCV1234A1Z5', '11223344556601', 3),
    ('spice@demo.vendorhub.test',   'Spice Route Caterers LLP',    '9000000002', 'Indiranagar, Bangalore',   '29AADCS5678B1Z2', '11223344556602', 8),
    ('royal@demo.vendorhub.test',   'Royal Feast Catering Co',     '9000000003', 'Banjara Hills, Hyderabad', '36AAECR9012C1Z9', '11223344556603', 12),
    ('green@demo.vendorhub.test',   'Green Leaf Foods',            '9000000004', 'Kothrud, Pune',            '27AAFCG3456D1Z4', '11223344556604', 5),
    ('coastal@demo.vendorhub.test', 'Coastal Flavours Catering',   '9000000005', 'Panjim, Goa',              '30AAGCC7890E1Z7', '11223344556605', 6),
    ('punjab@demo.vendorhub.test',  'Punjab Da Dhaba Caterers',    '9000000006', 'Sector 17, Chandigarh',    '04AAHCP2345F1Z1', '11223344556606', 15)
) AS d(email, name, contact, address, gst, fssai, years)
JOIN vendors v ON v.email = d.email
JOIN vendorprofile p ON p.vendor_id = v.id;

INSERT INTO vendor_bank_detail (account_number, account_holder_name, ifsc_code, vendor_id)
SELECT d.account, d.holder, d.ifsc, p.id
FROM (VALUES
    ('admin@demo.vendorhub.test',   '50100011112221', 'VendorHub Kitchen Pvt Ltd', 'HDFC0000123'),
    ('spice@demo.vendorhub.test',   '50100011112222', 'Spice Route Caterers LLP',  'ICIC0000456'),
    ('royal@demo.vendorhub.test',   '50100011112223', 'Royal Feast Catering Co',   'SBIN0000789'),
    ('green@demo.vendorhub.test',   '50100011112224', 'Green Leaf Foods',          'UTIB0000321'),
    ('coastal@demo.vendorhub.test', '50100011112225', 'Coastal Flavours Catering', 'KKBK0000654'),
    ('punjab@demo.vendorhub.test',  '50100011112226', 'Punjab Da Dhaba Caterers',  'PUNB0000987')
) AS d(email, account, holder, ifsc)
JOIN vendors v ON v.email = d.email
JOIN vendorprofile p ON p.vendor_id = v.id;

-- Database rules: max_people <= 1000, minimum_order_size >= 200,
-- maximum_order_size <= 1000, minimum_order_value >= 250
INSERT INTO vendor_service_area
    (max_people, service_pin, service_city, service_country, order_size,
     minimum_order_size, maximum_order_size, minimum_order_value, vendor_id)
SELECT d.max_people, d.pin, d.city, 'India', d.order_size, d.min_size, d.max_size, d.min_value, p.id
FROM (VALUES
    ('admin@demo.vendorhub.test',   1000, '560034', 'Bangalore',  500, 200, 1000, 300),
    ('spice@demo.vendorhub.test',    800, '560038', 'Bangalore',  400, 200,  800, 350),
    ('royal@demo.vendorhub.test',   1000, '500034', 'Hyderabad',  600, 250, 1000, 450),
    ('green@demo.vendorhub.test',    500, '411038', 'Pune',       300, 200,  500, 250),
    ('coastal@demo.vendorhub.test',  600, '403001', 'Goa',        350, 200,  600, 400),
    ('punjab@demo.vendorhub.test',   900, '160017', 'Chandigarh', 500, 300,  900, 300)
) AS d(email, max_people, pin, city, order_size, min_size, max_size, min_value)
JOIN vendors v ON v.email = d.email
JOIN vendorprofile p ON p.vendor_id = v.id;

INSERT INTO vendor_payment_plan (advance_payment, pre_payment, post_payment, vendor_id)
SELECT d.advance, d.pre, d.post, p.id
FROM (VALUES
    ('admin@demo.vendorhub.test',   '20% at booking', '50% one week before event', '30% after event'),
    ('spice@demo.vendorhub.test',   '25% at booking', '50% three days before event', '25% after event'),
    ('royal@demo.vendorhub.test',   '30% at booking', '50% one week before event', '20% after event'),
    ('green@demo.vendorhub.test',   '10% at booking', '60% two days before event', '30% after event'),
    ('coastal@demo.vendorhub.test', '20% at booking', '40% one week before event', '40% after event'),
    ('punjab@demo.vendorhub.test',  '25% at booking', '45% five days before event', '30% after event')
) AS d(email, advance, pre, post)
JOIN vendors v ON v.email = d.email
JOIN vendorprofile p ON p.vendor_id = v.id;

INSERT INTO order_policies (modify_orders, cancel_grace_period, max_allowed_duration, vendor_id)
SELECT d.modify, d.grace, d.duration, p.id
FROM (VALUES
    ('admin@demo.vendorhub.test',   ARRAY['Change guest count', 'Change menu items']::varchar[],        TRUE,  '48 hours before event'),
    ('spice@demo.vendorhub.test',   ARRAY['Change guest count']::varchar[],                             TRUE,  '72 hours before event'),
    ('royal@demo.vendorhub.test',   ARRAY['Change menu items', 'Change event time']::varchar[],         FALSE, '7 days before event'),
    ('green@demo.vendorhub.test',   ARRAY['Change guest count', 'Add extra dishes']::varchar[],         TRUE,  '24 hours before event'),
    ('coastal@demo.vendorhub.test', ARRAY['Change menu items']::varchar[],                              TRUE,  '48 hours before event'),
    ('punjab@demo.vendorhub.test',  ARRAY['Change guest count', 'Change menu items', 'Add extra dishes']::varchar[], FALSE, '5 days before event')
) AS d(email, modify, grace, duration)
JOIN vendors v ON v.email = d.email
JOIN vendorprofile p ON p.vendor_id = v.id;

INSERT INTO vendor_media (image, video, vendor_id)
SELECT 'https://cdn.demo.vendorhub.test/' || d.slug || '/cover.jpg',
       'https://cdn.demo.vendorhub.test/' || d.slug || '/intro.mp4',
       p.id
FROM (VALUES
    ('admin@demo.vendorhub.test',   'vendorhub-kitchen'),
    ('spice@demo.vendorhub.test',   'spice-route'),
    ('royal@demo.vendorhub.test',   'royal-feast'),
    ('green@demo.vendorhub.test',   'green-leaf'),
    ('coastal@demo.vendorhub.test', 'coastal-flavours'),
    ('punjab@demo.vendorhub.test',  'punjab-da-dhaba')
) AS d(email, slug)
JOIN vendors v ON v.email = d.email
JOIN vendorprofile p ON p.vendor_id = v.id;

-- ---------------------------------------------------------------------
-- 6. Menu: packages, dishes inside packages, vendor dish prices
-- ---------------------------------------------------------------------
INSERT INTO package (name, description, price, price_unit, active, vendor_id)
SELECT d.name, d.description, d.price, 'per plate', TRUE, v.id
FROM (VALUES
    ('admin@demo.vendorhub.test',   'Standard Buffet',  'Starter, 2 mains, dessert',         450.00),
    ('spice@demo.vendorhub.test',   'Silver Package',   'Veg starter, dal, rice, dessert',   350.00),
    ('royal@demo.vendorhub.test',   'Royal Biryani',    'Biryani, starter, dessert',         650.00),
    ('green@demo.vendorhub.test',   'Pure Veg Thali',   'Complete vegetarian thali',         300.00),
    ('coastal@demo.vendorhub.test', 'Goan Seafood',     'Fish curry, rice, dessert',         700.00),
    ('punjab@demo.vendorhub.test',  'Punjabi Gold',     'Paneer tikka, dal makhani, dessert', 550.00)
) AS d(email, name, description, price)
JOIN vendors v ON v.email = d.email;

INSERT INTO package_dish (package_id, dish_id, quantity)
SELECT pk.id, ds.id, d.quantity
FROM (VALUES
    ('admin@demo.vendorhub.test',   'Standard Buffet', 'Paneer Tikka',    2),
    ('spice@demo.vendorhub.test',   'Silver Package',  'Dal Makhani',     1),
    ('royal@demo.vendorhub.test',   'Royal Biryani',   'Chicken Biryani', 1),
    ('green@demo.vendorhub.test',   'Pure Veg Thali',  'Masala Dosa',     1),
    ('coastal@demo.vendorhub.test', 'Goan Seafood',    'Fish Curry',      1),
    ('punjab@demo.vendorhub.test',  'Punjabi Gold',    'Gulab Jamun',     2)
) AS d(email, package_name, dish_name, quantity)
JOIN vendors v   ON v.email = d.email
JOIN package pk  ON pk.vendor_id = v.id AND pk.name = d.package_name
JOIN LATERAL (SELECT id FROM dish WHERE name = d.dish_name ORDER BY id LIMIT 1) ds ON TRUE;

INSERT INTO vendor_dish (vendor_id, dish_id, price, available)
SELECT v.id, ds.id, d.price, d.available
FROM (VALUES
    ('admin@demo.vendorhub.test',   'Paneer Tikka',    220.00, TRUE),
    ('spice@demo.vendorhub.test',   'Dal Makhani',     180.00, TRUE),
    ('royal@demo.vendorhub.test',   'Chicken Biryani', 320.00, TRUE),
    ('green@demo.vendorhub.test',   'Masala Dosa',     120.00, TRUE),
    ('coastal@demo.vendorhub.test', 'Fish Curry',      380.00, FALSE),
    ('punjab@demo.vendorhub.test',  'Gulab Jamun',      80.00, TRUE)
) AS d(email, dish_name, price, available)
JOIN vendors v ON v.email = d.email
JOIN LATERAL (SELECT id FROM dish WHERE name = d.dish_name ORDER BY id LIMIT 1) ds ON TRUE;

COMMIT;

-- ---------------------------------------------------------------------
-- 7. Summary: total rows now in each table
-- ---------------------------------------------------------------------
SELECT 'vendors' AS table_name, count(*) AS total_rows FROM vendors
UNION ALL SELECT 'vendorprofile',                count(*) FROM vendorprofile
UNION ALL SELECT 'vendor_buissness_information', count(*) FROM vendor_buissness_information
UNION ALL SELECT 'vendor_bank_detail',           count(*) FROM vendor_bank_detail
UNION ALL SELECT 'vendor_service_area',          count(*) FROM vendor_service_area
UNION ALL SELECT 'vendor_payment_plan',          count(*) FROM vendor_payment_plan
UNION ALL SELECT 'order_policies',               count(*) FROM order_policies
UNION ALL SELECT 'vendor_media',                 count(*) FROM vendor_media
UNION ALL SELECT 'dish',                         count(*) FROM dish
UNION ALL SELECT 'event_type',                   count(*) FROM event_type
UNION ALL SELECT 'required_item',                count(*) FROM required_item
UNION ALL SELECT 'package',                      count(*) FROM package
UNION ALL SELECT 'package_dish',                 count(*) FROM package_dish
UNION ALL SELECT 'vendor_dish',                  count(*) FROM vendor_dish;
