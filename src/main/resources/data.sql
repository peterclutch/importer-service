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
(0, 'København K', '1050', 'Kongens Nytorv 18', 'DK-84', 'DK'),
(1000, 'Causeway Bay', '9999', '535 535 Jaffe Rd', null, 'HK');

INSERT INTO contact (id, email, first_name, last_name, local_name, mobile, phone, position) VALUES
(1000, 'peter@hallum', 'Peter', 'Hallum', '百香果', '40540592', '40540592', 'Boss-man');

INSERT INTO brand (id, name, tableau_id, address_id) VALUES
(0, 'Nine A/S', '72765d2c-9deb-4939-a568-d632660dcc07', 0);

INSERT INTO factory (id, local_name, name) VALUES
(1000, '假工厂', 'Peter Factory');

INSERT INTO brand_factory (custom_id, brand_id, factory_id, address_id, contact_id) VALUES
('E45', 0, 1000, 1000, 1000);

INSERT INTO product (id, identifier_type, identifier_value, specification, brand_id) VALUES
(1001, 'REFERENCE', 'Apron', 'Lorem ipsum 1', 0),
(1002, 'REFERENCE', 'Coffee Mug', 'Lorem ipsum 2', 0),
(1003, 'REFERENCE', 'Short Shorts', 'Lorem ipsum 3', 0),
(1004, 'REFERENCE', 'Tank Tops', 'Lorem ipsum 4', 0);
