package edu.csula.datascience.elasticsearch;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.bson.Document;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.node.Node;
import com.google.gson.Gson;
import com.mongodb.client.MongoCursor;
import edu.csula.datascience.utilities.MongoUtilities;

public class MovieExporter {
	private final static String indexName = "beautiful-movie-team-data";
	private final static String typeName = "movies";
	private Node node;
	private Client client;
	private BulkProcessor bulkProcessor;

	public MovieExporter(String clusterName) {
		this.node = nodeBuilder()
				.settings(Settings.builder().put("cluster.name", clusterName).put("path.home", "elasticsearch-data"))
				.node();
		this.client = this.node.client();
	}

	public void exportMovies() {
		MongoUtilities mongo = new MongoUtilities("movie-data", "csv_files");
		MongoCursor<Document> cursor = mongo.getCollection().find().iterator();
		bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
			@Override
			public void beforeBulk(long executionId, BulkRequest request) {
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
				System.out.println("Facing error while importing data to elastic search");
				failure.printStackTrace();
			}
		}).setBulkActions(400).setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
				.setFlushInterval(TimeValue.timeValueSeconds(5)).setConcurrentRequests(1)
				.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)).build();

		int counter = 0;

		while (cursor.hasNext() && counter < 400) {
			Document document = cursor.next();
			if (validateDocument(document)) {
				Movie movie = new Movie(document.getInteger("movieID"), document.getString("title"),
						document.getDouble("rating"), document.getInteger("year"));
				insertObjAsJson(movie);
				System.out.println("Movie #: " + ++counter + " inserted into elastic search.");
			}
		}
	}

	public void insertObjAsJson(Movie movie) {
		bulkProcessor.add(new IndexRequest(indexName, typeName).source(new Gson().toJson(movie)));
	}

	class Movie {
		final int movieId;
		final String title;
		final double rating;
		final int year;

		public Movie(int movieId, String title, double rating, int year) {
			this.movieId = movieId;
			this.title = title.split(" ")[0]; // titles were stored as "title "
												// in mongo
			this.rating = rating;
			this.year = year;
		}
	}

	public boolean validateDocument(Document document) {
		boolean docValid = false;
		docValid = validateValue(document.getString("title"));
		return docValid;
	}

	public boolean validateValue(String value) {
		boolean valueValid = false;
		if (value != null && !value.isEmpty()) {
			valueValid = true;
		}
		return valueValid;
	}

}
