package com.example.myappmusicwithdatabase2.daoClasses;


public class Playlist {

    private Integer id;
    private String name;
    private Integer x;
    private Integer y;

    public Playlist(String name, Integer x, Integer y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Playlist(Integer id, String name, Integer x, Integer y) {
        this(name, x, y);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

}
