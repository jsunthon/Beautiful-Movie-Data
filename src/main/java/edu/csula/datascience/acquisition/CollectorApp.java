package edu.csula.datascience.acquisition;

import edu.csula.datascience.acquisition.csv.CsvCollector;
import edu.csula.datascience.acquisition.csv.CsvSource;
import edu.csula.datascience.acquisition.twitter.TwitterCollector;
import edu.csula.datascience.acquisition.twitter.TwitterResponse;
import edu.csula.datascience.acquisition.twitter.TwitterSource;
import edu.csula.datascience.utilities.MongoUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectorApp {
    public static void main(String[] args){
        boolean firstRun = true;

        while(true) { //will run through all titles to continuously go through live twitter feed
            //TODO add file
            String file = "mergedMovieData.csv";
            CsvSource source = new CsvSource(file, true);
            CsvCollector collector = new CsvCollector();

            while (source.hasNext()) {
                Collection<Movie> movies = source.next();
                Collection<Movie> mungedMovies = collector.mungee(movies);
                ArrayList<Movie> munged = new ArrayList(mungedMovies);
                if (!mungedMovies.isEmpty() && munged.get(0).getYear() > 2010) {
                    if(firstRun) {
                        //only saves csv data on first run, used to continuously run through twitter
                        collector.save(mungedMovies);
                    }

                    TwitterSource tsource = new TwitterSource(munged.get(0).getHashtagTitle(), 10000);
                    TwitterCollector tcollector = new TwitterCollector();
                    Set<TwitterResponse> initResponses = new HashSet<TwitterResponse>();

                    while (tsource.hasNext()) {
                        Collection<TwitterResponse> tweets = tsource.next();
                        if (tweets.size() != 0) {
                            initResponses.addAll(tweets);
                        }
                    }
                    tsource.stopStream();
                    Collection<TwitterResponse> cleanedTweets = tcollector.mungee(initResponses);
                    tcollector.save(cleanedTweets);
                } else {
                    continue;
                }

            }
            firstRun = false;
        }
        //MongoUtilities mongoUtil = new MongoUtilities("movie-data", "csv_files");
        //mongoUtil.printDocuments();

    }
}
