-- =========================================================
-- FlySolo - Script Definitivo (Esquema + Seed)
-- Versión: v1.3
-- =========================================================
-- Características del diseño:
--  * Tabla única users con user_type (PASSENGER | PILOT | ADMIN)
--  * Flujo: PASSENGER solicita ser piloto => license_number + pilot_status=PENDIENTE
--           ADMIN aprueba => user_type=PILOT, pilot_status=APROBADO
--  * Histórico de estados del piloto en pilot_status_history
--  * Naves sin columna pilot_id; relación N..M histórica pilot_ship_assignments
--    (PK compuesta: ship_id + pilot_user_id + assigned_at)
--  * Coordenadas espaciales:
--        - Sistemas solares: system_x_u12, system_y_u12, system_z_u12 en unidades de 10^12 km
--        - Planetas (offset dentro del sistema): planet_x_u6, planet_y_u6, planet_z_u6 en unidades de 10^6 km
--        - Posición absoluta planeta (en km) = (system_*_u12 * 1e12) + (planet_*_u6 * 1e6)
--  * Trips sin estado EXPIRADO (usar CANCELADO con cancel_reason='EXPIRADO' si se requiere)
--  * Trips con modo (trip_mode): NORMAL | UNDERCOVER
--      - NORMAL: reglas de compatibilidad con NEUTRAL como puente
--      - UNDERCOVER: solo REBEL/IMPERIAL, misma facción, NEUTRAL no participa
--  * Payments sin currency/updated_at (métodos válidos: TRANSFERENCIA | CREDITO, validar en Java)
--  * Todas las validaciones de dominio y transiciones de estado se realizan en Java (sin CHECK/ENUM).
-- =========================================================

DROP DATABASE IF EXISTS flysolo;
CREATE DATABASE flysolo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE flysolo;

SET NAMES utf8mb4;
SET time_zone = '+00:00';

-- =========================================================
-- Tabla: users
-- =========================================================
CREATE TABLE users (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  faction VARCHAR(20) NOT NULL,            -- IMPERIAL | REBEL | NEUTRAL (validar en Java)
  user_type VARCHAR(15) NOT NULL,          -- PASSENGER | PILOT | ADMIN
  license_number VARCHAR(64) NULL,         -- se completa al solicitar ser piloto
  pilot_status VARCHAR(20) NULL,           -- NULL | PENDIENTE | APROBADO | RECHAZADO
  rating_avg DECIMAL(3,2) NULL,            -- rating histórico (lógica de reseñas fuera de alcance por ahora)
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_users_type (user_type),
  KEY idx_users_pilot_status (pilot_status),
  KEY idx_users_faction (faction)
) ENGINE=InnoDB;

-- =========================================================
-- Histórico de estados de piloto
-- =========================================================
CREATE TABLE pilot_status_history (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT UNSIGNED NOT NULL,
  status VARCHAR(20) NOT NULL,             -- PENDIENTE | APROBADO | RECHAZADO
  reason VARCHAR(255) NULL,
  admin_user_id BIGINT UNSIGNED NULL,
  changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (admin_user_id) REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  KEY idx_psh_user (user_id),
  KEY idx_psh_status (status)
) ENGINE=InnoDB;

-- =========================================================
-- Sistemas Solares (coordenadas escala 10^12 km)
-- =========================================================
CREATE TABLE solar_systems (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  system_x_u12 INT UNSIGNED NOT NULL DEFAULT 0,
  system_y_u12 INT UNSIGNED NOT NULL DEFAULT 0,
  system_z_u12 INT UNSIGNED NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_solar_system_name (name),
  KEY idx_system_coords (system_x_u12, system_y_u12, system_z_u12)
) ENGINE=InnoDB;

