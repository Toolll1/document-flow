insert into organizations (name, inn) values ('Greenatom', '4420098300');
insert into passports (series, number,issued,date,kp) values ('4523','148322','MVD PO MOSKVE','2020-11-20 00:00:00.000000','770006');
insert into users (last_name, first_name, patronymic, date_of_birth, email,phone, user_password, post,role, passport_id, org_id)
values ('Admin','Admin','Admin','2002-09-26 00:00:00.000000','admin@mail.ru','89853661411',
        '$2a$10$cQFePvGZSKi/XA4J0.BoQeki1cjpniwMJTCP5ByUGQJhyAAkHeMlG','Test Post','ADMIN',1,1);
