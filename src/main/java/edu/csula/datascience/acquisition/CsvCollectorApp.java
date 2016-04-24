package edu.csula.datascience.acquisition;


import edu.csula.datascience.utilities.MongoUtilities;

import java.util.Collection;

public class CsvCollectorApp {
    public static void main(String[] args){
        //TODO add file
        String file = "test.csv";
        CsvSource source = new CsvSource(file);
        CsvCollector collector = new CsvCollector();

        while (source.hasNext()){
            Collection<Movie> movies = source.next();
            Collection<Movie> mungedMovies = collector.mungee(movies);
            collector.save(mungedMovies);
        }

        MongoUtilities mongoUtil = new MongoUtilities("movie-data", "csv_files");
        mongoUtil.printDocuments();

    }
}
