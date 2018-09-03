package com.in.ac.srit.books.Room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    private FavoritesRepository repository;
    private Favorites favouritesClass;
    private LiveData<List<Favorites>> listData;
    public FavoritesViewModel(@NonNull Application application) {

        super(application);
        repository=new FavoritesRepository(application);
        listData=repository.getFavList();
    }

    public Favorites chekingDb(String id) {
        favouritesClass=repository.checking(id);
        return favouritesClass;
    }
    public LiveData<List<Favorites>> getList(){
        return listData;

    }
    public void insertData(Favorites favouritesClass){
        repository.insert(favouritesClass);
    }
    public void deleteData(Favorites favouritesClass){
        repository.delete(favouritesClass);
    }
}