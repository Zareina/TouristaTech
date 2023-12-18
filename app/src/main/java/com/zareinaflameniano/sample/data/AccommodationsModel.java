package com.zareinaflameniano.sample.data;

import androidx.annotation.NonNull;

public class AccommodationsModel {

    public int accID;
    public int townID;
    public String accName;
    public String accLocation;
    public String accDescription;
    public String accImage;
    public int accFavorite;


    public AccommodationsModel(int accID,int townID, String accName, String accLocation, String accDescription, String accImage, int accFavorite){
        this.accID = accID;
        this.townID = townID;
        this.accName = accName;
        this.accLocation = accLocation;
        this.accDescription = accDescription;
        this.accImage = accImage;
        this.accFavorite = accFavorite;

    }
    @NonNull
    @Override
    public String toString() {
        String s = "" + accID + ": " + accName;
        return s;
    }
}
