package edu.csula.datascience.acquisition;

public class Movie {
    private int id;
    private String title, hashtagTitle;
    private double rating;

    public Movie(){
    }

    public Movie(int movieID, String movieTitle){
        id = movieID;
        title = movieTitle;
        hashtagTitle = "#" + title.replaceAll("\\s","");
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHashtagTitle() {
        return hashtagTitle;
    }

    public void setHashtagTitle(String hashtagTitle) {
        this.hashtagTitle = hashtagTitle;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;
        return getId() == movie.getId();
    }
}
