package com.example.rub.functionalities.locations;

public class City extends Locality{
    public City(String city){
        super(city);
    }
    @Override
    public String toString(){
        return getLocalityName();
    }

    @Override
    public Locality get(){
        return (City) this;
    }
}
