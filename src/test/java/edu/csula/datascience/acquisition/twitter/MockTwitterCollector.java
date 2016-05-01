package edu.csula.datascience.acquisition.twitter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import edu.csula.datascience.acquisition.Collector;
import edu.csula.datascience.acquisition.model.TwitterResponse;

/**
 * A mock implementation of collector for testing
 */
public class MockTwitterCollector implements Collector<TwitterModel, MockTwitterData> {
    @Override
    public Collection<TwitterModel> mungee(Collection<MockTwitterData> src) {
        // in your example, you might need to check src.hasNext() first
        Collection<TwitterModel> newSrc = src
            .stream()
            .filter(data -> data.getText() != null)
            .map(TwitterModel::build)
            .collect(Collectors.toList());
        
        Set<TwitterModel> cleanTexts = new HashSet<TwitterModel>();
        String []query = {"sunny"};
		for(TwitterModel tr: newSrc){
			for(int i=0; i< query.length; i++)
			if(!tr.getText().contains("@"+query[i]))
			{
				cleanTexts.add(tr);
			}
		}
        Set<TwitterModel> noDups = new HashSet<TwitterModel>(cleanTexts);
		return noDups;
    }

    @Override
    public void save(Collection<TwitterModel> data) {
    }
}
