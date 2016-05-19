package edu.csula.datascience.elasticsearch;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import org.bson.Document;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import com.google.gson.Gson;
import com.mongodb.client.MongoCursor;
import edu.csula.datascience.utilities.MongoUtilities;

public class TweetExporter extends Exporter {
	private final static String indexName = "beautiful-movie-team-data";
	private final static String typeName = "tweets";

	public TweetExporter(String clusterName) {
		super(clusterName);
	}

	@Override
	public void exportToES() {
		MongoUtilities mongo = new MongoUtilities("movie-data", "tweets");
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
		}).setBulkActions(10000).setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
				.setFlushInterval(TimeValue.timeValueSeconds(5)).setConcurrentRequests(1)
				.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)).build();

		while (cursor.hasNext()) {
			Document document = cursor.next();
			if (validateDocument(document)) {
				Tweet tweet = new Tweet(document.getString("username"), document.getString("text"),
						document.getInteger("favCt"), document.getInteger("retwtCt"), document.getString("date"),
						document.getString("source"));
				insertObjAsJson(tweet);
			}
		}
	}

	@Override
	public void insertObjAsJson(Object object) {
		if (object != null && object instanceof Tweet) {
			Tweet tweet = (Tweet) object;
			bulkProcessor.add(new IndexRequest(indexName, typeName).source(new Gson().toJson(tweet)));
			System.out.println("Tweet record inserted into elastic search.");
		}
	}

	@Override
	public boolean validateDocument(Document document) {
		boolean docValid = false;
		docValid = (validateValue(document.getString("username")) && validateValue(document.getString("text"))
				&& validateValue(document.getString("date")) && validateValue(document.getString("source")));

		return docValid;
	}

	private class Tweet {
		final String user;
		final String text;
		final int favCt;
		final int retweetCt;
		final String date;
		final String source;

		public Tweet(String user, String text, int favCt, int retweetCt, String date, String source) {
			this.user = user;
			this.text = text;
			this.favCt = favCt;
			this.retweetCt = retweetCt;
			// Need parseDate to convert to a format of 'YY-MM-DD' for elastic
			// search
			this.date = parseDate(date);
			this.source = source;
		}

		public String parseDate(String date) {
			Locale dateLocale = Locale.US;
			DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy", dateLocale);
			TemporalAccessor tempDate = inFormatter.parse(date);
			DateTimeFormatter outFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
			return outFormatter.format(tempDate);
		}
	}
}