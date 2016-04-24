package edu.csula.datascience.acquisition;

public class TwitterResponse {
	private long id;
	private int favCt;
	private int retwtCt;
	private String userName;
	private String text;
	private String lang;
	private String source;

	public TwitterResponse(long id, int favCt, int retwtCt, String userName, String text, String lang, String source) {
		super();
		this.id = id;
		this.favCt = favCt;
		this.retwtCt = retwtCt;
		this.userName = userName;
		this.text = text;
		this.lang = lang;
		this.source = source;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the favCt
	 */
	public int getFavCt() {
		return favCt;
	}

	/**
	 * @return the retwtCt
	 */
	public int getRetwtCt() {
		return retwtCt;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwitterResponse other = (TwitterResponse) obj;
		
		if (this.text.equals(other.text)) {
			return true;
		}		
		return false;
	}

}
