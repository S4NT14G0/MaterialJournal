package com.developer.android.sroig.materialjournal.models;

import java.util.Calendar;

/**
 * Created by Santiago Roig on 11/5/2015.
 */
public class JournalItem {

    private long id;
    private String title;
    private String text;
    //private Bitmap image;
    private Calendar date;
    private String location;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    private String tags;


    //public  Bitmap getImage() {
    //    return  image;
    //}

    //public void setImage(Bitmap image) {
    //    this.image = image;
    //}

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
