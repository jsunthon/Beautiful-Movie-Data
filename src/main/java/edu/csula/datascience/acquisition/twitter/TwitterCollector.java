package edu.csula.datascience.acquisition.twitter;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.csula.datascience.acquisition.Collector;
import edu.csula.datascience.acquisition.model.TwitterResponse;
import edu.csula.datascience.utilities.TweetAnalysis;
import twitter4j.Status;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TwitterCollector implements Collector<TwitterResponse, TwitterResponse> {
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> collection;

	public TwitterCollector() {
		mongoClient = new MongoClient();
		database = mongoClient.getDatabase("movie-data");
		collection = database.getCollection("tweets");
	}

	@Override
	public Collection<TwitterResponse> mungee(Collection<TwitterResponse> src) {
		
		List<TwitterResponse> tweet = (ArrayList<TwitterResponse>) src;
		if (validateNulls(tweet.get(0)))  {
			System.out.println("Successfully mungee " + tweet.get(0));
			return tweet;
		}
		
		return new ArrayList<TwitterResponse>();
	}

	@Override
	public void save(Collection<TwitterResponse> data) {
		
		if (data.size() == 0 || data == null) return;
		
		System.out.println("Data size in saved method: " + data.size());
		List<Document> documents = data.stream()
				.map(item -> new Document().append("tweetId", item.getId()).append("favCt", item.getFavCt())
						.append("retwtCt", item.getRetwtCt()).append("username", item.getUserName())
						.append("text", item.getText()).append("date", item.getDate())
						.append("source", item.getSource()))
				.collect(Collectors.toList());

		collection.insertMany(documents);
	}
	
	private boolean validateNulls(TwitterResponse tweet) {
		return (tweet.getUserName() != null && tweet.getText() != null
				&& tweet.getDate() != null && tweet.getSource() != null);
	}

}
