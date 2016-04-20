package edu.csula.datascience.acquisition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class CsvSource implements Source<String>{
    private BufferedReader fileReader;
    private final String DELIMITER = ",";
    private String line;

    public CsvSource(File file){
        try {
            fileReader = new BufferedReader(new FileReader(file));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean hasNext(){
        try {
            line = fileReader.readLine();
        } catch (Exception e){
            e.printStackTrace();
        }
        if (line != null){
            return true;
        } else {
            return false;
        }
    }

    public Collection<String> next(){
        String[] tokens = line.split(DELIMITER);
        ArrayList<String> list = new ArrayList(Arrays.asList(tokens));
        return list;
    }
}
