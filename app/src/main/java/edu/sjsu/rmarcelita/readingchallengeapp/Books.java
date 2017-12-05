package edu.sjsu.rmarcelita.readingchallengeapp;

/**
 * Created by riri on 12/4/17.
 */

public class Books {

    private String title;
    private String author;
    private String cover;
    private String genre;
    private int pages;
    private String reviews;
    private double stars;

    public Books() {
        title = "";
        author = "";
        cover = "";
        genre = "";
        pages = 0;
        reviews = "";
        stars = 0.00;
    }

    public Books(String bookTitle, String bookAuthor,
                 String bookGenre, String bookCover, int bookPages, double bookStars) {
       title  = bookTitle;
       author = bookAuthor;
       genre = bookGenre;
       cover = bookCover;
       pages = bookPages;
       stars = bookStars;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCover() {
        return cover;
    }

    public int getPages() {
        return pages;
    }

    public String getGenre() {
        return genre;
    }

    public double getStars() {
        return stars;
    }

    public void setReviews(String bookReviews) {
        reviews = bookReviews;
    }

    public String getReviews() {
        return reviews;
    }
}
