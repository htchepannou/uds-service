-- findById
insert into t_domain(id, name, description, deleted) values(100, 'admin.moralab.com', 'Admin site', 0);
insert into t_domain(id, name, description, deleted) values(101, 'www.moralab.com', 'Main site', 0);
insert into t_domain(id, name, description, deleted) values(102, 'api.moralab.com', 'API site', 0);
insert into t_domain(id, name, description, deleted) values(103, 'deleted.moralab.com', 'API site', 1);

-- update
insert into t_domain(id, name, description, deleted) values(200, 'update.me', null, 0);
insert into t_domain(id, name, description, deleted) values(201, 'update.me-deleted', null, 1);

-- delete
insert into t_domain(id, name, description, deleted) values(300, 'delete.me', null, 0);

-- -- roles
-- insert into t_role(id, name) values(400, 'admin400');
-- insert into t_role(id, name) values(401, 'member400');
--
-- insert into t_user (id, party_fk, login, password, from_date, to_date, status) values (400, 100, 'ray.sponsible400', 'secret', '1973-12-27 10:30:45', null, 'A');
-- insert into t_domain_user(id, domain_fk, user_fk, role_fk, from_date) values(400, 100, 400, 400, '2014-12-27 10:30:45');
-- insert into t_domain_user(id, domain_fk, user_fk, role_fk, from_date) values(401, 100, 400, 401, '2014-12-27 10:30:45');
--
-- -- permissions
-- insert into t_permission(id, name) values(500, 'perm500');
-- insert into t_permission(id, name) values(501, 'perm501');
-- insert into t_permission(id, name) values(502, 'perm502');
-- insert into t_permission(id, name) values(503, 'perm503');
--
-- insert into t_role(id, name) values(500, 'admin500');
-- insert into t_role(id, name) values(501, 'member500');
--
-- insert into t_role_permission(role_fk, permission_fk) values(500, 500);
-- insert into t_role_permission(role_fk, permission_fk) values(500, 501);
--
-- insert into t_role_permission(role_fk, permission_fk) values(501, 501);
-- insert into t_role_permission(role_fk, permission_fk) values(501, 502);
--
-- insert into t_user (id, party_fk, login, password, from_date, to_date, status) values (500, 100, 'ray.sponsible500', 'secret', '1973-12-27 10:30:45', null, 'A');
-- insert into t_domain_user(id, domain_fk, user_fk, role_fk, from_date) values(500, 100, 500, 500, '2014-12-27 10:30:45');
-- insert into t_domain_user(id, domain_fk, user_fk, role_fk, from_date) values(501, 100, 500, 501, '2014-12-27 10:30:45');
--
--
-- -- grant
-- insert into t_role(id, name) values(600, 'admin600');
-- insert into t_role(id, name) values(601, 'member600');
--
-- insert into t_user (id, party_fk, login, password, from_date, to_date, status) values (600, 100, 'ray.sponsible600', 'secret', '1973-12-27 10:30:45', null, 'A');
-- insert into t_domain_user(id, domain_fk, user_fk, role_fk, from_date) values(600, 100, 600, 600, '2014-12-27 10:30:45');
