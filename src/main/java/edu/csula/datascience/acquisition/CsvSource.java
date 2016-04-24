package edu.csula.datascience.acquisition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;


public class CsvSource implements Source<Movie>{
    private BufferedReader br1, br2;
    private final String DELIMITER = ",";
    private String line;

    public CsvSource(String fileName){
        try {
            File file = new File(fileName);
            br1 = new BufferedReader(new FileReader(file));
            br2 = new BufferedReader(new FileReader(file));

            //skips header of csv file
            line = br1.readLine();
            line = br2.readLine();
            //puts br2 1 line ahead;
            line = br2.readLine();
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
        String[] tokens, nextTokens;
        try {
            do{
                line = br1.readLine();
                Movie tempMovie = new Movie();
                tokens = line.split(DELIMITER);
                tempMovie.setId(Integer.parseInt(tokens[0]));
                tempMovie.setTitle(tokens[1]);
                tempMovie.setRating(Double.parseDouble(tokens[4]));

                movies.add(tempMovie);

                //check if next entry
                line = br2.readLine();
                nextTokens = line.split(DELIMITER);
                if (nextTokens[0].equals(tokens[0])){
                    break;
                }
            } while(hasNext());
        } catch (Exception e){
            e.printStackTrace();
        }

        return movies;
    }
}
