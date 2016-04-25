package edu.csula.datascience.acquisition.csv;


import edu.csula.datascience.acquisition.Movie;
import edu.csula.datascience.acquisition.twitter.TwitterCollector;
import edu.csula.datascience.acquisition.twitter.TwitterResponse;
import edu.csula.datascience.acquisition.twitter.TwitterSource;
import edu.csula.datascience.utilities.MongoUtilities;

import java.util.ArrayList;
import java.util.Collection;

public class CsvCollectorApp {
    public static void main(String[] args){
        //TODO add file
        String file = "test.csv";
        CsvSource source = new CsvSource(file, true);
        CsvCollector collector = new CsvCollector();

        while (source.hasNext()){
            Collection<Movie> movies = source.next();
            Collection<Movie> mungedMovies = collector.mungee(movies);
            ArrayList<Movie> munged = new ArrayList(mungedMovies);
            if (!mungedMovies.isEmpty() && munged.get(0).getYear() > 2010){
                collector.save(mungedMovies);

                TwitterSource tSource = new TwitterSource(Long.MAX_VALUE, munged.get(0).getHashtagTitle());
                TwitterCollector tCollector = new TwitterCollector();


                //twitter has a limit on how many calls you can make. comment this code out
                //if you want to stop the calls
                while (tSource.hasNext()) {
                    Collection<TwitterResponse> tweets = tSource.next();
                    Collection<TwitterResponse> cleanedTweets = tCollector.mungee(tweets); //returns a collection with all duplicates removed
                    tCollector.save(cleanedTweets);
                }
            }
            else{
                continue;
            }

        }

        MongoUtilities mongoUtil = new MongoUtilities("movie-data", "csv_files");
        mongoUtil.printDocuments();

    }
}
