package com.example.markme;

public class MODEL {

    public String id;
    public String name;
    public String email;
    public String subject;
    public String phone;
    public String role;
    public String adminId;

    public MODEL() {}

    public MODEL(String name, String email, String subject, String phone, String role, String adminId) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.phone = phone;
        this.role = role;
        this.adminId = adminId;
    }
}
