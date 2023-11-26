DROP TABLE IF EXISTS organizations CASCADE;


DROP TABLE IF EXISTS passports CASCADE;


DROP TABLE IF EXISTS users CASCADE;


DROP TABLE IF EXISTS document_types CASCADE;


DROP TABLE IF EXISTS attributes CASCADE;


DROP TABLE IF EXISTS type_attributes CASCADE;


DROP TABLE IF EXISTS document_changes CASCADE;


DROP TABLE IF EXISTS attribute_values CASCADE;


DROP TABLE IF EXISTS document_process CASCADE;


DROP TABLE IF EXISTS documents CASCADE;


CREATE TABLE IF NOT EXISTS organizations
(
    org_id
    bigint
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    varchar
(
    320
) NOT NULL,
    inn varchar
(
    10
));


CREATE TABLE IF NOT EXISTS passports
(
    passport_id
    bigint
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    series
    varchar
(
    4
) NOT NULL, number varchar
(
    6
) NOT NULL,
    issued varchar
(
    320
) NOT NULL, date timestamp WITHOUT TIME ZONE NOT NULL,
    kp varchar
(
    6
) NOT NULL);


CREATE TABLE IF NOT EXISTS users
(
    user_id
    bigint
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    last_name
    varchar
(
    100
) NOT NULL,
    first_name varchar
(
    100
) NOT NULL,
    patronymic varchar
(
    100
),
    date_of_birth timestamp WITHOUT TIME ZONE NOT NULL,
    email varchar
(
    320
) UNIQUE NOT NULL,
    phone varchar
(
    11
) UNIQUE NOT NULL,
    user_password varchar
(
    255
),
    post varchar
(
    320
) NOT NULL,
    ROLE varchar
(
    5
) NOT NULL,
    passport_id bigint NOT NULL REFERENCES passports
(
    passport_id
),
    org_id bigint NOT NULL REFERENCES organizations
(
    org_id
));


CREATE TABLE IF NOT EXISTS attributes
(
    attribute_id
    bigint
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    varchar
(
    320
) NOT NULL,
    TYPE varchar
(
    500
) NOT NULL);


CREATE TABLE IF NOT EXISTS attribute_values
(
    value_id
    bigint
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    attribute_value
    varchar
(
    1000
) NOT NULL,
    attribute_id bigint NOT NULL REFERENCES attributes
(
    attribute_id
),
    document_id bigint);


CREATE TABLE IF NOT EXISTS document_types
(
    type_id
    bigint
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    varchar
(
    320
) NOT NULL,
    agreement_type varchar
(
    16
) DEFAULT 'EVERYONE');


CREATE TABLE IF NOT EXISTS documents
(
    document_id
    bigint
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    varchar
(
    256
),
    document_path varchar
(
    1000
),
    created_at timestamp WITHOUT TIME ZONE NOT NULL,
    organization_id bigint NOT NULL REFERENCES organizations
(
    org_id
),
    owner_Id bigint NOT NULL REFERENCES users
(
    user_id
),
    type_id bigint REFERENCES document_types
(
    type_id
),
    value_id bigint REFERENCES attribute_values
(
    value_id
),
    final_doc_status varchar
(
    32
) DEFAULT 'WAITING_FOR_APPROVE',
    CONSTRAINT correct_document_date CHECK
(
    created_at <= NOW
(
)), CONSTRAINT fk_attribute_values_id
    FOREIGN KEY
(
    value_id
) REFERENCES attribute_values
(
    value_id
)
                         ON DELETE CASCADE);


CREATE TABLE IF NOT EXISTS type_attributes
(
    type_id bigint NOT NULL REFERENCES document_types
(
    type_id
),
    attribute_id bigint NOT NULL REFERENCES attributes
(
    attribute_id
));


CREATE TABLE IF NOT EXISTS document_changes
(
    changes_id
    bigint
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    date_of_change
    timestamp
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    user_Changer_Id
    bigint
    NOT
    NULL
    REFERENCES
    users
(
    user_id
),
    changes varchar
(
    1000
) NOT NULL,
    previous_version varchar
(
    1000
) NOT NULL,
    document_id bigint NOT NULL,
    CONSTRAINT fk_document_id
    FOREIGN KEY
(
    document_id
) REFERENCES documents
(
    document_id
) ON DELETE CASCADE);


CREATE TABLE IF NOT EXISTS document_process
(
    process_id
    bigint
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    document_id
    bigint,
    sender_id
    bigint
    NOT
    NULL
    REFERENCES
    users
(
    user_id
),
    recipient_id bigint NOT NULL REFERENCES users
(
    user_id
),
    status varchar
(
    20
) NOT NULL,
    COMMENT varchar
(
    1000
) NOT NULL);