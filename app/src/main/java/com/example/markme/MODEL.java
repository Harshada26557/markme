package com.example.markme;
public class MODEL {

    public String id;
    public String name;
    public String email;
    public String subject;
    public String phone;
    public String password;


    public MODEL() {}

    public MODEL(String name, String email, String subject, String phone, String password) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.phone = phone;
        this.password = password;
    }
}
