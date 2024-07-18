Article REST Api
====================

It is simple api using framework Javalin to run rest api and postgres DB to store data.\
the framework Javalin looks less popular than spring like but i found that some years ago at some course,
and wanted to store something with that :)

to see data connect to the PostgreSQL database eg. with pgAdmin\
(db is 20mb and stand on https://api.elephantsql.com)\
host name: dumbo.db.elephantsql.com
password:  PHm0jpvwUdwMtElhSwmHCA85Ucl9G6l8
username:  xvwxelvu

## How to create database and some sample data ##

DROP table article;

CREATE TABLE article(
    id SERIAL PRIMARY KEY,
    contents varchar,
    publication_date date,
    name varchar(255),
    author varchar(255),
    recording_date timestamp default CURRENT_TIMESTAMP
); 

INSERT INTO article(contents,publication_date,name,author)VALUES('The Hobbit or There and Back Again','1937-09-21','Hobbit','J.R.R. Tolkiena');
INSERT INTO article(contents,publication_date,name,author)VALUES('Lord of the Rings. The Fellowship of the Ring - How It All Began?','1954-07-29','Lord of the Rings','J.R.R. Tolkiena');
INSERT INTO article(contents,publication_date,name,author)VALUES('Shrek! is a humorous fantasy picture book published in 1990 by American book writer and cartoonist William Steig, about ...','1990-10-17','Shrek','William Steig');
INSERT INTO article(contents,publication_date,name,author)VALUES('Pan Tadeusz, czyli ostatni zajazd na Litwie','1834-06-08','Pan Tadeusz','Adam Mickiewicz');
INSERT INTO article(contents,publication_date,name,author)VALUES('Lalka – powieść społeczno-obyczajowa Bolesława Prusa publikowana w odcinkach w latach 1887–1889 w dzienniku „Kurier Codzienny”','1883-09-29','Lalka','Bolesław Prus');

