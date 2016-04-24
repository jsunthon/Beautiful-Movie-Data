package edu.csula.datascience.acquisition;

import java.util.Collection;
import edu.csula.datascience.utilities.MongoUtilities;

public class TwitterCollectorApp {
    public static void main(String[] args) {
        TwitterSource source = new TwitterSource(Long.MAX_VALUE, "#candle");
        TwitterCollector collector = new TwitterCollector();
         
//        twitter has a limit on how many calls you can make. comment this code out
//        if you want to stop the calls
        while (source.hasNext()) {
            Collection<TwitterResponse> tweets = source.next();
            Collection<TwitterResponse> cleanedTweets = collector.mungee(tweets);
            collector.save(cleanedTweets);
        }
        
        //create a MongoUtilities object to test that the results are saved
        MongoUtilities mongoUtil = new MongoUtilities("movie-data", "tweets");
        mongoUtil.printDocuments();
    }
      
}
