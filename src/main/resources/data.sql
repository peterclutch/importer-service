INSERT INTO country (iso_code, label) VALUES
('DK', 'Denmark'),
('HK', 'Hong Kong');

INSERT INTO country_administrative_area (iso_code, label, country) VALUES
('DK-84', 'Capital', 'DK'),
('DK-82', 'Central Jutland', 'DK'),
('DK-81', 'North Jutland', 'DK'),
('DK-83', 'South Denmark', 'DK'),
('DK-85', 'Zeeland', 'DK');

INSERT INTO address (id, city, post_code, street, area_code, country) VALUES
(0, 'København K', '1050', 'Kongens Nytorv 18', 'DK-84', 'DK');

INSERT INTO brand (id, name, tableau_id, address_id) VALUES
(0, 'Nine A/S', '72765d2c-9deb-4939-a568-d632660dcc07', 0);

