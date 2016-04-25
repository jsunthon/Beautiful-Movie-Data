package edu.csula.datascience.acquisition.csv;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import edu.csula.datascience.acquisition.csv.Collector;

/**
 * A mock implementation of collector for testing
 */
public class MockCsvCollector implements Collector<MovieModel, MockCsvData> {
    @Override
    public Collection<MovieModel> mungee(Collection<MockCsvData> src) {
        // in your example, you might need to check src.hasNext() first
        Collection<MovieModel> newSrc = src
            .stream()
            .map(MovieModel::build)
            .collect(Collectors.toList());
        
        Set<MovieModel> noDups = new HashSet<MovieModel>(newSrc);
		return noDups;
    }

    @Override
    public void save(Collection<MovieModel> data) {
    }
    
    //regex helper methods from StackOverflow
    private static boolean isValidTitle(String title){
        boolean valid = (title.matches("^[a-zA-Z0-9_() ]*$") &&
                            (title != null) &&
                            (!title.replaceAll("\\s+","").isEmpty()));
        return valid;
    }

    private static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }
}
