package edu.csula.datascience.acquisition.elasticsearch;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;


import edu.csula.datascience.elasticsearch.model.Movie;

public class TweetExporterTest {
	
	private List<String> movies = new ArrayList<>();
	private List<String> tweets = new ArrayList<>();
	@Test
	public void vaidateSentimentTxtFile() {
		List<String> positiveWords = new ArrayList<>();
		List<String> negativeWords = new ArrayList<>();
		
		
		try {
			positiveWords = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("positive-words.txt").toURI()));
			negativeWords = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("negative-words.txt").toURI()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		assertTrue(positiveWords.size() > 0 && negativeWords.size() > 0);
		assertEquals("a+", positiveWords.get(0));
		assertEquals("2-faced", negativeWords.get(0));
	}
	public void addSentimentForMovie()
	{

		 try {
			List<String> positiveWords = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("positive-words.txt").toURI()));
			List<String> negativeWords = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("negative-words.txt").toURI()));
		
			for( movie: movies)
			{
				int positiveCounter = 0;
				int negativeCounter = 0;
				int tweetCounter = 0;
				for(Tweet tweet: tweets){
					if(tweet.title.equals(movie.getTitle()))
					{
						tweetCounter++;
						for(String positiveWord: positiveWords)
						{
							if(tweet.text.contains(positiveWord));
							positiveCounter++;
						}
						for(String negativeWord: negativeWords)
						{
							if(tweet.text.contains(negativeWord));
							negativeCounter++;
						}
					}	
				}
				int sentiment = (positiveCounter - negativeCounter) / tweetCounter;
				System.out.println("Sentiment for movie:"+movie.getTitle()+ "is :" +sentiment);
				movie.setSentiment(sentiment);
			}
			
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
