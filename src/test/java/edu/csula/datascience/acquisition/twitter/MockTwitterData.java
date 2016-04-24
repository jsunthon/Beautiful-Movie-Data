package edu.csula.datascience.acquisition.twitter;

/**
 * Mock raw twitter data
 */
public class MockTwitterData {
    private final long id;
    private final String content;

    public MockTwitterData(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
