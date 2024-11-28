INSERT INTO cinema (name, address, capacity, image)
VALUES
    ('Cinema 1', '127 Kattenstraat', 200, 'cinema1.jpg'),
    ('Cinema 2', '365 Pothoekstraat', 150, 'cinema2.jpg'),
    ('Cinema 3', '398 Predikerinnenstraat', 300, 'cinema3.jpg'),
    ( 'Cinema 4', '741 Bredabaan', 220, 'cinema4.jpg');

INSERT INTO movie (title, release_date, rating, genre, image)
VALUES
    ('Avengers: Endgame', '2019-04-26', 8.5, 'ACTION', 'movie1.jpg'),
    ('Toy Story 4', '2019-06-21', 7.8, 'ANIMATION', 'movie2.jpg'),
    ('The Lion King', '2019-07-19', 6.8, 'ADVENTURE', 'movie3.jpg'),
    ('Joker', '2019-10-04', 8.4, 'DRAMA', 'movie4.jpg');

INSERT INTO cinema_screen (screen_number, cinema_id_fk, screenType, size, movie_id_fk)
VALUES
    ( 1, 1, 'IMAX', 100, 1),
    ( 2, 2, 'Regular', 50, 2),
    ( 1, 3, 'Regular', 75, 3),
    ( 2, 4, 'Small', 50, 4);

INSERT INTO cinema_screen_movie (screen_id, movie_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (1, 2),
    (2, 1),
    (3, 4),
    (4, 3);
INSERT INTO cinema_movie (cinema_id, movie_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 2),
    (2, 1),
    (3, 3),
    (3, 4),
    (4, 4),
    (4, 3);
