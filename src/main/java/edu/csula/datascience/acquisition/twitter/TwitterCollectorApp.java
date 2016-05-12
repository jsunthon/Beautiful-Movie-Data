package edu.csula.datascience.acquisition.twitter;

import java.util.Collection;
import edu.csula.datascience.acquisition.model.TwitterResponse;

public class TwitterCollectorApp {

	public static void main(String[] args) {
		/**
		 * second argument is the amount of time to start streaming.it's in ms,
		 * so for example, 15000 is 15 seconds. 1000 is 1 second, ect.
		 */
		TwitterSource source = new TwitterSource(new String[] { "love", "star" });
		TwitterCollector collector = new TwitterCollector();

		source.startConsuming();

		while (true) {
			if (source.hasNext()) {
				Collection<TwitterResponse> initResponses = source.next();
				Collection<TwitterResponse> cleanedTweets = collector.mungee(initResponses);
				collector.save(cleanedTweets);
			}
		}
	}
}
