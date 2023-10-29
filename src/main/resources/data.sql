insert into roles (id,name)
values
    (DEFAULT,'ROLE_ADMIN'),
    (DEFAULT,'ROLE_USER')
ON CONFLICT DO NOTHING;

insert into users (id,username,email,password,activation_status,telegram_id)
values
    (DEFAULT,'test','test@mail.ru','$2a$10$0bOs7XlXyG6vEmxVfyUURu4b20Hb7byb2rjmmWG9cm2WeYkFAvNWy','VERIFIED','telegramId')
ON CONFLICT DO NOTHING;

insert into user_roles (user_id, role_id)
    select u.id, r.id from users u, roles r
    where u.username = 'test' and r.name in ('ROLE_USER')
ON CONFLICT DO NOTHING;
