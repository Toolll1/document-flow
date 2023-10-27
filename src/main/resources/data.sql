insert into organizations (name, inn) values ('ООО ЗеленоглазоеТакси', '0000000000');
insert into passports (series, number,issued,date,kp) values ('4523','148322','MVD PO MOSKVE','2020-11-20 00:00:00.000000','770006');
insert into users (last_name, first_name, patronymic, date_of_birth, email,phone, user_password, post,role, passport_id, org_id) values ('Admin','Admin','Admin','2002-09-26 00:00:00.000000','admin@mail.ru','89853661411','$2a$10$cQFePvGZSKi/XA4J0.BoQeki1cjpniwMJTCP5ByUGQJhyAAkHeMlG','Test Post','ADMIN',1,1);
INSERT INTO documents (document_id, title, document_path, created_at, organization_id, owner_id, type_id)
VALUES
    (1, 'Test Document 1', '/path/to/document1', NOW(), 1, 1, null),
    (2, 'Test Document 2', '/path/to/document2', NOW(), 1, 1, null);