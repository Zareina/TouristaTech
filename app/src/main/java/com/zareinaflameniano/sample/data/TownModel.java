package com.zareinaflameniano.sample.data;

import androidx.annotation.NonNull;

public class TownModel {


    public int townID;
    public String townName;


    public TownModel(int townID, String townName){
        this.townID = townID;
        this.townName=townName;
    }
    @NonNull
    @Override
    public String toString() {
        String s = townName;
        return s;
    }

    public int getTownID(){
        return  townID;
    }
}