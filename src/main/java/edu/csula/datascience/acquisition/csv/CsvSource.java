package edu.csula.datascience.acquisition.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import au.com.bytecode.opencsv.CSV;


import au.com.bytecode.opencsv.CSVReader;
import edu.csula.datascience.acquisition.Movie;


public class CsvSource implements Source<Movie>{
    private CSVReader cr1, cr2;
    private final String DELIMITER = ",";
    private String line[];

    public CsvSource(String fileName, boolean hasHeader){
        try {
            File file = new File(ClassLoader.getSystemResource(fileName).toURI());
            cr1 = new CSVReader(new FileReader(file));
            cr2 = new CSVReader(new FileReader(file));

            //skips header of csv file
            if (hasHeader) {
                line = cr1.readNext();
                line = cr2.readNext();
            }
            //puts br2 1 line ahead;
            line = cr2.readNext();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean hasNext(){

        if (line != null){
            return true;
        } else {
            return false;
        }
    }

    public Collection<Movie> next(){
        ArrayList<Movie> movies = new ArrayList();
        int counter = 0;
        String[] tokens = line;
        double rating = 0.0;
        try {
            do{
                counter++;
                line = cr1.readNext();
                try {
                    rating += Double.parseDouble(line[4]);
                } catch(Exception e){
                }
                tokens = line;

                //check if next entry
                line = cr2.readNext();
                if (line == null || !line[0].equals(tokens[0])){
                    break;
                }
            } while(hasNext());
        } catch (Exception e){
            e.printStackTrace();
        }
        Movie tempMovie = new Movie();
        tempMovie.setId(Integer.parseInt(tokens[0]));
        tempMovie.setTitle(tokens[1]);
        tempMovie.setRating(rating/(double)counter);
        movies.add(tempMovie);
        return movies;
    }
}
