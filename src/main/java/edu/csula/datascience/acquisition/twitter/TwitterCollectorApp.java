package edu.csula.datascience.acquisition.twitter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TwitterCollectorApp {
    public static void main(String[] args) {
        TwitterSource source = new TwitterSource(Long.MAX_VALUE, "#candle");
        TwitterCollector collector = new TwitterCollector();
        Set<TwitterResponse> cleanedTweets = new HashSet<TwitterResponse>();
        
//        twitter has a limit on how many calls you can make. comment this code out
//        if you want to stop the calls
        while (source.hasNext()) {
            Collection<TwitterResponse> tweets = source.next();
            cleanedTweets.addAll((Set<TwitterResponse>) collector.mungee(tweets)); //returns a collection with all duplicates removed
            
        }
        if (cleanedTweets != null) {
        	collector.save(cleanedTweets);
        }
                
//        //create a MongoUtilities object to test that the results are saved
//        MongoUtilities mongoUtil = new MongoUtilities("movie-data", "tweets");
//        mongoUtil.printDocuments();
    }
      
}
