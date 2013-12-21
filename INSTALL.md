Required software:
* Java version >=1.6
* Servlet 3.0 API compatible servlet container (Apache Tomcat, Jetty, ...)
* ant (only to build the software)
* sqlite database 

References:
$WIS-project: refers to this project's directory
$sqlite: refers to your sqlite binary 

Installation:
* initiate the database (\favorite_location\ can be any directory where you want to setup the database)
** $sqlite \favorite_location\DrinkingBuddies.db < $WIS-project\db\db-schema.sql 
** $sqlite \favorite_location\DrinkingBuddies.db < $WIS-project\db\beers.sql
* to be able to use Facebook OAuth, you need to register your instance of this application
** register your application with Facebook: https://developers.facebook.com/apps
** after registration, write down these values:
*** App ID, App Secret, Site URL
** NOTE: for development purposes you can use the settings as configured in the example configuration file ($WIS-project\drinking-buddies-config.xml) 
* to be able to use the BreweryDB web services,
** create an account at: http://www.brewerydb.com
** after registration, write down your API key
** NOTE: for development purposes you can use the settings as configured in the example configuration file ($WIS-project\drinking-buddies-config.xml)
 
