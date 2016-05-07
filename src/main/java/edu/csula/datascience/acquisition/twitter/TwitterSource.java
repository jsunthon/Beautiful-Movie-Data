package edu.csula.datascience.acquisition.twitter;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.acquisition.model.TwitterResponse;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterSource implements Source<TwitterResponse> {

	private String TWITTER_CONSUMER_KEY = "KXjY39ROD2QMa0LUIkEBSnJMM";
	private String TWITTER_CONSUMER_SECRET = "lX27EskdFXqFCYwQCo7zK8c7FT9NgL2YpDox0anq4U9g6SrOKG";
	private String TWITTER_ACCESS_TOKEN = "722849671593402368-Fx0XHOaSsIyCQy0VJ6ZWnZ97TmWJ3CG";
	private String TWITTER_ACCESS_SECRET = "ByBD930AcAK9Ue00PvWXMFQ3o7j4RSRSZXthz0rigzKOk";

	private final String[] twitterSearchQuery;
	private TwitterStream twitterStream;

	private Producer twitterProd;
	private Consumer twitterCons;
	private BlockingQueue<TwitterResponse> twitterQueue;
	private Stack<TwitterResponse> twitterResponses;

	private long startTime = System.currentTimeMillis();
	private long totalDuration;

	public TwitterSource(String[] query, long duration) {
		this.twitterSearchQuery = query;
		this.twitterQueue = new LinkedBlockingQueue<TwitterResponse>();
		this.twitterResponses = new Stack<TwitterResponse>();
		this.totalDuration = duration;

		try {
			printQueryString(twitterSearchQuery);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Start producing and consuming tweets
	public void startConsuming() {
		twitterProd = new Producer(twitterQueue);
		twitterCons = new Consumer(twitterQueue);
		twitterProd.start();
		twitterCons.start();
	}

	// Return true if the time limit is not up yet
	@Override
	public boolean hasNext() {
		long currentTime = System.currentTimeMillis();
		long duration = currentTime - startTime;
		return duration < totalDuration;
	}
	
	@Override
	public Collection<TwitterResponse> next() {
		List<TwitterResponse> finalResponses = new ArrayList<>();
		if (!twitterResponses.isEmpty())
			finalResponses.add(twitterResponses.pop()); // size of one
		return finalResponses;
	}

	public void stopStream() {
		twitterStream.cleanUp();
	}

	class Producer extends Thread {
		private BlockingQueue<TwitterResponse> sharedTwitterQueue;

		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				if (validateNulls(status)) {
					try {
						sharedTwitterQueue.put(new TwitterResponse(status.getId(), status.getFavoriteCount(),
								status.getRetweetCount(), status.getUser().getScreenName(), status.getText(),
								status.getCreatedAt().toString(), status.getSource()));
//						System.out.println(
//								"PRODUCED TWEET: @" + status.getUser().getScreenName() + " - " + status.getText());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};

		public Producer(BlockingQueue<TwitterResponse> twitterQueue) {
			super("PRODUCER");
			System.out.println("Starting producer..");
			this.sharedTwitterQueue = twitterQueue;
		}

		public void run() {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			// cb.setDebugEnabled(true).setOAuthConsumerKey(System.getenv("TWITTER_CONSUMER_KEY"))
			// .setOAuthConsumerSecret(System.getenv("TWITTER_CONSUMER_SECRET"))
			// .setOAuthAccessToken(System.getenv("TWITTER_ACCESS_TOKEN"))
			// .setOAuthAccessTokenSecret(System.getenv("TWITTER_ACCESS_SECRET"));
			cb.setDebugEnabled(true).setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
					.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET).setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
					.setOAuthAccessTokenSecret(TWITTER_ACCESS_SECRET);

			twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
			twitterStream.addListener(listener);

			FilterQuery tweetFilterQuery = new FilterQuery();
			tweetFilterQuery.track(twitterSearchQuery);
			tweetFilterQuery.language(new String[] { "en" });
			twitterStream.filter(tweetFilterQuery);
		}
	}

	class Consumer extends Thread {
		private BlockingQueue<TwitterResponse> sharedTwitterQueue;

		public Consumer(BlockingQueue<TwitterResponse> twitterQueue) {
			super("CONSUMER");
			System.out.println("Starting to produce...");
			this.sharedTwitterQueue = twitterQueue;
		}

		public void run() {
			try {
				while (true) {
					TwitterResponse tweet = sharedTwitterQueue.take();
					twitterResponses.push(tweet);
					System.out.println("CONSUMED TWEET: @" + tweet.getUserName() + " - " + tweet.getText());
				}
			} catch (InterruptedException e) {
				System.out.println("error in putting into the queue");
				e.printStackTrace();
			}
		}
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

	private void printQueryString(String[] query) throws InterruptedException {
		System.out.println("Query string: ");
		for (int i = 0; i < query.length; i++) {
			System.out.println(query[i]);
		}
		Thread.sleep(2000);
	}

	private boolean validateNulls(Status status) {
		return (status.getUser().getScreenName() != null && status.getText() != null
				&& status.getCreatedAt().toString() != null && status.getSource() != null);
	}
}
