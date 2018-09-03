package com.in.ac.srit.books.Room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavoritesRepository {
    private FavoritesDao myDaoClass;
    LiveData<List<Favorites>> favList;
    public FavoritesRepository(Application application){
        FavoritesDatabase myDatabaseClass=FavoritesDatabase.getDatabase(application);
        myDaoClass=myDatabaseClass.myDaoClass();
        favList=myDaoClass.getAllBooks();
    }
    public LiveData<List<Favorites>> getFavList(){
        return  favList;
    }
    public void insert(Favorites favouritesClass){
        new insertAsyncTask(myDaoClass).execute(favouritesClass);

    }
    public void delete(Favorites favouritesClass){
        new deleteAsyncTask(myDaoClass).execute(favouritesClass);

    }
    public Favorites checking(String id){
        Favorites fav=myDaoClass.checking(id);
        return fav;

    }
    private class insertAsyncTask extends AsyncTask<Favorites,Void,Void> {
        private FavoritesDao daoClass;
        public insertAsyncTask(FavoritesDao myDaoClass){
            daoClass= myDaoClass;
        }
        @Override
        protected Void doInBackground(Favorites... favouritesClasses) {
            daoClass.insertIntoDatabase(favouritesClasses[0]);

            return null;
        }
    }
    private class deleteAsyncTask extends AsyncTask<Favorites,Void,Void>{
        private FavoritesDao delDao;

        public deleteAsyncTask(FavoritesDao del){
            delDao=del;
        }

        @Override
        protected Void doInBackground(Favorites... favouritesClasses) {
            delDao.deleteFromDatabase(favouritesClasses[0]);
            return null;
        }
    }

}
