package com.example.rub.functionalities.locations;

public class City extends Locality{
    private String cityString;

    public City(String cityString){
        this.cityString = cityString;
    }

    public String getCityString() {
        return cityString;
    }

    public void setCityString(String cityString) {
        this.cityString = cityString;
    }

    @Override
    public String toString(){
        return cityString;
    }
}
