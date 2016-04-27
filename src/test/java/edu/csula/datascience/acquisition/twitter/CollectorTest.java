package edu.csula.datascience.acquisition.twitter;

import com.google.common.collect.Lists;

import edu.csula.datascience.acquisition.Collector;
import edu.csula.datascience.acquisition.Source;

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
    private Collector<TwitterModel, MockTwitterData> collector;
    private Source<MockTwitterData> source;
    private final String text = "The Burning Votive Candle https://t.co/PFYABgr2Ms #candle #votivecandle";

    @Before
    public void setup() {
        collector = new MockTwitterCollector();
        source = new MockTwitterSource();
    }

    @Test
    public void mungee() throws Exception {
    	
    	Collection<TwitterModel> list = collector.mungee(source.next());
        List<TwitterModel> expectedList = Lists.newArrayList(
            new TwitterModel(1234, text)
        );

        Assert.assertEquals(3, list.size());

//        for (int i = 0; i < 2; i ++) {
////            Assert.assertEquals(list.get(i).getId(), expectedList.get(i).getId());
//            Assert.assertEquals(list.get(i).getText(), expectedList.get(i).getText());
//        }
    }
}