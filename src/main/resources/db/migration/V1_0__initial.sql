CREATE TABLE t_user_status_code(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(100) NOT NULL UNIQUE,
    active BIT(1),
    default_status BIT(1)
) ENGINE=INNODB;


CREATE TABLE t_user(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    party_fk BIGINT NOT NULL,

    deleted BIT DEFAULT 0,
    from_date DATETIME,
    to_date DATETIME,

    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(50)
) ENGINE=INNODB;


CREATE TABLE t_user_status (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    user_fk BIGINT NOT NULL,
    status_code_fk BIGINT NOT NULL,

    status_date DATETIME,
    comment TEXT,

    CONSTRAINT fk_user_status__user FOREIGN KEY (user_fk) REFERENCES t_user(id),
    CONSTRAINT fk_user_status__status_code FOREIGN KEY (status_code_fk) REFERENCES t_user_status_code(id)
) ENGINE=INNODB;

ALTER TABLE t_user ADD COLUMN status_fk BIGINT REFERENCES t_user_status(id);



CREATE TABLE t_role(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=INNODB;

CREATE TABLE t_permission(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
) ENGINE=INNODB;

CREATE TABLE t_role_permission(
    role_fk BIGINT NOT NULL REFERENCES t_role(id),
    permission_fk BIGINT NOT NULL REFERENCES t_permission(id),

    PRIMARY KEY (role_fk, permission_fk),

    CONSTRAINT fk_role_permission__role FOREIGN KEY (role_fk) REFERENCES t_role(id),
    CONSTRAINT fk_role_permission__permission FOREIGN KEY (permission_fk) REFERENCES t_permission(id)
) ENGINE=INNODB;



CREATE TABLE t_domain(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    deleted BIT DEFAULT 0,
    from_date DATETIME,
    to_date DATETIME,

    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
) ENGINE=INNODB;


CREATE TABLE t_domain_user(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    domain_fk BIGINT NOT NULL,
    user_fk BIGINT NOT NULL,
    role_fk BIGINT NOT NULL,

    from_date DATETIME,

    UNIQUE(domain_fk, user_fk, role_fk),

    CONSTRAINT fk_domain_user__domain FOREIGN KEY(domain_fk) REFERENCES t_domain(id),
    CONSTRAINT fk_domain_user__user FOREIGN KEY(user_fk) REFERENCES t_user(id),
    CONSTRAINT fk_domain_user__role FOREIGN KEY(role_fk) REFERENCES t_role(id)
) ENGINE=INNODB;



CREATE TABLE t_access_token(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,

    user_fk BIGINT NOT NULL,
    domain_fk BIGINT NOT NULL,

    from_date DATETIME,
    to_date DATETIME,
    expiry_date DATETIME NOT NULL,
    expired BIT(1),
    user_agent VARCHAR(2048),
    remote_ip VARCHAR(50),

    CONSTRAINT fk_access_token__user FOREIGN KEY (user_fk) REFERENCES t_user(id),
    CONSTRAINT fk_access_token__domain FOREIGN KEY (domain_fk) REFERENCES t_domain(id)
) ENGINE=INNODB;



