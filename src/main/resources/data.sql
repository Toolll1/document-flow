insert into organizations (name, inn) values ('ООО ЗеленоглазоеТакси', '0000000000');
insert into organizations (name, inn) values ('ООО Михаил Боярский', '0002300000');
insert into passports (series, number,issued,date,kp) values ('4523','148322','MVD PO MOSKVE','2020-11-20 00:00:00.000000','770006');
insert into users (last_name, first_name, patronymic, date_of_birth, email,phone, user_password, post,role, passport_id, org_id)
values ('Admin','Admin','Admin','2002-09-26 00:00:00.000000','admin@mail.ru','89853661411',
        '$2a$10$cQFePvGZSKi/XA4J0.BoQeki1cjpniwMJTCP5ByUGQJhyAAkHeMlG','Test Post','ADMIN',1,1),
       ('User 1','test','test','2002-09-26 00:00:00.000000','user1@mail.ru','89853661412',
        '$2a$10$zXJXdvir4P4ZYQEsIF4mSOzVLG2fAzdFBTnZreyLb6zly3L9CHTOS','none','USER',1,1),
       ('User 2','test','test','2002-09-26 00:00:00.000000','user2@mail.ru','89853661413',
        '$2a$10$zXJXdvir4P4ZYQEsIF4mSOzVLG2fAzdFBTnZreyLb6zly3L9CHTOS','none','USER',1,1),
       ('User 3','test','test','2002-09-26 00:00:00.000000','user3@mail.ru','89853661414',
        '$2a$10$zXJXdvir4P4ZYQEsIF4mSOzVLG2fAzdFBTnZreyLb6zly3L9CHTOS','none','USER',1,2);
INSERT INTO ATTRIBUTES (NAME, TYPE)
VALUES
    ('Name attribute 1', 'type attribute 1'),
    ('Name attribute 2', 'type attribute 2'),
    ('Name attribute 3', 'type attribute 3'),
    ('Name attribute 4', 'type attribute 4'),
    ('Name attribute 5', 'type attribute 5'),
    ('Name attribute 6', 'type attribute 6');
INSERT INTO DOCUMENT_TYPES (NAME)
VALUES
    ('Накладная'),
    ('Счёт-фактура'),
    ('Опись имущества'),
    ('Письмо'),
    ('Донос');
INSERT INTO documents (name, document_path, created_at, organization_id, owner_id, type_id)
VALUES
    ('Test Document 1', '/path/to/document1', NOW(), 1, 1, 1),
    ('Test Document 2', '/path/to/document2', NOW(), 2, 2, 2),
    ('Test Document 3', '/path/to/document3', NOW(), 2, 4, 2);
INSERT INTO document_process (document_id, sender_id, recipient_id, status, comment)
VALUES
    (1, 1, 2, 'NEW', 'Test Comment 1'),
    (2, 3, 3, 'NEW', '');