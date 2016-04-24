package edu.csula.datascience.acquisition.csv;


import java.util.Collection;

public class CsvCollectorApp {
    public static void main(String[] args){
        //TODO add file
        String file = "test.csv";
        CsvSource source = new CsvSource(file);
        CsvCollector collector = new CsvCollector();


        while (source.hasNext()){
            Collection<String> movies = source.next();
            Collection<String> mungedMovies = collector.mungee(movies);
            collector.save(mungedMovies);
        }

    }
}
