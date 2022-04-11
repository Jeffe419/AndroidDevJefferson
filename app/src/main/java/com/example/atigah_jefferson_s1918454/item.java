package com.example.atigah_jefferson_s1918454;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class item  {

    private String title;
    private String description;
    private String link;
    private String georss_point;
    private String author;
    private String comments;
    private String pubDate;




    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink(){
        return link;
    }

    public String getGeorss_point(){
        return  georss_point;
    }

    public  String getAuthor(){
        return author;

    }

    public String getComments(){
        return  comments;
    }
    public String getPubDate(){
        return pubDate;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setGeorss_point(String georss_point) {
        this.georss_point = georss_point;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }


    @Override
    public String toString() {
        return "item{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", georss_point='" + georss_point + '\'' +
                ", author='" + author + '\'' +
                ", comments='" + comments + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }


}
