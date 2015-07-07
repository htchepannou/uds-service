-- status codes
INSERT INTO t_user_status_code(id, name, active, default_status) values(1, 'new', false, true);
INSERT INTO t_user_status_code(id, name, active, default_status) values(2, 'active', true, false);
INSERT INTO t_user_status_code(id, name, active, default_status) values(3, 'suspended', false, false);


-- findById
insert into t_user (id, party_fk, login, password, from_date, to_date) values (
  101, 100, 'ray.sponsible', 'secret', '1973-12-27 10:30:45', null);

insert into t_user_status(id, user_fk, status_code_fk, status_date, comment) values(
  101, 101, 1, '1973-12-27 10:30:45', 'Initial'
);
update t_user set status_fk=101 where id=101;


-- findById_deleted
insert into t_user (id, party_fk, login, password, from_date, to_date, deleted) values (
  301, 300, 'ray301.sponsible', 'secret', '1973-12-27 10:30:45', null, 1);

-- create_accountAlreadyExist
insert into t_user (id, party_fk, login, password, from_date, to_date) values (
  400, 400, 'ray400.sponsible', 'secret', '1973-12-27 10:30:45', null);

-- updateLogin
insert into t_user (id, party_fk, login, password, from_date, to_date) values (
  600, 600, 'ray600.sponsible', 'secret', '1973-12-27 10:30:45', null);

insert into t_user_status(id, user_fk, status_code_fk, status_date, comment) values(
  600, 600, 2, '1973-12-27 10:30:45', 'Activated'
);
update t_user set status_fk=600 where id=600;

-- updatePassword
insert into t_user (id, party_fk, login, password, from_date, to_date) values (
  700, 700, 'ray700.sponsible', 'secret', '1973-12-27 10:30:45', null);

-- delete
insert into t_user (id, party_fk, login, password, from_date, to_date) values (
  1000, 1000, 'ray1000.sponsible', 'secret', '1973-12-27 10:30:45', null);

-- setStatus
insert into t_user (id, party_fk, login, password, from_date, to_date) values (
  1100, 1100, 'ray1100.sponsible', 'secret', '1973-12-27 10:30:45', null);

insert into t_user_status(id, user_fk, status_code_fk, status_date, comment) values(
  1100, 1100, 1, '1973-12-27 10:30:45', 'New'
);
update t_user set status_fk=1100 where id=1100;