-- =========================================================
-- Planetas (offset relativo al sistema escala 10^6 km)
-- =========================================================
CREATE TABLE planets (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  solar_system_id INT UNSIGNED NOT NULL,
  planet_x_u6 INT UNSIGNED NOT NULL DEFAULT 0,
  planet_y_u6 INT UNSIGNED NOT NULL DEFAULT 0,
  planet_z_u6 INT UNSIGNED NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (solar_system_id) REFERENCES solar_systems(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE KEY uq_planet_per_system (solar_system_id, name),
  KEY idx_planet_coords (planet_x_u6, planet_y_u6, planet_z_u6)
) ENGINE=InnoDB;

-- =========================================================
-- Naves
-- =========================================================
CREATE TABLE ships (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  model VARCHAR(100) NOT NULL,
  registration_code VARCHAR(64) NULL UNIQUE,
  capacity_passengers INT UNSIGNED NOT NULL DEFAULT 0,
  capacity_cargo_kg DECIMAL(10,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',  -- PENDIENTE | APROBADO | RECHAZADO
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_ship_status (status)
) ENGINE=InnoDB;

-- =========================================================
-- Asignaciones nave–piloto (N..M histórico)
-- PK compuesta = ship_id + pilot_user_id + assigned_at
-- =========================================================
CREATE TABLE pilot_ship_assignments (
  ship_id BIGINT UNSIGNED NOT NULL,
  pilot_user_id BIGINT UNSIGNED NOT NULL,
  assigned_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6), -- mayor resolución
  unassigned_at DATETIME(6) NULL,
  assigned_by_admin_user_id BIGINT UNSIGNED NULL,
  PRIMARY KEY (ship_id, pilot_user_id, assigned_at),
  FOREIGN KEY (ship_id) REFERENCES ships(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (pilot_user_id) REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (assigned_by_admin_user_id) REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  KEY idx_psa_pilot (pilot_user_id),
  KEY idx_psa_ship (ship_id)
) ENGINE=InnoDB;

-- =========================================================
-- Viajes
-- =========================================================
CREATE TABLE trips (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  passenger_user_id BIGINT UNSIGNED NOT NULL,
  pilot_user_id BIGINT UNSIGNED NULL,
  origin_planet_id INT UNSIGNED NOT NULL,
  destination_planet_id INT UNSIGNED NOT NULL,
  trip_mode VARCHAR(20) NOT NULL DEFAULT 'NORMAL',          -- NORMAL | UNDERCOVER (validar reglas en Java)
  type VARCHAR(20) NOT NULL DEFAULT 'INMEDIATO',            -- INMEDIATO | PROGRAMADO
  status VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',          -- PENDIENTE | CONFIRMADO | EN_CURSO | COMPLETADO | CANCELADO
  price DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  requested_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  started_at DATETIME NULL,
  completed_at DATETIME NULL,
  canceled_at DATETIME NULL,
  cancel_reason VARCHAR(255) NULL,
  notes TEXT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (passenger_user_id) REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (pilot_user_id) REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  FOREIGN KEY (origin_planet_id) REFERENCES planets(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (destination_planet_id) REFERENCES planets(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  KEY idx_trip_passenger (passenger_user_id),
  KEY idx_trip_pilot (pilot_user_id),
  KEY idx_trip_status (status),
  KEY idx_trip_type (type),
  KEY idx_trip_mode (trip_mode)
) ENGINE=InnoDB;

-- =========================================================
-- Pagos
-- =========================================================
CREATE TABLE payments (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  trip_id BIGINT UNSIGNED NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',     -- PENDIENTE | PAGADO | FALLIDO | REEMBOLSADO
  method VARCHAR(20) NOT NULL,                         -- TRANSFERENCIA | CREDITO
  paid_at DATETIME NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (trip_id) REFERENCES trips(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE KEY uq_payment_trip (trip_id),
  KEY idx_payment_status (status),
  KEY idx_payment_method (method)
) ENGINE=InnoDB;

-- =========================================================
-- SEED DATA
-- =========================================================

-- Sistemas (posiciones ficticias)
INSERT INTO solar_systems (name, system_x_u12, system_y_u12, system_z_u12) VALUES
('Corellian Sector', 120, 45, 8),
('Tatoo System',     300, 52, 9);

-- Planetas (offset relativo en millones de km)
INSERT INTO planets (name, solar_system_id, planet_x_u6, planet_y_u6, planet_z_u6) VALUES
('Corellia', 1, 15, 3, 1),
('Drall',    1, 22, 6, 2),
('Tatooine', 2, 5,  2, 1),
('Geonosis', 2, 40, 15, 4);

-- Usuarios (admins y pasajeros iniciales)
INSERT INTO users (name, email, faction, user_type, license_number, pilot_status, rating_avg) VALUES
('Mon Mothma',       'mothma@rebel.example',   'REBEL',   'ADMIN',     NULL,         NULL,        NULL),
('Imperial Officer', 'officer@imperial.example','IMPERIAL','ADMIN',     NULL,         NULL,        NULL),
('Leia Organa',      'leia@rebel.example',     'REBEL',   'PASSENGER', NULL,         NULL,        4.80),
('Han Solo',         'han@neutral.example',    'NEUTRAL', 'PASSENGER', NULL,         NULL,        4.10),
('Neutral Trader',   'trader@neutral.example', 'NEUTRAL', 'PASSENGER', NULL,         NULL,        3.90);

-- Han solicita ser piloto
UPDATE users
SET license_number = 'LIC-HAN-001',
    pilot_status = 'PENDIENTE'
WHERE email = 'han@neutral.example';

INSERT INTO pilot_status_history (user_id, status, reason, admin_user_id, changed_at)
SELECT id, 'PENDIENTE', NULL, NULL, NOW()
FROM users WHERE email = 'han@neutral.example';

-- Aprobación de Han (Mon Mothma)
UPDATE users
SET user_type = 'PILOT',
    pilot_status = 'APROBADO'
WHERE email = 'han@neutral.example';

INSERT INTO pilot_status_history (user_id, status, reason, admin_user_id)
SELECT id, 'APROBADO', 'Verificación completa',
       (SELECT id FROM users WHERE user_type='ADMIN' AND name='Mon Mothma')
FROM users WHERE email = 'han@neutral.example';

-- Nave y asignación (PK compuesta: ship_id, pilot_user_id, assigned_at)
INSERT INTO ships (model, registration_code, capacity_passengers, capacity_cargo_kg, status)
VALUES ('YT-1300', 'REG-FALCON', 6, 5000.00, 'APROBADO');

INSERT INTO pilot_ship_assignments (ship_id, pilot_user_id, assigned_by_admin_user_id)
VALUES (
  (SELECT id FROM ships WHERE registration_code='REG-FALCON'),
  (SELECT id FROM users WHERE email='han@neutral.example'),
  (SELECT id FROM users WHERE user_type='ADMIN' AND name='Mon Mothma')
);

-- Viaje NORMAL: Leia (REBEL) -> Tatooine, sin piloto asignado inicialmente.
-- Compatibilidad: pasajero REBEL puede luego ser atendido por piloto NEUTRAL o REBEL
INSERT INTO trips (passenger_user_id, pilot_user_id, origin_planet_id, destination_planet_id, trip_mode, type, status, price, notes)
VALUES (
  (SELECT id FROM users WHERE email='leia@rebel.example'),
  NULL,
  (SELECT id FROM planets WHERE name='Corellia'),
  (SELECT id FROM planets WHERE name='Tatooine'),
  'NORMAL',
  'INMEDIATO',
  'PENDIENTE',
  1200.00,
  'Traslado urgente a Tatooine'
);

-- Asignación de piloto (Han, NEUTRAL) y confirmación
-- Permitido porque en modo NORMAL: REBEL pasajero ↔ NEUTRAL piloto es válido
UPDATE trips
SET pilot_user_id = (SELECT id FROM users WHERE email='han@neutral.example'),
    status = 'CONFIRMADO'
WHERE notes = 'Traslado urgente a Tatooine';

-- Iniciar y completar viaje
UPDATE trips SET status='EN_CURSO', started_at=NOW() WHERE status='CONFIRMADO';
UPDATE trips SET status='COMPLETADO', completed_at=NOW() WHERE status='EN_CURSO';

-- Registrar pago y marcarlo como pagado
INSERT INTO payments (trip_id, amount, status, method)
SELECT id, price, 'PENDIENTE', 'TRANSFERENCIA' FROM trips WHERE status='COMPLETADO';

UPDATE payments SET status='PAGADO', paid_at=NOW() WHERE status='PENDIENTE';

-- =========================================================
-- Consultas de verificación (comentar/descomentar según necesidad)
-- =========================================================
-- SELECT * FROM users;
-- SELECT * FROM pilot_status_history ORDER BY changed_at;
-- SELECT * FROM ships;
-- SELECT * FROM pilot_ship_assignments;
-- SELECT * FROM trips;
-- SELECT * FROM payments;
-- Pilotos aprobados:
-- SELECT id, name, license_number FROM users WHERE user_type='PILOT' AND pilot_status='APROBADO';
-- Viajes completados pendientes de pago (debería estar vacío porque se pagó):
-- SELECT t.id, t.price, COALESCE(p.status,'SIN_PAGO') AS payment_status
-- FROM trips t LEFT JOIN payments p ON p.trip_id = t.id
-- WHERE t.passenger_user_id = (SELECT id FROM users WHERE email='leia@rebel.example')
--   AND t.status='COMPLETADO'
--   AND (p.id IS NULL OR p.status='PENDIENTE');
-- Coordenadas absolutas de Corellia (km):
-- SELECT ss.name AS Solar_system,
--        ss.system_x_u12*1e12 + p.planet_x_u6*1e6 AS abs_x_km,
--        ss.system_y_u12*1e12 + p.planet_y_u6*1e6 AS abs_y_km,
--        ss.system_z_u12*1e12 + p.planet_z_u6*1e6 AS abs_z_km
-- FROM planets p JOIN solar_systems ss ON ss.id = p.solar_system_id
-- WHERE p.name='Corellia';

-- =========================================================
-- FIN SCRIPT
-- =========================================================