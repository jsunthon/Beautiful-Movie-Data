package edu.csula.datascience.acquisition;


import edu.csula.datascience.utilities.MongoUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvCollectorApp {
    public static void main(String[] args){
        //TODO add file
        String file = "test.csv";
        CsvSource source = new CsvSource(file, true);
        CsvCollector collector = new CsvCollector();

        while (source.hasNext()){
            Collection<Movie> movies = source.next();
            Collection<Movie> mungedMovies = collector.mungee(movies);
            if (isValidTitle(new ArrayList<Movie>(mungedMovies).get(0).getTitle())){
                collector.save(mungedMovies);
            }
            else{
                continue;
            }

        }

        MongoUtilities mongoUtil = new MongoUtilities("movie-data", "csv_files");
        mongoUtil.printDocuments();


    }

    //regex from StackOverflow
    private static boolean isValidTitle(String title){
        boolean valid = title.matches("^[a-zA-Z0-9_ ]*$");

        return valid;
    }
}
