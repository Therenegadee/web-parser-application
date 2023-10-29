insert into roles (id,name)
values
    (DEFAULT,'ROLE_ADMIN'),
    (DEFAULT,'ROLE_USER')
ON CONFLICT DO NOTHING;
