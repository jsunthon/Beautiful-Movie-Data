package edu.csula.datascience.acquisition;

import java.util.Collection;

/**
 * Created by SteveShim on 4/20/2016.
 */
public class CsvSource implements Source<String>{
    public CsvSource(){

    }

    public boolean hasNext(){
        return true;
    }

    public Collection<String> next(){

        return null;
    }
}
