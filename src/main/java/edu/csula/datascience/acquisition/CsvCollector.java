package edu.csula.datascience.acquisition;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Collection;


public class CsvCollector implements Collector<String, String>{
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

    public Collection<String> mungee(Collection<String> src){
        return src;
    }

    public void save(Collection<String> data){

    }
}
