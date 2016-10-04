#Beautiful Movie Data
This project is composed of a collector and an exporter. The collector's job is to use Twitter streams involving the use of movie titles. This data is stored into MongoDB. The exporter exports it to Amazon Web Service's elastic search to perform visual analysis. Results also visualized through JavaScript here: https://beautiful-movie-team.firebaseapp.com/.

#import the JSON into mongo
* `mongoimport --db movie-data --collection tweets --file tweets.json`
* `mongoimport --db movie-data --collection csv_files --file movies.json`

#export the JSON
* `mongoexport --db movie-data --collection tweets --out tweets.json`
* `mongoexport --db movie-data --collection csv_files --out movies.json`

