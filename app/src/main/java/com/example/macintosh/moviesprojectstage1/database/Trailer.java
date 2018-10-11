package com.example.macintosh.moviesprojectstage1.database;

public class Trailer {
    private int id;
    private String name;
    private String key;
    private String type;

    public Trailer(int id, String name, String key, String type) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }
}
