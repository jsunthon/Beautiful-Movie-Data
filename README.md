#BigDataGroup

#Starting the collector
* Run CollectorApp.java

#Creating a branch
* `git checkout homework_3`
* `git pull` //make sure you have most up-to-date homework_3
* `git branch yourBranch`
* `git push origin yourBranch` //this is used to put your new local branch onto github.com repo
* `git checkout yourBranch`
* `git branch` //do this to make sure you're on your new branch

#import the JSON into mongo
* `mongoimport --db movie-data --collection tweets --file tweets.json`
* `mongoimport --db movie-data --collection csv_files --file movies.json`

#export the JSON
* `mongoexport --db movie-data --collection tweets --out tweets.json`
* `mongoexport --db movie-data --collection csv_files --out movies.json`

#Set Path for the positive and negative text files 
* `I placed the two files on the desktop and`
* `My path is `
* `final String pathPositive = "/Users/anto004/Desktop/positive-words.txt";`
* `final String pathNegative = "/Users/anto004/Desktop/negative-words.txt";`
