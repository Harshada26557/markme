package com.example.markme;

public class Announcement {

    public String id;
    public String title;
    public String description;
    public long timestamp;

    public Announcement() {
        // required for Firebase
    }

    public Announcement(String id, String title, String description, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }
}
