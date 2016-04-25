package edu.csula.datascience.acquisition.csv;

import com.google.common.collect.Lists;

import edu.csula.datascience.acquisition.csv.Collector;
import edu.csula.datascience.acquisition.csv.Source;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * A test case to show how to use Collector and Source
 */
public class CollectorTest {
    private Collector<MovieModel, MockCsvData> collector;
    private Source<MockCsvData> source;
   

    @Before
    public void setup() {
        collector = new MockCsvCollector();
        source = new MockCsvSource();
    }

    @Test
    public void mungee() throws Exception {
    	
    	Collection<MovieModel> list = collector.mungee(source.next());
//        List<MovieModel> expectedList = Lists.newArrayList(
//            new MovieModel(1234, text)
//        );

        Assert.assertEquals(3, list.size());

//        for (int i = 0; i < 2; i ++) {
////            Assert.assertEquals(list.get(i).getId(), expectedList.get(i).getId());
//            Assert.assertEquals(list.get(i).getText(), expectedList.get(i).getText());
//        }
    }
}