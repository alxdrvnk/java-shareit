CREATE TABLE IF NOT EXISTS users (
                                     id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     name VARCHAR NOT NULL,
                                     email VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests (
                                     id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     description VARCHAR NOT NULL,
                                     requestor_id INTEGER,
                                     CONSTRAINT fk_requestor_id FOREIGN KEY (requestor_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
                                     id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     name VARCHAR NOT NULL,
                                     description VARCHAR NOT NULL,
                                     is_available BOOLEAN NOT NULL,
                                     owner_id INTEGER,
                                     request_id INTEGER,
                                     CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
                                     CONSTRAINT fk_request_id FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
                                     id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     start_date DATETIME NOT NULL,
                                     end_date DATETIME NOT NULL,
                                     status VARCHAR NOT NULL,
                                     item_id INTEGER,
                                     booker_id INTEGER,
                                     CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
                                     CONSTRAINT fk_booker_id FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
                                     id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     text VARCHAR NOT NULL,
                                     item_id INTEGER,
                                     author_id INTEGER,
                                     CONSTRAINT fk_comments_item_id FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
                                     CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);
