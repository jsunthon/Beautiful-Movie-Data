package edu.csula.datascience.acquisition;

import com.google.common.collect.Lists;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.Collection;
import java.util.List;

public class TwitterSource implements Source<Status> {
	private long minId;
	private final String searchQuery;
	private String TWITTER_CONSUMER_KEY = "KXjY39ROD2QMa0LUIkEBSnJMM";
	private String TWITTER_CONSUMER_SECRET = "lX27EskdFXqFCYwQCo7zK8c7FT9NgL2YpDox0anq4U9g6SrOKG";
	private String TWITTER_ACCESS_TOKEN = "722849671593402368-Fx0XHOaSsIyCQy0VJ6ZWnZ97TmWJ3CG";
	private String TWITTER_ACCESS_SECRET = "ByBD930AcAK9Ue00PvWXMFQ3o7j4RSRSZXthz0rigzKOk";

	public TwitterSource(long minId, String query) {
		this.minId = minId;
		this.searchQuery = query;
	}

	@Override
	public boolean hasNext() {
		return minId > 0;
	}

	@Override
	public Collection<Status> next() {
		List<Status> list = Lists.newArrayList();
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
		query.setLang("EN");
		query.setSince("20160101");
		if (minId != Long.MAX_VALUE) {
			query.setMaxId(minId);
		}

		list.addAll(getTweets(twitter, query));

		return list;
	}

	private List<Status> getTweets(Twitter twitter, Query query) {
		QueryResult result;
		List<Status> list = Lists.newArrayList();
		try {
			do {
				result = twitter.search(query);

				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					minId = Math.min(minId, tweet.getId());
				}
				list.addAll(tweets);
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
}
