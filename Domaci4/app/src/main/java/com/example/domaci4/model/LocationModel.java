package com.example.domaci4.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

public class LocationModel {

        @Exclude
        private String mId;
        private String mName;
        private String date;
        private String opis;
        private double longtitue;
        private double latitude;




        public LocationModel() {

        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        @Exclude
        public String getId() {
            return mId;
        }

        @Exclude
        public void setId(String id) {
            mId = id;
        }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public double getLongtitue() {
        return longtitue;
    }

    public void setLongtitue(double longtitue) {
        this.longtitue = longtitue;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
