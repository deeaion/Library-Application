--inserting the data for books
INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Deadpool: Volume 1', 
    '9780785185687', 
    'Marvel', 
    'Gerry Duggan, Brian Posehn', 
    'English', 
    'Deadpool’s back and better than ever! The Merc with a Mouth gets a fresh start in this Marvel NOW! volume by writers Gerry Duggan and Brian Posehn.', 
    'Superhero', 
    'COMIC', 
    '/views/images/bookCover/Comic/DeadPool_vol1.jpg',
    5
);
INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Bleach: Volume 1', 
    '9781591164418', 
    'VIZ Media LLC', 
    'Tite Kubo', 
    'English', 
    'Ichigo Kurosaki has always been able to see ghosts, but this ability doesn\t change his life nearly as much as his close encounter with Rukia Kuchiki, a Soul Reaper and member of the mysterious Soul Society.', 
    'FANTASY', 
    'MANGA', 
    'views/images/bookCover/Manga/Bleach_vol1.jpeg', 
    10
);


INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Blue Period: Volume 1', 
    '9781646511129', 
    'Kodansha Comics', 
    'Tsubasa Yamaguchi', 
    'English', 
    'Yatora Yaguchi discovers his passion for painting and pursues a career in art.', 
    'SLICEOFLIFE', 
    'MANGA', 
    'views/images/bookCover/Manga/BluePeriod_vol1.jpg', 
    7
);

INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Bungo Stray Dogs: Volume 22', 
    '9784041082342', 
    'Kadokawa Shoten', 
    'Kafka Asagiri, Sango Harukawa', 
    'Japanese', 
    'The Armed Detective Agency faces off against the Port Mafia.', 
    'SUPERNATURAL', 
    'MANGA', 
    '/views/images/bookCover/Manga/BungoStrayDogs_vol22.jpg', 
    5
);

INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Horimiya: Volume 1', 
    '9780316342032', 
    'Yen Press', 
    'HERO, Daisuke Hagiwara', 
    'English', 
    'A high school romance between Hori and Miyamura.', 
    'ROMANCE', 
    'MANGA', 
    '/views/images/bookCover/Manga/Horimiya_vol1.jpeg', 
    8
);

INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Kimi ni Todoke: Volume 9', 
    '9781421527882', 
    'VIZ Media LLC', 
    'Karuho Shiina', 
    'English', 
    'Sawako and Kazehaya’s relationship blossoms.', 
    'ROMANCE', 
    'MANGA', 
    '/views/images/bookCover/Manga/KimiNiTodoke_vol9.jpg', 
    10
);

INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Naruto: Volume 72', 
    '9781421579744', 
    'VIZ Media LLC', 
    'Masashi Kishimoto', 
    'English', 
    'Naruto and Sasuke\s final battle.', 
    'ADVENTURE', 
    'MANGA', 
    'views/images/bookCover/Manga/Naruto_vol72.jpg', 
    12
);

-- Insert Book Information for Omniscient Reader’s Viewpoint: Volume 1
INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Omniscient Reader’s Viewpoint: Volume 1', 
    '9781949980802', 
    'Redice Studio', 
    'singNsong, Sleepy-C', 
    'English', 
    'Kim Dokjas ordinary life is turned upside down when his favorite web novel becomes a reality.', 
    'FANTASY', 
    'MANHWA', 
    'views/images/bookCover/Manhwa/OmniscientReadersViewpoint_vol1.jpg', 
    5
) RETURNING bookinfo_id;

-- Insert Book Information for Solo Leveling: Volume 1
INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Solo Leveling: Volume 1', 
    '9781975319274', 
    'Yen Press', 
    'Chugong, Dubu (Redice Studio)', 
    'English', 
    'Sung Jin-Woo, an E rank hunter, becomes the worlds strongest after a near-death experience.', 
    'FANTASY', 
    'MANHWA', 
    'views/images/bookCover/Manhwa/SoloLeveling_vol1.jpg', 
    7
) RETURNING bookinfo_id;

-- Insert Book Information for Unordinary: Volume 1
INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Unordinary: Volume 1', 
    '9781427867208', 
    'WEBTOON Unscrolled', 
    'uru-chan', 
    'English', 
    'John Doe hides his true powers in a world where abilities define social status.', 
    'SUPERHERO', 
    'MANHWA', 
    'views/images/bookCover/Manhwa/Unordinary_vol1.jpg', 
    6
) RETURNING bookinfo_id;

-- Insert Book Information for Villains Are Destined to Die: Volume 1
INSERT INTO BookInformation (
    title, 
    isbn, 
    publisher, 
    author, 
    language, 
    description, 
    main_genre, 
    type, 
    img_url, 
    nr_of_copies
) VALUES (
    'Villains Are Destined to Die: Volume 1', 
    '9781939424459', 
    'Tappytoon', 
    'Gwon Gyeoeul, SUOL', 
    'English', 
    'A modern girl wakes up as a doomed villainess from her favorite otome game.', 
    'FANTASY', 
    'MANHWA', 
    'views/images/bookCover/Manhwa/VillainsAreDestinedToDie_vol1.jpg', 
    8
) RETURNING bookinfo_id;


select * from bookinformation

