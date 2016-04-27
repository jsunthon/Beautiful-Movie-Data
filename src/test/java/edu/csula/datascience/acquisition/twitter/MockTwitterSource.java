package edu.csula.datascience.acquisition.twitter;

import com.google.common.collect.Lists;

import edu.csula.datascience.acquisition.Source;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * A mock source to provide data
 */
public class MockTwitterSource implements Source<MockTwitterData> {
    int index = 0;
    private final String text = "The Burning Votive Candle https://t.co/PFYABgr2Ms #candle #votivecandle";

    @Override
    public boolean hasNext() {
        return index < 1;
    }

    @Override
    public Collection<MockTwitterData> next() {
        return Lists.newArrayList(
            new MockTwitterData(1234, text),
            new MockTwitterData(1235, "hello"),
            new MockTwitterData(1236, text),
            new MockTwitterData(1236, text),
            new MockTwitterData(1234, "nope!")
        );
    }
}
