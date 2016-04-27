package edu.csula.datascience.acquisition;

import edu.csula.datascience.acquisition.csv.CsvCollector;
import edu.csula.datascience.acquisition.csv.CsvSource;
import edu.csula.datascience.acquisition.twitter.TwitterCollector;
import edu.csula.datascience.acquisition.twitter.TwitterResponse;
import edu.csula.datascience.acquisition.twitter.TwitterSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectorApp {
	public static void main(String[] args) throws InterruptedException {
		
		/**
		 * SET THE TIME LENGTH YOU WANT TO LISTEN TO EACH MOVIE. In ms!
		 */
		long streamDuration = 60000;

		String file = "mergedMovieData.csv";
		CsvSource source = new CsvSource(file, true);
		CsvCollector collector = new CsvCollector();

		while (source.hasNext()) {
			Collection<Movie> movies = source.next();
			Collection<Movie> mungedMovies = collector.mungee(movies);
			ArrayList<Movie> munged = new ArrayList(mungedMovies);

			if (!mungedMovies.isEmpty()) {
				System.out.println("NOT SAVED: munged movie of YEAR: " + munged.get(0).getYear());
				if (munged.get(0).getYear() > 2010) {
					collector.save(mungedMovies);
					TwitterSource tsource = new TwitterSource(munged.get(0).getHashtagTitle(), streamDuration);
					TwitterCollector tcollector = new TwitterCollector();
					Set<TwitterResponse> initResponses = new HashSet<TwitterResponse>();
					System.out.println(
							"size of SAVED munged " + munged.size() + " and year is " + munged.get(0).getYear());
					while (tsource.hasNext()) {
						Collection<TwitterResponse> tweets = tsource.next();
						if (tweets.size() != 0) {
							initResponses.addAll(tweets);
						}
					}
					tsource.stopStream();
					tsource = null;
					Collection<TwitterResponse> cleanedTweets = tcollector.mungee(initResponses);
					tcollector.save(cleanedTweets);
					System.out.println("Sleeping the thread...");
					Thread.sleep(10000);
				}
			}
		}
	}
}
