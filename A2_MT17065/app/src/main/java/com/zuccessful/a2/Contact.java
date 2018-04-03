package com.zuccessful.a2;

import java.io.Serializable;
import java.util.ArrayList;

class Contact implements Serializable{
    private String id;
    private String title;
    private String image;
    private ArrayList<String> number;
    private ArrayList<String> email;

    public Contact(String id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.number = new ArrayList<>();
        this.email = new ArrayList<>();

    }

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email.add(email);
    }

    public void setNumber(String number) {
        this.number.add(number);
    }

    public ArrayList<String> getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
