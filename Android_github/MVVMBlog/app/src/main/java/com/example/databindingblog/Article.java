package com.example.databindingblog;

/**
 * Created by milan on 17.8.16.
 */
public class Article {

    private String title;
    private String excerpt;
    private boolean highlight;
    private String imageUrl;
    private int commentsNumber;
    private boolean read;

    public Article(String title, String excerpt, boolean highlight, String imageUrl, int commentsNumber) {
        this.title = title;
        this.excerpt = excerpt;
        this.highlight = highlight;
        this.imageUrl = imageUrl;
        this.commentsNumber = commentsNumber;
        this.read = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
