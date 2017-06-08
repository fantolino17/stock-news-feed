package com.example.frank.stocknews;

/**
 * Created by Frank on 12/27/2016.
 */

public class FeedEntry {
    private String title;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String toString(){
        return "\nTitle: " + getTitle() +
                "\nLink: " + getLink();
    }


}
