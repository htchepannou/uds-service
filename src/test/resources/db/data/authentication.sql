-- status codes
INSERT INTO t_user_status_code(id, name, active, default_status) values(1, 'new', false, true);
INSERT INTO t_user_status_code(id, name, active, default_status) values(2, 'active', true, false);
INSERT INTO t_user_status_code(id, name, active, default_status) values(3, 'suspended', false, false);


-- Role
insert into t_role(id, name) values(100, 'admin');

-- domain
insert into t_domain(id, name, description) values(100, 'admin.moralab.com', 'Admin site');


-- login
insert into t_user (id, party_fk, login, password, from_date, to_date) values (
  100, 100, 'ray.sponsible', 'SCHublZJUN03uJ576K868m63mM0Sa/83', '1973-12-27 10:30:45', null);

insert into t_domain_user(id, domain_fk, user_fk, role_fk, from_date) values(
  100, 100, 100, 100, '2014-12-27 10:30:45');

insert into t_user_status(id, user_fk, status_code_fk, status_date, comment) values(
  100, 100, 2, '1973-12-27 10:30:45', 'Initial'
);
update t_user set status_fk=100 where id=100;

-- findById
insert into t_access_token(id, user_fk, domain_fk, from_date, expiry_date, expired) values (
    100, 100, 100, '1973-12-27 10:30:45', '2020-12-27 10:30:45', 0);

-- findById_expired
insert into t_access_token(id, user_fk, domain_fk, from_date, expiry_date, expired) values (
  200, 100, 100, '2015-01-30 10:30:45', '2015-01-01 10:30:45', 0);

-- logout
insert into t_access_token(id, user_fk, domain_fk, from_date, expiry_date, expired) values (
  300, 100, 100, '2015-01-30 10:30:45', '2020-01-01 10:30:45', 0);

-- logout_expired
insert into t_access_token(id, user_fk, domain_fk, from_date, expiry_date, expired) values (
  400, 100, 100, '2015-01-30 10:30:45', '2015-01-01 10:30:45', 0);

-- login_user_deleted
insert into t_user (id, party_fk, login, password, from_date, to_date, deleted) values (
  500, 100, 'ray500.sponsible', 'SCHublZJUN03uJ576K868m63mM0Sa/83', '1973-12-27 10:30:45', null, 1);
insert into t_domain_user(id, domain_fk, user_fk, role_fk, from_date) values(
  500, 100, 500, 100, '2014-12-27 10:30:45');

-- login_access_denied
insert into t_user (id, party_fk, login, password, from_date, to_date) values (
  600, 100, 'ray600.sponsible', 'SCHublZJUN03uJ576K868m63mM0Sa/83', '1973-12-27 10:30:45', null);

insert into t_user_status(id, user_fk, status_code_fk, status_date, comment) values(
  600, 100, 2, '1973-12-27 10:30:45', 'Initial'
);
update t_user set status_fk=600 where id=600;


-- login_inactive
insert into t_user (id, party_fk, login, password, from_date, to_date) values (
  700, 100, 'ray700.sponsible', 'SCHublZJUN03uJ576K868m63mM0Sa/83', '1973-12-27 10:30:45', null);

insert into t_user_status(id, user_fk, status_code_fk, status_date, comment) values(
  700, 100, 1, '1973-12-27 10:30:45', 'Initial'
);
update t_user set status_fk=700 where id=800;

insert into t_domain_user(id, domain_fk, user_fk, role_fk, from_date) values(
  700, 100, 700, 100, '2014-12-27 10:30:45');
