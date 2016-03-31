package com.praveenbhati.agrovision.model;

/**
 * Created by Bhati on 12/12/2015.
 */
public class Village {
    int villageId,talukaId;
    String villageName;

    public Village() {
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public int getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(int talukaId) {
        this.talukaId = talukaId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
