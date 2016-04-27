package edu.csula.datascience.acquisition.twitter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TwitterCollectorApp {
	public static void main(String[] args) {
		
		/**
		 * second argument is the amount of time to start streaming.it's in ms,
		 * so for example, 15000 is 15 seconds. 1000 is 1 second, ect.
		 */
		TwitterSource source = new TwitterSource("love", 10000);
		TwitterCollector collector = new TwitterCollector();
		Set<TwitterResponse> initResponses = new HashSet<TwitterResponse>();

		while (source.hasNext()) {
			Collection<TwitterResponse> tweets = source.next();
			if (tweets.size() != 0) {
				initResponses.addAll(tweets);
			}
		}		
		source.stopStream();
		Collection<TwitterResponse> cleanedTweets = collector.mungee(initResponses);
		collector.save(cleanedTweets);
	}
}
