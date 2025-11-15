USE flysolo;

-- 1) Agregar columna 'password' como NULL temporalmente
ALTER TABLE users
  ADD COLUMN password VARCHAR(128) NULL AFTER email;

-- 2) Completar contraseñas de ejemplo (en texto plano, solo para pruebas)
--    Ajusta los valores si lo deseas.
UPDATE users SET password = 'admin123'   WHERE email = 'mothma@rebel.example';
UPDATE users SET password = 'admin123'   WHERE email = 'officer@imperial.example';
UPDATE users SET password = 'leia123usersusers'    WHERE email = 'leia@rebel.example';
UPDATE users SET password = 'han123'     WHERE email = 'han@neutral.example';
UPDATE users SET password = 'trader123'  WHERE email = 'trader@neutral.example';

-- 3) Hacer la columna NOT NULL una vez que todos tengan valor
ALTER TABLE users
  MODIFY password VARCHAR(128) NOT NULL;