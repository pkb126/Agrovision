package com.praveenbhati.agrovision.model;

/**
 * Created by Bhati on 12/12/2015.
 */
public class Taluka {
    int talukaId,DistrictId;
    String talukaName;

    public Taluka() {
    }

    public int getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(int talukaId) {
        this.talukaId = talukaId;
    }

    public int getDistrictId() {
        return DistrictId;
    }

    public void setDistrictId(int districtId) {
        DistrictId = districtId;
    }

    public String getTalukaName() {
        return talukaName;
    }

    public void setTalukaName(String talukaName) {
        this.talukaName = talukaName;
    }
}
