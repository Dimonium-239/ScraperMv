package com.gmail.dymitr.kuzmin;

public class Gramophone extends DBElement {

    private String model;

    Gramophone(){}

    Gramophone(String URL, int price, String model){
        setURL(URL);
        this.model = model;
        setPrice(price);
    }

    String getModel(){ return model; }

    void setModel(String model){ this.model = model; }

}
