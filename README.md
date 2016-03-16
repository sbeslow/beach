#DrekBeach

####DrekBeach is an Open Source, Open Data project monitoring the health of Chicago's 27 beaches.  Visit the website at http://www.drekbeach.org

###What it does
During Chicago's beach season (Memorial Day through Labor Day), each beach is given a status of no advisory, swim advisory, or swim ban based on various conditions.  One of these factors is the predicted e-coli level, which estimates the level of fecal bacteria in the water.  This value is estimated based on an EPA model because the actual test takes 18 hours to incubate.  The Chicago Park District publishes the status of the beach, as well as the current day's estimated e-coli level and yesterday's measured level.  This website scrapes that data and maintains an historic record to assess the health of our beaches over the course of the season.

The front-end compares the beaches by giving weight to swim advisories and swim bans (bans = 2 x advisories) called the Poo Score, which ultimately measures how often the beach flies the yellow or red flag.

###Tech Stack
This application was written in Java using the Play! Framework version 2.3.8.  The database is Postgresql.  The front end uses Bootstrap, JQuery, and the Scala-based templating language.  The charts are built using HighCharts (http://www.highcharts.com).  This application is currently being hosted on Microsoft Azure on a CentOS Linux server.

###How can I run this myself?


To build and run this app, first install the Play! Framework at https://www.playframework.com/documentation/2.4.x/Installing

See http://www.playframework.com for more information about the project structure, running, and deploying this app
