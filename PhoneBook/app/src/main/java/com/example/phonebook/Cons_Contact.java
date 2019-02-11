package com.example.phonebook;

public class Cons_Contact {
    private String Name;
    private String Phone;
    private int Photo;

    public Cons_Contact() {
    }

    public Cons_Contact(String name, String phone, int photo) {
        Name = name;
        Phone = phone;
        Photo = photo;
    }
    public String getName() {
        return Name;
    }
    public String getPhone() {
        return Phone;
    }
    public int getPhoto() {
        return Photo;
    }
    public void setName(String name) {
        Name = name;
    }
    public void setPhone(String phone) {
        Phone = phone;
    }
    public void setPhoto(int photo) {
        Photo = photo;
    }
}
