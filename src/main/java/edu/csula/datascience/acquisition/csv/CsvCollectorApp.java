package edu.csula.datascience.acquisition.csv;

import edu.csula.datascience.acquisition.Movie;
import edu.csula.datascience.utilities.MongoUtilities;

import java.util.ArrayList;
import java.util.Collection;

public class CsvCollectorApp {
    public static void main(String[] args){
        //TODO add file
        String file = "test.csv";
        CsvSource source = new CsvSource(file, true);
        CsvCollector collector = new CsvCollector();

        while (source.hasNext()){
            Collection<Movie> movies = source.next();
            Collection<Movie> mungedMovies = collector.mungee(movies);
            ArrayList<Movie> munged = new ArrayList(mungedMovies);
            if (!mungedMovies.isEmpty() && munged.get(0).getYear() > 1990){
                collector.save(mungedMovies);
            }
            else{
                continue;
            }

        }

        MongoUtilities mongoUtil = new MongoUtilities("movie-data", "csv_files");
        mongoUtil.printDocuments();

    }
}
