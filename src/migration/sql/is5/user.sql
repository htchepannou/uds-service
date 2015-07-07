-- status codes
INSERT INTO t_user_status_code(id, name, active, default_status) values(1, 'new', false, true);
INSERT INTO t_user_status_code(id, name, active, default_status) values(2, 'active', true, false);
INSERT INTO t_user_status_code(id, name, active, default_status) values(3, 'suspended', false, false);

-- users
INSERT INTO t_user(id, party_fk, login, deleted, from_date)
  SELECT party_id, party_id, CONCAT('user-', party_id, '-', RAND()), party_deleted, party_creation_date FROM is5.party WHERE party_type_fk=1;

UPDATE IGNORE uds.t_user U
  JOIN is5.pattr A ON U.id=A.pattr_party_fk
  JOIN is5.party P ON U.id=P.party_id
SET U.login=A.pattr_value
WHERE A.pattr_name='uname' AND P.party_deleted=false
;

UPDATE uds.t_user U
  JOIN is5.pattr A ON U.id=A.pattr_party_fk
SET U.password=A.pattr_value
WHERE A.pattr_name='password';

-- Domain users
INSERT INTO t_domain_user(domain_fk, user_fk, role_fk)
  SELECT 1, id, 1 FROM uds.t_user;

INSERT INTO t_domain_user(domain_fk, user_fk, role_fk)
  SELECT 2, id, 100 FROM uds.t_user U JOIN is5.sysadmin A ON U.id=A.sysadmin_id;

-- User status
INSERT INTO t_user_status(id, user_fk, status_code_fk, status_date)
  SELECT id, id, 2, from_date FROM t_user;

UPDATE t_user SET status_fk=id;
