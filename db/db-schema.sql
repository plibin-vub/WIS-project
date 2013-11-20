CREATE TABLE user(
   id 				INTEGER 	PRIMARY KEY,
   name 			TEXT		NOT NULL,
   oauth_name 		TEXT,
   oauth_provider	TEXT,
   location_x 		REAL,
   location_y 		REAL,
   birthdate 		CHAR(8)		NOT NULL,
   picture			BLOB
);

CREATE TABLE buddy(
   user_id 			INTEGER,
   buddy_id 		INTEGER,
   FOREIGN KEY(user_id) REFERENCES user(id),
   FOREIGN KEY(buddy_id) REFERENCES user(id)
);

CREATE TABLE bar(
   id 				INTEGER 	PRIMARY KEY,
   name 			TEXT		NOT NULL,
   photo			BLOB,
   website 			TEXT,
   address_id		INTEGER		NOT NULL,
   location_x 		REAL,
   location_y 		REAL,
   FOREIGN KEY(address_id) REFERENCES address(id)
);

CREATE TABLE address(
   id				INTEGER		PRIMARY KEY,
   street 			TEXT,
   number			TEXT,
   zipcode 			TEXT,
   city 			TEXT,
   country 			TEXT
);

CREATE TABLE favorite_bar(
   user_id 			INTEGER,
   bar_id 			INTEGER,
   FOREIGN KEY(user_id) REFERENCES user(id),
   FOREIGN KEY(bar_id) REFERENCES bar(id)
);

CREATE TABLE beer(
   id 				INTEGER 	PRIMARY KEY,
   url				TEXT		NOT NULL,
   name 			TEXT		NOT NULL,
   webservice_name	TEXT		NOT NULL
);

CREATE TABLE favorite_beer (
   user_id 			INTEGER,
   beer_id 			INTEGER,
   FOREIGN KEY(user_id) REFERENCES user(id),
   FOREIGN KEY(beer_id) REFERENCES beer(id)
);

CREATE TABLE beer2_bar (
   beer_id 			INTEGER,
   bar_id			INTEGER,
   FOREIGN KEY(beer_id) REFERENCES beer(id),
   FOREIGN KEY(bar_id) REFERENCES bar(id)
);

CREATE TABLE review (
   id				INTEGER		PRIMARY KEY,
   beer_id 			INTEGER,
   user_id			INTEGER,
   visual_score			real,
   smell_score			real,
   taste_score			real,
   feel_score			real,
   text				TEXT,
   post_time		CHAR(16),
   FOREIGN KEY(beer_id) REFERENCES beer(id),
   FOREIGN KEY(user_id) REFERENCES user(id)
);

CREATE TABLE review_comment (
   id				INTEGER		PRIMARY KEY,
   review_id 		INTEGER,
   user_id			INTEGER,
   text				TEXT,
   post_time		CHAR(16),
   FOREIGN KEY(review_id) REFERENCES review(id),
   FOREIGN KEY(user_id) REFERENCES user(id)
);

CREATE TABLE beer_tag (
   id 				INTEGER 	PRIMARY KEY,
   name				TEXT		UNIQUE	
);

CREATE TABLE beer2_beer_tag (
   beer_tag_id 		INTEGER,
   beer_id 			INTEGER,
   FOREIGN KEY(beer_tag_id) REFERENCES beer_tag(id),
   FOREIGN KEY(beer_id) REFERENCES beer(id)
);

CREATE TABLE bar_comment (
   id				INTEGER		PRIMARY KEY,
   user_id			INTEGER		NOT NULL,
   text				TEXT,
   timestamp		CHAR(16)
);

CREATE TABLE bar2_bar_comment (
   bar_comment_id	INTEGER,
   bar_id 			INTEGER,
   FOREIGN KEY(bar_comment_id) REFERENCES bar_comment(id),
   FOREIGN KEY(bar_id) REFERENCES bar(id)
);

CREATE TABLE bar_score (
   id			INTEGER		PRIMARY KEY,
   score		INTEGER,
   post_time	CHAR(16),
   user_id		INTEGER,
   FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE bar2_bar_score (
   bar_id	INTEGER,
   bar_score_id	INTEGER,
   FOREIGN KEY (bar_id) REFERENCES bar(id),
   FOREIGN KEY (bar_score_id) REFERENCES bar_score(id)
);
