CREATE TABLE Person (
    person_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    birthday TIMESTAMP,
    gender VARCHAR(50),
    address VARCHAR(255),
    phone VARCHAR(50),
    cpn VARCHAR(13)
);
insert into UserCredentials (username,password,email,seed) values('admin','$2a$10$FT16Ory4Uuzp/Sm0emhdPe5gWhhBz06JsCaa0l7g47p2QMDRgGhGy','admin','$2a$10$FT16Ory4Uuzp/Sm0emhdPe');
UPDATE UserCredentials
SET password = 'weV7YLyx8YBjz5GkbMc4iCeisvwh6QNn1EJsg4CCsqw=', seed = 'hDuDvIXCK2CcItIOpaGpHg=='
WHERE user_id = 2;
 select s1_0.subscriber_id,c1_0.user_id,c1_0.email,c1_0.password,c1_0.seed,c1_0.username,s1_0.date_of_subscription,p1_0.person_id,
 p1_0.address,p1_0.birthday,p1_0.cpn,p1_0.first_name,p1_0.gender,p1_0.last_name,p1_0.phone,s1_0.unique_code,cr1_0.subscriber_id,
 cr1_1.rental_id,cr1_1.ended_at,r1_0.user_id,r1_0.email,r1_0.password,r1_0.seed,r1_0.username,r2_0.user_id,r2_0.email,
 r2_0.password,r2_0.seed,r2_0.username,cr1_1.started_at 
 from subscriber s1_0 
 left join usercredentials c1_0 on c1_0.user_id=s1_0.credentials_id
 join person p1_0 on p1_0.person_id=s1_0.subscriber_id 
 left join CurrentRentals cr1_0 on s1_0.subscriber_id=cr1_0.subscriber_id
 left join rental cr1_1 on cr1_1.rental_id=cr1_0.rental_id l
 eft join usercredentials r1_0 on r1_0.user_id=cr1_1.rented_by left join usercredentials r2_0 on r2_0.user_id=cr1_1.retrieved_by where s1_0.subscriber_id=?
Hibernate: select c1_0.user_id,c1_0.email,c1_0.password,c1_0.seed,c1_0.username from usercredentials c1_0 where c1_0.user_id=?
insert into UserCredentials (username,password,email,seed) values('ana','$2a$10$OOXvhhyG6pTiOVik3h5.S.Kuk9mQUYfDCOLt3PO4q1wyHfTGLNS9W','ana','$2a$10$OOXvhhyG6pTiOVik3h5.S.');
select * from person
select * from subscriber
select * from UserCredentials;
CREATE TABLE UserCredentials (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    seed VARCHAR(255)
);

CREATE TABLE Admin (
    admin_id BIGINT PRIMARY KEY,
    FOREIGN KEY (admin_id) REFERENCES UserCredentials(user_id) ON DELETE CASCADE
);

CREATE TABLE Librarian (
    librarian_id BIGINT PRIMARY KEY,
    credentials_id BIGINT,
    date_of_employment TIMESTAMP,
    unique_code VARCHAR(50) UNIQUE,
    FOREIGN KEY (librarian_id) REFERENCES Person(person_id) ON DELETE CASCADE,
    FOREIGN KEY (credentials_id) REFERENCES UserCredentials(user_id) ON DELETE SET NULL
);

CREATE TABLE Subscriber (
    subscriber_id BIGINT PRIMARY KEY,
    credentials_id BIGINT,
    date_of_subscription TIMESTAMP,
    FOREIGN KEY (subscriber_id) REFERENCES Person(person_id) ON DELETE CASCADE,
    FOREIGN KEY (credentials_id) REFERENCES UserCredentials(user_id) ON DELETE SET NULL
);

CREATE TABLE BookInformation (
    bookinfo_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    isbn VARCHAR(50),
    publisher VARCHAR(255),
    author VARCHAR(255),
    language VARCHAR(50),
    description TEXT,
    main_genre VARCHAR(100),
    type VARCHAR(100),
    img_url TEXT,
    nr_of_copies INT
);

CREATE TABLE Book (
    book_id BIGSERIAL PRIMARY KEY,
    information_id BIGINT,
    unique_code VARCHAR(60),
    state VARCHAR(25),
    FOREIGN KEY (information_id) REFERENCES BookInformation(bookinfo_id) ON DELETE SET NULL
);

CREATE TABLE BasketItem (
    basket_id BIGSERIAL PRIMARY KEY,
    book_id BIGINT,
    number_of_items INT DEFAULT 0,
    subscriber_of_basket_id BIGINT,
    FOREIGN KEY (book_id) REFERENCES BookInformation(bookinfo_id) ON DELETE SET NULL,
    FOREIGN KEY (subscriber_of_basket_id) REFERENCES Subscriber(subscriber_id) ON DELETE CASCADE
);

CREATE TABLE Rental (
    rental_id BIGSERIAL PRIMARY KEY,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    rented_by BIGINT,
    retrieved_by BIGINT,
    FOREIGN KEY (rented_by) REFERENCES UserCredentials(user_id) ON DELETE SET NULL,
    FOREIGN KEY (retrieved_by) REFERENCES UserCredentials(user_id) ON DELETE SET NULL
);

CREATE TABLE BookRented (
    book_id BIGINT,
    rental_id BIGINT,
    PRIMARY KEY (book_id, rental_id),
    FOREIGN KEY (book_id) REFERENCES Book(book_id) ON DELETE CASCADE,
    FOREIGN KEY (rental_id) REFERENCES Rental(rental_id) ON DELETE CASCADE
);

CREATE TABLE CurrentRentals (
    rental_id BIGINT,
    subscriber_id BIGINT,
    PRIMARY KEY (rental_id, subscriber_id),
    FOREIGN KEY (rental_id) REFERENCES Rental(rental_id) ON DELETE CASCADE,
    FOREIGN KEY (subscriber_id) REFERENCES Subscriber(subscriber_id) ON DELETE CASCADE
);

CREATE TABLE PreviousRentals (
    rental_id BIGINT,
    subscriber_id BIGINT,
    PRIMARY KEY (rental_id, subscriber_id),
    FOREIGN KEY (rental_id) REFERENCES Rental(rental_id) ON DELETE CASCADE,
    FOREIGN KEY (subscriber_id) REFERENCES Subscriber(subscriber_id) ON DELETE CASCADE
);
