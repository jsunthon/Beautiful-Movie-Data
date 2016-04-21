package edu.csula.datascience.acquisition;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import twitter4j.Status;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TwitterCollector implements Collector<Status, Status> {
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;
    public TwitterCollector() {
        // establish database connection to MongoDB
        mongoClient = new MongoClient();

        database = mongoClient.getDatabase("movie-data");

        // select collection by name `tweets`
        collection = database.getCollection("tweets");
    }
    @Override
    public Collection<Status> mungee(Collection<Status> src) {
        return src;
    }

    @Override
    public void save(Collection<Status> data) {
        List<Document> documents = data.stream()
            .map(item -> new Document()
                .append("tweetId", item.getId())
                .append("username", item.getUser().getName())
                .append("text", item.getText())
                .append("lang", item.getLang())
                .append("source", item.getSource()))
            .collect(Collectors.toList());

        collection.insertMany(documents);
    }
}
