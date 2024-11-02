package com.comicbookreader.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Userdata {
    public boolean hadInitialScan;

    public boolean getHadInitialScan(){
        return hadInitialScan;
    }

    public void setHadInitialScan(boolean hadInitialScan) {
        this.hadInitialScan = hadInitialScan;
    }

}
