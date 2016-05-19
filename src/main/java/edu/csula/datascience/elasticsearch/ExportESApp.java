package edu.csula.datascience.elasticsearch;

public class ExportESApp {

	public static void main(String[] args) {
		
		//pass in your cluster name to the constructor
		TweetExporter tweetExp = new TweetExporter("darkserith");
		tweetExp.exportToES();
		MovieExporter movieExp = new MovieExporter("darkserith");
		movieExp.exportToES();
	}	
}
