-- Step 1: Create Genre Table
CREATE TABLE IF NOT EXISTS genre (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS genre_seq START WITH 1 INCREMENT BY 1;

-- Insert sample genres
INSERT INTO genre (name) VALUES
('Action'),
('Comedy'),
('Drama');

-- Step 2: Modify Movie Table

-- Create movie table if not exists with genre_id referencing genre
CREATE TABLE IF NOT EXISTS movie (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255),
    name VARCHAR(255),
    genre_id INTEGER REFERENCES genre(id) -- Add genre_id referencing the genre table
);

-- Alter sequence to increment by 1
--ALTER SEQUENCE movie_id_seq INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS movie_seq START WITH 1 INCREMENT BY 1;

-- Insert some sample movies
INSERT INTO movie (description, name, genre_id) VALUES
('Die Hard', 'An action movie about a cop battling terrorists', 1),
('The Hangover', 'A comedy about a wild bachelor party in Vegas', 2),
('The Shawshank Redemption', 'A drama about a man sentenced to life imprisonment and his friendship with another inmate', 3),
('Mad Max: Fury Road', 'An action movie set in a post-apocalyptic world',1),
('Superbad', 'A coming-of-age comedy about two high school friends', 2);
