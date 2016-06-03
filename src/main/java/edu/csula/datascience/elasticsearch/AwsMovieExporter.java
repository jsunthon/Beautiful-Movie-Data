package edu.csula.datascience.elasticsearch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCursor;

import edu.csula.datascience.elasticsearch.model.Movie;
import edu.csula.datascience.elasticsearch.model.Tweet;
import edu.csula.datascience.utilities.MongoUtilities;
import io.searchbox.action.BulkableAction;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

//import movie data to AWS
public class AwsMovieExporter {
	private final static String indexName = "beautiful-movie-team-data";
	private final static String typeName = "movies"; //type name
	private List<Movie> movies;
	private List<Tweet> tweets = new ArrayList<>();
	private String awsAddress;
	private JestClientFactory factory;
	private JestClient client;
	
	public AwsMovieExporter(List<Movie> movies, String awsAddress) {
		this.movies = movies;
		this.awsAddress = awsAddress;
		this.factory.setHttpClientConfig(new HttpClientConfig
	            .Builder(awsAddress)
	            .multiThreaded(true)
	            .build());
		this.client = factory.getObject();
	}
	
	public void exportToAwsES() {
		int tweetCounter = 0;
		MongoUtilities mongo = new MongoUtilities("movie-data", "tweets");
		MongoCursor<Document> cursor = mongo.getCollection().find().iterator();

		while (cursor.hasNext()) {
			Document document = cursor.next();
			if (validateDocument(document)) {
				String tweetTxt = document.getString("text");
				for (Movie movie : movies) {
					String hashTitle = movie.getHashTitle();
					String movieTitle = movie.getTitle();
					if (tweetTxt.contains(hashTitle) || tweetTxt.contains(movieTitle)) {
						tweetCounter++;
						Tweet tweet = new Tweet(document.getString("username"), document.getString("text"),
								movieTitle, hashTitle, movie.getRating(), document.getString("date"));
						tweets.add(tweet); //add a tweet
						System.out.println("Tweet #: " + tweetCounter + " added to tweets list; " + tweet.getText()) ;
					}
				}
			}
		}
		
		if (tweets.size() != 0) {
			addSentimentForMovie();
			exportMovies(movies); //at this pt, we have our final list of movie objs.
		}
	}
	
	public void exportMovies(List<Movie> movies) {
        try {
            Collection<BulkableAction> actions = Lists.newArrayList();
            movies.stream()
                .forEach(movie -> {
                    actions.add(new Index.Builder(movie).build());
                });
            Bulk.Builder bulk = new Bulk.Builder()
                .defaultIndex(indexName)
                .defaultType(typeName)
                .addAction(actions);
            client.execute(bulk.build());
            System.out.println("Inserted " + movies.size() + " Movies to AWS Cloud");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void addSentimentForMovie()
	{
		 try {
			List<String> positiveWords = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("positive-words.txt").toURI()));
			List<String> negativeWords = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("negative-words.txt").toURI()));
		
			for(Movie movie: movies)
			{
				int positiveCounter = 0;
				int negativeCounter = 0;
				int tweetCounter = 0;
				double sentiment = 0.0;
				for(Tweet tweet: tweets){
					if(tweet.getTitle().equals(movie.getTitle()))
					{
						String []tweetArray = tweet.getText().split("\\s");
						tweetCounter++;
						for(String positiveWord: positiveWords)
						{
							for(int i=0; i<tweetArray.length; i++)
							if(tweetArray[i].contains(positiveWord)){
								positiveCounter++;
							}
						}
						for(String negativeWord: negativeWords)
						{
							for(int i=0; i<tweetArray.length; i++)
							if(tweetArray[i].contains(negativeWord)){
								negativeCounter++;
							}
						}
					}	
				}
				if(tweetCounter != 0)
				{
					sentiment = (double) (positiveCounter - negativeCounter) / tweetCounter;
				}
				System.out.println("Number of tweets for movie:"+movie.getTitle()+" is "+tweetCounter);
				System.out.println("Number of Positive words of tweets for movie:"+movie.getTitle()+" is "+positiveCounter);
				System.out.println("Number of Negative words of tweets for movie:"+movie.getTitle()+" is "+negativeCounter);
				System.out.println("Sentiment for movie:"+movie.getTitle()+ "is :" +sentiment);
				
				movie.setSentiment(sentiment);
			}
			
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private boolean validateValue(String value) {
		boolean valueValid = false;
		if (value != null && !value.isEmpty()) {
			valueValid = true;
		}
		return valueValid;
	}
	
	private boolean validateDocument(Document document) {
		boolean docValid = false;
		docValid = (validateValue(document.getString("username")) && validateValue(document.getString("text"))
				&& validateValue(document.getString("date")));

		return docValid;
	}

}
