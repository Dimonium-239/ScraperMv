package com.gmail.dymitr.kuzmin;

public abstract class DBElement extends Object {

    private String URL;

    private int price;

    void setURL(String URL){ this.URL = URL; }

    String getURL(){ return URL; }

    void setPrice(int price){ this.price = price; }

    int getPrice(){ return price; }

}
