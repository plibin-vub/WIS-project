Required software:
* Java version >=1.6
* Servlet 3.0 API compatible servlet container (Apache Tomcat, Jetty, ...)
* ant (only to build the software)
* sqlite database 

References:
* $WIS-project: refers to this project's directory
* $sqlite: refers to your sqlite binary 

Installation:
* initiate the database (/favorite_location/ can be any directory where you want to setup the database)
 * $sqlite /favorite_location/DrinkingBuddies.db < $WIS-project/db/db-schema.sql 
 * $sqlite /favorite_location/DrinkingBuddies.db < $WIS-project/db/beers.sql
* to be able to use Facebook OAuth, you need to register your instance of this application
 * register your application with Facebook: https://developers.facebook.com/apps
 * after registration, write down these values: App ID, App Secret, Site URL
 * NOTE: for development purposes (only if you test on http://localhost:8080/db !) you can use the settings as configured in the example configuration file ($WIS-project/config.xml) 
* to be able to use the BreweryDB web services,
 * create an account at: http://www.brewerydb.com
 * after registration, write down your API key
 * NOTE: for development purposes you can use the settings as configured in the example configuration file ($WIS-project/config.xml)
* create a configuration file based on the example XML file $WIS-project/drinking-buddies-config.xml
* move the newly created configuration file to 
 * C:\DrinkingBuddies\config.xml (on Windows)
 * /etc/drinking_buddies/config.xml (on MacOS X, Linux or other Unix-based systems)
* build the war file
 * cd $WIS-project
 * ant 
 * after a succesful build, the drinking-buddies.war can be found in $WIS-project/dist
* install drinking-buddies.war in your servlet container
 * for this, consult you servlet container manual
* you should now be able to access the application on the URL where you installed the war file on
 * NOTE 1: the context of the application is /db, so to run the application you should use the url http://mydomain.com/install-url/db/ 
 * NOTE 2: You can change the default context (/db) in $WIS-project/WebRoot/WEB-INF/web.xml; to do this, you should change the servlet mappings 
