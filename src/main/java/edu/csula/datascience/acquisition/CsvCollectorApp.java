package edu.csula.datascience.acquisition;


import java.io.File;
import java.util.Collection;

public class CsvCollectorApp {
    public static void main(String[] args){
        //TODO add file
        File file = null;
        CsvSource source = new CsvSource(file);
        CsvCollector collector = new CsvCollector();

        while (source.hasNext()){
            Collection<String> movies = source.next();
            Collection<String> mungedMovies = collector.mungee(movies);
            collector.save(mungedMovies);
        }

    }
}
