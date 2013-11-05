CREATE TABLE DrinkingBuddies.User(
   id 			INTEGER 	PRIMARY KEY,
   name 		TEXT		NOT NULL,
   facebookname 	TEXT,
   email 		TEXT,
   city 		TEXT,
   country 		TEXT,
   phone 		INTEGER,
   location_x 		REAL,
   location_y 		REAL,
   birthdate 		CHAR(8)		NOT NULL,
   picture		BLOB
);

CREATE TABLE DrinkingBuddies.Buddy (
   Id_user 	INTEGER,
   Id_buddy 	INTEGER,
   FOREIGN KEY(id_user) REFERENCES User(id),
   FOREIGN KEY(id_buddy) REFERENCES User(id)
);

CREATE TABLE DrinkingBuddies.Bar(
   id 		INTEGER 	PRIMARY KEY,
   name 	TEXT		NOT NULL,
   photo	BLOB,
   website 	TEXT,
   id_address	INTEGER,
   location_x 	REAL,
   location_y 	REAL,
   FOREIGN KEY(id_address) REFERENCES Address(id)
);

CREATE TABLE DrinkingBuddies.Address(
   id		INTEGER		PRIMARY KEY,
   street 	TEXT,
   number	TEXT,
   zipcode 	TEXT,
   city 	TEXT,
   country 	TEXT
);

CREATE TABLE DrinkingBuddies.FavoriteBar (
   Id_user 	INTEGER,
   Id_bar 	INTEGER,
   FOREIGN KEY(id_user) REFERENCES User(id),
   FOREIGN KEY(id_bar) REFERENCES Bar(id)
);

CREATE TABLE DrinkingBuddies.Beer(
   id 			INTEGER 	PRIMARY KEY,
   name 		TEXT		NOT NULL,
   webservice_name	TEXT
);

CREATE TABLE DrinkingBuddies.FavoriteBeer (
   id_user 	INTEGER,
   id_beer 	INTEGER,
   FOREIGN KEY(id_user) REFERENCES User(id),
   FOREIGN KEY(id_beer) REFERENCES Beer(id)
);

CREATE TABLE DrinkingBuddies.BeerBar (
   id_beer 	INTEGER,
   id_bar 	INTEGER,
   FOREIGN KEY(id_beer) REFERENCES Beer(id),
   FOREIGN KEY(id_bar) REFERENCES Bar(id)
);

CREATE TABLE DrinkingBuddies.Review (
   id			INTEGER		PRIMARY KEY,
   id_beer 		INTEGER,
   id_user		INTEGER,
   visual_score		INTEGER,
   smell_score		INTEGER,
   taste_score		INTEGER,
   afetertaste_score	INTEGER,
   text			TEXT,
   post_time		CHAR(16),
   FOREIGN KEY(id_beer) REFERENCES Beer(id),
   FOREIGN KEY(id_user) REFERENCES User(id)
);

CREATE TABLE DrinkingBuddies.ReviewComment (
   id		INTEGER		PRIMARY KEY,
   id_review 	INTEGER,
   id_user	INTEGER,
   text		TEXT,
   post_time	CHAR(16),
   FOREIGN KEY(id_review) REFERENCES Review(id),
   FOREIGN KEY(id_user) REFERENCES User(id)
);

CREATE TABLE DrinkingBuddies.BeerTag (
   name		TEXT		PRIMARY KEY
);

CREATE TABLE DrinkingBuddies.Beer_BeerTag (
   beertag 	TEXT,
   id_beer 	INTEGER,
   FOREIGN KEY(beertag) REFERENCES BeerTag(name),
   FOREIGN KEY(id_beer) REFERENCES Beer(id)
);

CREATE TABLE DrinkingBuddies.BarComment (
   id		INTEGER		PRIMARY KEY,
   id_user	INTEGER		NOT NULL,
   text		TEXT,
   timestamp	CHAR(16)
);

CREATE TABLE DrinkingBuddies.Bar_BarComment (
   id_barcomment	INTEGER,
   id_bar 		INTEGER,
   FOREIGN KEY(id_barcomment) REFERENCES BarComment(id),
   FOREIGN KEY(id_bar) REFERENCES Bar(id)
);

CREATE TABLE DrinkingBuddies.BarScore (
   Id		INTEGER		PRIMARY KEY,
   score	INTEGER,
   post_time	CHAR(16),
   user_id	INTEGER,
   FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE DrinkingBuddies.Bar_BarScore (
   bar_id	INTEGER,
   barScore_id	INTEGER,
   FOREIGN KEY (bar_id) REFERENCES Bar(id),
   FOREIGN KEY (barScore_id) REFERENCES BarScore(id)
);
