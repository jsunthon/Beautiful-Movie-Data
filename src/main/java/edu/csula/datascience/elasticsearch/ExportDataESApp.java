package edu.csula.datascience.elasticsearch;

public class ExportDataESApp {

	public static void main(String[] args) {
		//throws out of memory error
//		TweetExporter tweetExp = new TweetExporter("darkserith");
//		tweetExp.exportTweets();
		MovieExporter movieExp = new MovieExporter("darkserith");
		movieExp.exportMovies();

	}	
}
