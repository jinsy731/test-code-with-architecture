insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (1, 'jinsy731@gmail.com', 'jinsy731', 'Seoul', 'aa-aaa-a-a', 'ACTIVE', 0);

insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (2, 'jinsy732@gmail.com', 'jinsy732', 'Seoul', 'aa-aaa-a-aaa', 'PENDING', 0);

insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values (1, 'hello world', 16785306739558, 0, 1);