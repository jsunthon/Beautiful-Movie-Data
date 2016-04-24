package edu.csula.datascience.acquisition.twitter;

/**
 * A simple model for testing
 */
public class TwitterModel {
	private final long id;
	private String text;

	public TwitterModel(long id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	public static TwitterModel build(MockTwitterData data) {
		return new TwitterModel(data.getId(), data.getContent());
	}
	
	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwitterModel other = (TwitterModel) obj;
		
		if (this.text.equals(other.text)) {			
			return true;
		}		
		return false;
	}
}
