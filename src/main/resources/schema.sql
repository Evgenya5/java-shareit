CREATE TABLE IF NOT EXISTS users (
              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
              email varchar(100) NOT NULL,
              name varchar(200),
              CONSTRAINT users_pk PRIMARY KEY (id),
              CONSTRAINT email_uk UNIQUE (email)
          );
CREATE TABLE IF NOT EXISTS items (
              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
              description varchar(210),
              name varchar(100) NOT NULL,
              is_available BOOLEAN DEFAULT TRUE,
              owner_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
              request_id varchar(200),
              CONSTRAINT items_pk PRIMARY KEY (id)
          );
CREATE TABLE IF NOT EXISTS comments (
              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
              text varchar(200),
              created date,
              author_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
              item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
              CONSTRAINT comments_pk PRIMARY KEY (id)
          );
CREATE TABLE IF NOT EXISTS bookings (
              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
              start_date TIMESTAMP WITHOUT TIME ZONE,
              end_date TIMESTAMP WITHOUT TIME ZONE,
              description varchar(210),
              status varchar(100) NOT NULL,
              is_available BOOLEAN DEFAULT TRUE,
              item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
              booker_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
              CONSTRAINT bookings_pk PRIMARY KEY (id)
          );