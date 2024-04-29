package com.example.myappmusicwithdatabase2.daoClasses;


public class Song {

    private Integer id;
    private String path;

    public Song(String path) {
        this.path = path;
    }

    public Song(Integer id, String path) {
        this(path);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Song: " + id + ", path: " + path;
    }

}