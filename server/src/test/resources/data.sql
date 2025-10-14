MERGE INTO users (id, email, name) VALUES
(1, 'user1@example.com', 'User 1'),
(2, 'user2@example.com', 'User 2'),
(3, 'user3@example.com', 'User 3');

ALTER TABLE users ALTER COLUMN id RESTART WITH 4;
