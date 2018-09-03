package com.in.ac.srit.books.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoritesDao {
    @Query("SELECT * FROM Favorites")
    LiveData<List<Favorites>> getAllBooks();
    @Insert
    void insertIntoDatabase(Favorites favouritesClass);
    @Delete
    void deleteFromDatabase(Favorites favouritesClass);
    @Query("SELECT * FROM Favorites WHERE id=:id LIMIT 1")
    Favorites checking(String id);
}
