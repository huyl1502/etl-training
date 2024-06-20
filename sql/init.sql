CREATE TABLE media_settings (
  platform_id varchar(3) NOT NULL,
  imp_col varchar(45) NOT NULL, -- imp column name in input_file
  click_col varchar(45) NOT NULL, -- click column name in input_file
  cost_col varchar(45) NOT NULL, -- cost column name in input_file
  cv_col varchar(45) NOT NULL, -- cv column name in input_file
  report_date_col varchar(45) DEFAULT NULL, -- report_date column in input_file
  PRIMARY KEY (platform_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE platforms (
  id varchar(3) NOT NULL,
  name varchar(45) NOT NULL,
  currency_rate double NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `name_UNIQUE` (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- No change data here
INSERT INTO platforms (id, name, currency_rate) VALUES ('Kan', 'Kanade', 1.5), ('Pin', 'Pine', 1);
INSERT INTO 'media_settings'('platform_id', 'imp_col', 'click_col', 'cost_col', 'cv_col', 'report_date_col')
VALUES ('Kan', 'Imp', 'Click', 'Cost', 'Cv', 'Date'), ('Pin', 'Impressions', 'Outbound clicks', 'Spend in account currency', 'Click-through conversions (Checkout)', 'Date');

CREATE TABLE action_log (
  id int AUTO_INCREMENT NOT NULL,
  file_name varchar(512) NOT NULL,
  etl_result varchar(512) NOT NULL,
  execution_time varchar(512) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `id_UNIQUE` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;