NSTALLATION
Thank you for downloading FriKomPort. The latest version and information about the FriKom project can be found at https://projects.unified.no/x/AoAM.

This is the Java part of the FriKom application, and this takes care of everything that has to do with the courses/classes:

    * Add, modify and remove courses
    * Add, modify and remove locations
    * Add, modify and remove instructors
    * Send e-mail notifications for different events
    * Handle attachments
    * and more

To get started, you need to configure your database settings, tomcat-settings and e-mail settings.
REQUIREMENTS
The following components are required to start developing on this project:

    * Apache Tomcat version 5 or higher
    * SUN JDK 5.0 or higher
    * Apache Ant
    * MySQL v4.1.0 or higher
    * The FriKom eZ-publish project

CONFIGURATION
ENVIRONMENT VARIABLES
$CATALINE_HOME must point to your Tomcat installation folder.
$JAVA_HOME must point to your Java installation folder.
$ANT_HOME must point to your Ant installation folder.
TOMCAT SETTINGS

    * In properties.xml the following settings might come in handy
          o http.port - the port the Tomcat instance is listening on
          o https.port - the SSL enabled port the Tomcat instance is listening on
          o tomcat.server - the host on which the Tomcat server is running

DATABASE SETTINGS
Some configuration is in most cases needed to get the database connection up and running.
To customize these settings for your need, follow the instructions under.

    * The password for the root user of the MySQL instance must be set in "database.admin.password" in the build.properties file found in the root folder.
    * In properties.xml, found in the root folder, there are several properties that needs to be set:
          o     database.name - The name of the schema that will contain the data. The structure and sample-data for this will be created upon building the application.
          o database.host - The host name of the computer that hosts the mysql instance
          o database.username - The user name of the user that the application will use to communicate with the database
          o database.password - The password of the database.username user
          o database.admin.url - The JDBC connection string for administration of the database (for the root user)
          o database.admin.username - The name of the superuser. In most cases, "root" is a good choice.
          o database.admin.password - The password of the superuser
          o database.url - The JDBC connection string for the database.

    * In web\WEB-INF\classes\ApplicationResources.properties several properties needs to be modified
          o ezpublish.database.url - Holds the JDBC location of the ezpublish database
          o ezpublish.database.username - Holds the user name used for connecting to the ezpublish database. Note that this user only needs permission to read from this database.
          o ezpublish.database.password - Holds the password for connecting to the ezpublish database.

EMAIL SETTINGS

    * In the "error.mailTo" attribute in build.properties you can set where you want errors sent. You might want to change the "errors.mailHost" and "errors.server" attributes as well. This feature is not used much, but you should still provide the correct information here.
    * In web\WEB-INF\classes\ApplicationResources.properties you might want to look over all the properties:
          o mail.default.from - The e-mail address as a from-address when sending e-mails from the application.
          o mail.host - The host on which the SMTP server to be used can be found
          o mail.username - (optional) The username used for the SMTP server if it requires user name and password
          o mail.password - (optional) The password user for the SMTP server if it requires user name and password

OTHER SETTINGS
There are some misc settings that need to be reviewed.

    * In web\WEB-INF\classes\ApplicationResources.properties several properties needs to be modified
          o javaapp.baseurl - Holds the external HTTP base string for eZ publish

BUILDING THE PROJECT
The project is developed using Eclipse, and the build system is ANT-based. The project is based on the AppFuse framework (version 1.9.x), so that is a good place to look for information if there are problems with the configuration or building.
The build.xml on the root folder of the project contains several targets, the most important are:

    * setup-db - Wipes the database clean (if it exists), rebuilds it and inserts sample-data into it.
    * clean - Removes all .class-files
    * compile - Does a full compilation of all layers of the code base
          o compile-dao -Compiles the dao-layer
          o compile-service - Compiles the service-layer
          o compile-web - Compiles the web-layer
    * deploy - Checks for changes and does necessary compilation. Then deploys the project to Tomcat
    * setup - Does everything: Clean, setup-db, compile and deploy

For more information on building, see the build.xml file or read about it on the AppFuse website.

We hope you enjoy this piece of software.