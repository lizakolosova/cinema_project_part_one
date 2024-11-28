CREATE TABLE cinema (
                        cinema_id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(10) NOT NULL unique,
                        address VARCHAR(30) NOT NULL,
                        capacity INT NOT NULL,
                        image VARCHAR(15) NOT NULL
);

CREATE TABLE movie (
                       movie_id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(30) NOT NULL UNIQUE,
                       release_date DATE NOT NULL,
                       rating DECIMAL(3, 1) CHECK (rating >= 0 AND rating <= 10),
                       genre VARCHAR(15) NOT NULL,
                       image VARCHAR(15) NOT NULL
);

CREATE TABLE cinema_screen (
                               screen_id INT AUTO_INCREMENT PRIMARY KEY,
                               screen_number INT NOT NULL,
                               cinema_id_fk INT NOT NULL,
                               screenType VARCHAR(15) NOT NULL,
                               size INT NOT NULL,
                               movie_id_fk INT NOT NULL,
                               FOREIGN KEY (cinema_id_fk) REFERENCES cinema(cinema_id),
                               FOREIGN KEY (movie_id_fk) REFERENCES movie(movie_id)
);
CREATE TABLE cinema_screen_movie (
                                     screen_id INT NOT NULL,
                                     movie_id INT NOT NULL,
                                     PRIMARY KEY (screen_id, movie_id),
                                     FOREIGN KEY (screen_id) REFERENCES cinema_screen(screen_id),
                                     FOREIGN KEY (movie_id) REFERENCES movie(movie_id)
);
CREATE TABLE cinema_movie (
                              cinema_id INT NOT NULL,
                              movie_id INT NOT NULL,
                              PRIMARY KEY (cinema_id, movie_id),
                              FOREIGN KEY (cinema_id) REFERENCES cinema(cinema_id),
                              FOREIGN KEY (movie_id) REFERENCES movie(movie_id)
);
