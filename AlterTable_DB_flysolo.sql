USE flysolo;

ALTER TABLE users
  ADD COLUMN password VARCHAR(128) NULL AFTER email;

UPDATE users SET password = 'admin123'   WHERE email = 'mothma@rebel.example';
UPDATE users SET password = 'admin123'   WHERE email = 'officer@imperial.example';
UPDATE users SET password = 'leia123usersusers'    WHERE email = 'leia@rebel.example';
UPDATE users SET password = 'han123'     WHERE email = 'han@neutral.example';
UPDATE users SET password = 'trader123'  WHERE email = 'trader@neutral.example';

ALTER TABLE users
  MODIFY password VARCHAR(128) NOT NULL;
