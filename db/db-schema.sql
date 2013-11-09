CREATE TABLE user(
   id 			INTEGER 	PRIMARY KEY,
   name 		TEXT		NOT NULL,
   facebook_name 	TEXT,
   email 		TEXT,
   city 		TEXT,
   country 		TEXT,
   phone 		INTEGER,
   location_x 		REAL,
   location_y 		REAL,
   birthdate 		CHAR(8)		NOT NULL,
   picture		BLOB
);

CREATE TABLE buddy(
   id_user 	INTEGER,
   id_buddy 	INTEGER,
   FOREIGN KEY(id_user) REFERENCES User(id),
   FOREIGN KEY(id_buddy) REFERENCES User(id)
);

CREATE TABLE bar(
   id 		INTEGER 	PRIMARY KEY,
   name 	TEXT		NOT NULL,
   photo	BLOB,
   website 	TEXT,
   id_address	INTEGER		NOT NULL,
   location_x 	REAL,
   location_y 	REAL,
   FOREIGN KEY(id_address) REFERENCES Address(id)
);

CREATE TABLE address(
   id		INTEGER		PRIMARY KEY,
   street 	TEXT,
   number	TEXT,
   zipcode 	TEXT,
   city 	TEXT,
   country 	TEXT
);

CREATE TABLE favorite_bar(
   id_user 	INTEGER,
   id_bar 	INTEGER,
   FOREIGN KEY(id_user) REFERENCES User(id),
   FOREIGN KEY(id_bar) REFERENCES Bar(id)
);

CREATE TABLE beer(
   id 			INTEGER 	PRIMARY KEY,
   url			TEXT		NOT NULL,
   name 		TEXT		NOT NULL,
   webservice_name	TEXT		NOT NULL
);

CREATE TABLE favorite_beer (
   id_user 	INTEGER,
   id_beer 	INTEGER,
   FOREIGN KEY(id_user) REFERENCES User(id),
   FOREIGN KEY(id_beer) REFERENCES Beer(id)
);

CREATE TABLE beer2_bar (
   id_beer 	INTEGER,
   id_bar 	INTEGER,
   FOREIGN KEY(id_beer) REFERENCES Beer(id),
   FOREIGN KEY(id_bar) REFERENCES Bar(id)
);

CREATE TABLE review (
   id			INTEGER		PRIMARY KEY,
   id_beer 		INTEGER,
   id_user		INTEGER,
   visual_score		INTEGER,
   smell_score		INTEGER,
   taste_score		INTEGER,
   feel_score		INTEGER,
   text			TEXT,
   post_time		CHAR(16),
   FOREIGN KEY(id_beer) REFERENCES Beer(id),
   FOREIGN KEY(id_user) REFERENCES User(id)
);

CREATE TABLE review_comment (
   id		INTEGER		PRIMARY KEY,
   id_review 	INTEGER,
   id_user	INTEGER,
   text		TEXT,
   post_time	CHAR(16),
   FOREIGN KEY(id_review) REFERENCES Review(id),
   FOREIGN KEY(id_user) REFERENCES User(id)
);

CREATE TABLE beer_tag (
   name		TEXT		PRIMARY KEY
);

CREATE TABLE beer2_beer_tag (
   beertag 	TEXT_,
   id_beer 	INTEGER,
   FOREIGN KEY(beertag) REFERENCES BeerTag(name),
   FOREIGN KEY(id_beer) REFERENCES Beer(id)
);

CREATE TABLE bar_comment (
   id		INTEGER		PRIMARY KEY,
   id_user	INTEGER		NOT NULL,
   text		TEXT,
   timestamp	CHAR(16)
);

CREATE TABLE bar2_bar_comment (
   id_barcomment	INTEGER,
   id_bar 		INTEGER,
   FOREIGN KEY(id_barcomment) REFERENCES BarComment(id),
   FOREIGN KEY(id_bar) REFERENCES Bar(id)
);

CREATE TABLE bar_score (
   id		INTEGER		PRIMARY KEY,
   score	INTEGER,
   post_time	CHAR(16),
   user_id	INTEGER,
   FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE bar2_bar_score (
   bar_id	INTEGER,
   bar_score_id	INTEGER,
   FOREIGN KEY (bar_id) REFERENCES Bar(id),
   FOREIGN KEY (bar_score_id) REFERENCES BarScore(id)
);
