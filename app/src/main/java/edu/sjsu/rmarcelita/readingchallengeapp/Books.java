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
    private String synopsis;
    private double stars;

    public Books() {
        title = "";
        author = "";
        cover = "";
        genre = "";
        pages = 0;
        synopsis = "";
        stars = 0.00;
    }

    public Books(String bookTitle, String bookAuthor, String bookGenre,
                 String bookSynopsis, String bookCover, int bookPages, double bookStars) {
       title  = bookTitle;
       author = bookAuthor;
       genre = bookGenre;
       synopsis = bookSynopsis;
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

    public String getSynopsis() {
        return synopsis;
    }
}
