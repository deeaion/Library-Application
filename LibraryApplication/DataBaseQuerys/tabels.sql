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

insert into UserCredentials (username,password,email,seed) values('ana','$2a$10$OOXvhhyG6pTiOVik3h5.S.Kuk9mQUYfDCOLt3PO4q1wyHfTGLNS9W','ana','$2a$10$OOXvhhyG6pTiOVik3h5.S.');

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
