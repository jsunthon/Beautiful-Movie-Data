package edu.csula.datascience.acquisition;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class CsvCollector implements Collector<Movie, Movie>{
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;
    public CsvCollector(){
        // establish database connection to MongoDB
        mongoClient = new MongoClient();

        database = mongoClient.getDatabase("movie-data");

        // select collection by name `csv_files`
        collection = database.getCollection("csv_files");
    }

    public Collection<Movie> mungee(Collection<Movie> src){
        return src;
    }

    public void save(Collection<Movie> data){
        List<Document> documents = data.stream()
                .map(item -> new Document()
                        .append("movieID", item.getId())
                        .append("title", item.getTitle())
                        .append("rating", item.getRating()))
                .collect(Collectors.toList());

        collection.insertMany(documents);

    }
}
