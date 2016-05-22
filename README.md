#BigDataGroup

#Starting the collector
* Run CollectorApp.java

#import the JSON into mongo
mongoimport --db movie-data --collection tweets --file tweets.json

#export the JSON
mongoexport --db movie-data --collection tweets --out tweets.json


