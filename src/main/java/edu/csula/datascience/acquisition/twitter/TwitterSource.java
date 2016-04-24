package edu.csula.datascience.acquisition.twitter;

import com.google.common.collect.Lists;

import edu.csula.datascience.acquisition.csv.Source;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TwitterSource implements Source<TwitterResponse> {
	private long minId;
	private final String searchQuery;
	private String TWITTER_CONSUMER_KEY = "KXjY39ROD2QMa0LUIkEBSnJMM";
	private String TWITTER_CONSUMER_SECRET = "lX27EskdFXqFCYwQCo7zK8c7FT9NgL2YpDox0anq4U9g6SrOKG";
	private String TWITTER_ACCESS_TOKEN = "722849671593402368-Fx0XHOaSsIyCQy0VJ6ZWnZ97TmWJ3CG";
	private String TWITTER_ACCESS_SECRET = "ByBD930AcAK9Ue00PvWXMFQ3o7j4RSRSZXthz0rigzKOk";

	public TwitterSource(long minId, String query) {
		System.out.println(query);
		this.minId = minId;
		this.searchQuery = query;
	}

	@Override
	public boolean hasNext() {
		return minId > 0;
	}

	@Override
	public Collection<TwitterResponse> next() {
		List<TwitterResponse> list = Lists.newArrayList();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		// cb.setDebugEnabled(true).setOAuthConsumerKey(System.getenv("TWITTER_CONSUMER_KEY"))
		// .setOAuthConsumerSecret(System.getenv("TWITTER_CONSUMER_SECRET"))
		// .setOAuthAccessToken(System.getenv("TWITTER_ACCESS_TOKEN"))
		// .setOAuthAccessTokenSecret(System.getenv("TWITTER_ACCESS_SECRET"));

		// testing purposes, won't be final. will deal with hiding these keys
		// and secrets later
		cb.setDebugEnabled(true).setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
				.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET).setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(TWITTER_ACCESS_SECRET);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		Query query = new Query(searchQuery);
		query.setCount(15);
//		query.setLang("EN");
//		query.setSince("20100101");
		if (minId != Long.MAX_VALUE) {
			query.setMaxId(minId);
		}

		list.addAll(getTweets(twitter, query));
//		printText(list);
		return list;
	}

	private List<TwitterResponse> getTweets(Twitter twitter, Query query) {
		QueryResult result;
		
		List<TwitterResponse> list = Lists.newArrayList();
		try {
			do {
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				result = twitter.search(query);
				System.out.println("completed in " + result.getCompletedIn());
				System.out.println("result page : " + result.getCount());
				List<Status> tweets = result.getTweets();
				System.out.println("First Twitter API Call, size of list: " + tweets.size());
				
				if (tweets.size() <= 1) {
					minId = 0;
					return list;
				}
				
				for (Status tweet : tweets) {
					System.out.println("text: " + tweet.getText());
					System.out.println("id: " + tweet.getId());
					minId = Math.min(minId, tweet.getId());
					list.add(new TwitterResponse(tweet.getId(), tweet.getFavoriteCount(), tweet.getRetweetCount(),
							tweet.getUser().getName(), tweet.getText(), tweet.getCreatedAt().toString(), tweet.getSource()));
				}

			} while ((query = result.nextQuery()) != null);
		} catch (TwitterException e) {
			// Catch exception to handle rate limit and retry
			e.printStackTrace();
			System.out.println("Got twitter exception. Current min id " + minId);
			try {
				Thread.sleep(e.getRateLimitStatus().getSecondsUntilReset() * 1000);
				list.addAll(getTweets(twitter, query));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		return list;
	}
	
	/**
	 * Helper methods
	 */	
	public void printText(Collection<TwitterResponse> list) {
		Iterator<TwitterResponse> iterator = list.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().getText());
		}
	}
}
