package com.kobid.mygreatapp;

public class City {
    String eng_name;

    public String getEng_name() {
        return eng_name;
    }

    public void setEng_name(String eng_name) {
        this.eng_name = eng_name;
    }

    @Override
    public String toString() {

        return eng_name;
    }
}