package com.spaceo.cardviewtutorial.bean;

/**
 * Created by sotsys-219 on 16/9/16.
 */
public class Detail {

    private String name;
    private String emailId;

    public Detail(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
