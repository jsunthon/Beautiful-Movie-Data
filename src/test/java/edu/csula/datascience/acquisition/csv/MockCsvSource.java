package edu.csula.datascience.acquisition.csv;

import com.google.common.collect.Lists;

import edu.csula.datascience.acquisition.csv.Source;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * A mock source to provide data
 */
public class MockCsvSource implements Source<MockCsvData> {
    int index = 7;
    
    @Override
    public boolean hasNext() {
    	index--;
        return index >= 1;
    }
    
    //simulate the csv data
    @Override
    public Collection<MockCsvData> next() {
        return Lists.newArrayList(
            new MockCsvData(1, "Toy Story (1995)", 4, "1137894004"),
            new MockCsvData(1, "Toy Story (1995)", 5, "944991567"),
            new MockCsvData(2, "Force Awakens (2016)", 4, "421421"),
            new MockCsvData(3, "Guardians of the Galaxy (2015)", 2, "112421"),
            new MockCsvData(3, "Guardians of the Galaxy (2015)", 5, "515"),
            new MockCsvData(4, "testiÃ±g (3000)", 4, "41241241"),
            new MockCsvData(5, "we are (cool) (2000)", 3, "1232321")
        );
    }
}
