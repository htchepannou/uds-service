CREATE TABLE t_domain(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    deleted BIT DEFAULT 0,
    from_date DATETIME,
    to_date DATETIME,

    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
) engine=InnoDB;

CREATE TABLE t_user_status_code(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(100) NOT NULL UNIQUE,
    active BIT(1),
    default_status BIT(1)
);


CREATE TABLE t_user(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    party_fk BIGINT NOT NULL REFERENCES t_party(id),

    deleted BIT DEFAULT 0,
    from_date DATETIME,
    to_date DATETIME,

    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(50),
    status CHAR(1)
);

CREATE TABLE t_user_status (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    user_fk BIGINT NOT NULL REFERENCES t_user(id),
    status_code_fk BIGINT NOT NULL REFERENCES t_user_status_code(id),

    status_date DATETIME,
    comment TEXT
);
ALTER TABLE t_user ADD COLUMN status_fk BIGINT REFERENCES t_user_status(id);



CREATE TABLE t_role(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE t_permission(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE t_role_permission(
    role_fk BIGINT NOT NULL REFERENCES t_role(id),
    permission_fk BIGINT NOT NULL REFERENCES t_permission(id),

    PRIMARY KEY (role_fk, permission_fk)
);


CREATE TABLE t_domain_user(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    domain_fk BIGINT NOT NULL REFERENCES t_domain(id),
    user_fk BIGINT NOT NULL REFERENCES t_user(id),
    role_fk BIGINT NOT NULL REFERENCES t_role(id),

    from_date DATETIME,

    UNIQUE(domain_fk, user_fk, role_fk)
);

CREATE TABLE t_access_token(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    user_fk BIGINT NOT NULL REFERENCES t_user(id),
    domain_fk BIGINT NOT NULL REFERENCES t_domain(id),

    from_date DATETIME,
    to_date DATETIME,
    expiry_date DATETIME NOT NULL,

    expired BIT(1),
    user_agent VARCHAR(2048),
    remote_ip VARCHAR(50)
);



