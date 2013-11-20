WIS-project
===========
instantiate the database:
 run:	sqlite DrinkingBuddies.db
	on the sqlite shell: .read db/db-schema.sql
generate jooq classes:
 run: 	ant generate-jooq-classes 
	(this task expects your database to be located in the root of the project with the name DrinkingBuddies.db)
 note: 	you only need to do this if you made changes to schema
