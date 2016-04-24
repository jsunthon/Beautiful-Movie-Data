package edu.csula.datascience.acquisition;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
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

    //Combines ratings for the same movie into one entry
    public Collection<Movie> mungee(Collection<Movie> src){
        ArrayList<Movie> srcArray = new ArrayList(src);
        ArrayList<Movie> finalMovieEntry = new ArrayList();
        double averageRating = 0.0;
        String title = "";
        int mid = 0;
        Movie theMovie = new Movie();
        for (Movie movie : srcArray){
            mid = movie.getId();
            title = movie.getTitle();
            averageRating += movie.getRating();
        }
        averageRating =  averageRating/(double)src.size();
        theMovie.setId(mid);
        theMovie.setTitle(title);
        theMovie.setRating(averageRating);
        finalMovieEntry.add(theMovie);
        return finalMovieEntry;
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
