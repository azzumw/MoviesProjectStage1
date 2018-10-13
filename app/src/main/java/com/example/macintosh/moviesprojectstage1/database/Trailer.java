package com.example.macintosh.moviesprojectstage1.database;

public class Trailer {
    private String id;
    private String name;
    private String key;
    private String type;

    public Trailer(String id, String name, String key, String type) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
    }

    public String getId() {
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
