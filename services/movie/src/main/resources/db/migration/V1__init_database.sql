-- Step 1: Create the Genre table, a sequence, and insert some sample rows
CREATE TABLE IF NOT EXISTS genre (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS genre_seq START WITH 1 INCREMENT BY 1;

INSERT INTO genre (name) VALUES
('Action'),
('Comedy'),
('Drama');

-- Align the genre_seq with the highest existing ID, to prevent duplicate key errors when inserting new rows.
SELECT setval('genre_seq', COALESCE((SELECT MAX(id) FROM genre), 0) + 1, false);

-- Step 2: Create the Movie table, a sequence, and insert some sample rows
CREATE TABLE IF NOT EXISTS movie (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255),
    name VARCHAR(255),
    genre_id INTEGER REFERENCES genre(id) -- Add genre_id referencing the genre table
);

CREATE SEQUENCE IF NOT EXISTS movie_seq START WITH 1;
SELECT setval('movie_seq', COALESCE((SELECT MAX(id) FROM movie), 0) + 1, false);

INSERT INTO movie (description, name, genre_id) VALUES
('Die Hard', 'An action movie about a cop battling terrorists', 1),
('The Hangover', 'A comedy about a wild bachelor party in Vegas', 2),
('The Shawshank Redemption', 'A drama about a man sentenced to life imprisonment and his friendship with another inmate', 3),
('Mad Max: Fury Road', 'An action movie set in a post-apocalyptic world',1),
('Superbad', 'A coming-of-age comedy about two high school friends', 2);

-- Align the movie_seq with the highest existing ID, to prevent duplicate key errors when inserting new rows.
SELECT setval('movie_seq', COALESCE((SELECT MAX(id) FROM movie), 0) + 1, false);