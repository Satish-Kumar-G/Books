package com.in.ac.srit.books.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

@Database(entities = Favorites.class,version = 1,exportSchema = false)
public abstract class FavoritesDatabase extends RoomDatabase {
    public abstract FavoritesDao myDaoClass();

    private static FavoritesDatabase database;

    static FavoritesDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized ((FavoritesDatabase.class)) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(), FavoritesDatabase.class, "BooksDB.db").fallbackToDestructiveMigration().allowMainThreadQueries().addCallback(callback).build();
                }
            }
        }
        return database;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {


        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(database).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, LiveData<List<Favorites>>> {
        private final FavoritesDao myDaoClass;

        public PopulateDbAsync(FavoritesDatabase databaseClass) {
            myDaoClass = databaseClass.myDaoClass();
        }

        @Override
        protected LiveData<List<Favorites>> doInBackground(Void... voids) {
            return myDaoClass.getAllBooks();
        }
    }
}