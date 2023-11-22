drop table if exists organizations cascade;
drop table if exists passports cascade;
drop table if exists users cascade;

drop table if exists document_types cascade;
drop table if exists attributes cascade;
drop table if exists type_attributes cascade;
drop table if exists document_changes cascade;
drop table if exists attribute_values cascade;
drop table if exists document_process cascade;
drop table if exists documents cascade;


create table if not exists organizations
(
    org_id
    bigint
    not
    null
    generated
    by
    default as
    identity
    primary
    key,
    name
    varchar
(
    320
) not null,
    inn varchar
(
    10
)
    );

create table if not exists passports
(
    passport_id
    bigint
    not
    null
    generated
    by
    default as
    identity
    primary
    key,
    series
    varchar
(
    4
) not null,
    number varchar
(
    6
) not null,
    issued varchar
(
    320
) not null,
    date timestamp without time zone not null,
    kp varchar
(
    6
) not null
    );

create table if not exists users
(
    user_id
    bigint
    not
    null
    generated
    by
    default as
    identity
    primary
    key,
    last_name
    varchar
(
    100
) not null,
    first_name varchar
(
    100
) not null,
    patronymic varchar
(
    100
),
    date_of_birth timestamp without time zone not null,
    email varchar
(
    320
) UNIQUE not null,
    phone varchar
(
    11
) UNIQUE not null,
    user_password varchar
(
    255
),
    post varchar
(
    320
) not null,
    role varchar
(
    5
) not null,
    passport_id bigint not null references passports
(
    passport_id
),
    org_id bigint not null references organizations
(
    org_id
)
    );


create table if not exists attributes
(
    attribute_id
    bigint
    not
    null
    generated
    by
    default as
    identity
    primary
    key,
    name
    varchar
(
    320
) not null,
    type varchar
(
    500
) not null
    );

create table if not exists attribute_values
(
    value_id
    bigint
    not
    null
    generated
    by
    default as
    identity
    primary
    key,
    attribute_value
    varchar
(
    1000
) not null,
    attribute_id bigint not null references attributes
(
    attribute_id
),
    document_id bigint
    );


create table if not exists document_types
(
    type_id
    bigint
    not
    null
    generated
    by
    default as
    identity
    primary
    key,
    name
    varchar
(
    320
) not null,
    agreement_type varchar
(
    16
) DEFAULT 'EVERYONE'
    );

create table if not exists documents
(
    document_id
    bigint
    not
    null
    generated
    by
    default as
    identity
    primary
    key,
    name
    varchar
(
    256
),
    document_path varchar
(
    1000
),
    created_at timestamp without time zone not null,
    organization_id bigint not null references organizations
(
    org_id
),
    owner_Id bigint not null references users
(
    user_id
),
    type_id bigint references document_types
(
    type_id
),
    value_id bigint references attribute_values
(
    value_id
),
    final_doc_status varchar
(
    32
) DEFAULT 'WAITING_FOR_APPROVE',
    CONSTRAINT correct_document_date
    CHECK
(
    created_at <= NOW
(
)),
    CONSTRAINT fk_attribute_values_id
    FOREIGN KEY
(
    value_id
)
    REFERENCES attribute_values
(
    value_id
)
                         ON DELETE CASCADE
    );


create table if not exists type_attributes
(
    type_id bigint not null references document_types
(
    type_id
),
    attribute_id bigint not null references attributes
(
    attribute_id
)
    );

create table if not exists document_changes
(
    changes_id
    bigint
    not
    null
    generated
    by
    default as
    identity
    primary
    key,
    date_of_change
    timestamp
    without
    time
    zone
    not
    null,
    user_Changer_Id
    bigint
    not
    null
    references
    users
(
    user_id
),
    changes varchar
(
    1000
) not null,
    previous_version varchar
(
    1000
) not null,
    document_id bigint not null,
    CONSTRAINT fk_document_id
    FOREIGN KEY
(
    document_id
)
    REFERENCES documents
(
    document_id
) ON DELETE CASCADE
    );

create table if not exists document_process
(
    process_id
    bigint
    not
    null
    generated
    by
    default as
    identity
    primary
    key,
    document_id
    bigint,
    sender_id
    bigint
    not
    null
    references
    users
(
    user_id
),
    recipient_id bigint not null references users
(
    user_id
),
    status varchar
(
    20
) not null,
    comment varchar
(
    1000
) not null
    );



