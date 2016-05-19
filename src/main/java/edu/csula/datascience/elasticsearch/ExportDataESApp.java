package edu.csula.datascience.elasticsearch;

public class ExportDataESApp {

	public static void main(String[] args) {
		TweetExporter tweetExp = new TweetExporter("darkserith");
		tweetExp.exportTweets();
	}	
}
