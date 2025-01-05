Name: Liza Kolosova

Explanation of domain and relations:
My database looks like this: cinema - cinemaScreen - movie and movie is also connected to cinema so I could have easier queries. Cinema - cinemascreen is one to many, so one cinema can have multiple screens. Cinemascreen - movie is many to many and movie - cinema is many to many.

Explanation of profiles:
Collections: I don't use any database here. It's like an array where I add more elements (cinemas or movies).

Database: -

jdbc: It's H2 database, where I use schema.sql and data.sql. 

Database:

url=jdbc:h2:mem:cinemadb

username=liza

password=password

dev:  It's H2 database, where I don't use schema.sql and data.sql anymore. I also use entity manager here.

Database: 

url=jdbc:h2:mem:cinemadb

username=liza

password=password

prod: It's postgres database, I use entity manager here(JPA).

Database:

url=jdbc:postgresql://localhost:5432/postgres

username=postgres

password=Student_1234


JPA:Here I use JPA interface with postgres database.

Database:

url=jdbc:postgresql://localhost:5432/postgres

username=postgres

password=Student_1234

Any extra information needed to be able to run the project smoothly: Just use /home and then you will have navbar to navigave through pages.

From what I see, I did everything and I don't see any errors so far.
