package com.zareinaflameniano.sample.data;

import androidx.annotation.NonNull;

public class FavoritesModel {

    public int favID;
    public String favImage ;
    public String favName;

    public FavoritesModel(int favID, String favName, String favImage){
        this.favID = favID;
        this.favName=favName;
        this.favImage = favImage;
    }
    @NonNull
    @Override
    public String toString() {
        String s = favName;
        return s;
    }

}
