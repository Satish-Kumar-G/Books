package com.in.ac.srit.books.Room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
@Entity
public class Favorites {
    public Favorites()
    {

    }
    public Favorites(@NonNull String id, String description, String imageurl, String title, String publisher_name, String usersRating) {
        this.id = id;
        this.description = description;
        this.imageurl = imageurl;
        this.title = title;
        this.publisher_name = publisher_name;
        UsersRating = usersRating;
    }

    @NonNull
    @PrimaryKey
    private String id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    private String description;
    private String imageurl;
    private String title;
    private String publisher_name;
    private String UsersRating;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getUsersRating() {
        return UsersRating;
    }

    public void setUsersRating(String usersRating) {
        UsersRating = usersRating;
    }


}
