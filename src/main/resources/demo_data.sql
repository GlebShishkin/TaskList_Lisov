insert into users (name, username, password)
values
    ('John Doe', 'johndoe@gmail.com', '$2a$10$l1IqoiqyEFOqrxWUA3ESzeJlVstYxCAWeE1muwT3aObhwuutoZrAi'),
    ('Mike Smith', 'mikesmith@gmail.com', '$2a$10$Ep3tnHMsd78PHQoyyHGTh.CXk9GsiuqNcSBg4RgeqrFf5VoDtDW4C');

insert into tasks (title, description, status, expiration_date)
values
    ('Task1', null, 'TODO', '2023-01-29 12:00:00'),
    ('Task2', 'Math', 'IN_PROGRESS', '2023-01-31 12:00:00'),
    ('Task3', null, 'DONE', '2023-02-01 12:00:00'),
    ('Task4', 'aSK ABOUT MEETING', 'TODO', '2023-02-01 00:00:00');

 insert into users_tasks (task_id, user_id)
 values
    (1, 1),
    (2, 1),
    (3, 1),
    (4, 2);


 insert into users_roles (user_id, role)
 values
    (1, 'ROLE_ADMIN'),
    (1, 'ROLE_USER'),
    (2, 'ROLE_USER');