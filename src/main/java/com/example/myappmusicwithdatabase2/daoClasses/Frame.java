package com.example.myappmusicwithdatabase2.daoClasses;

public class Frame {

    private int id;

    private Integer height;
    private Integer width;

    public Frame(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }

    public Frame(Integer id, Integer height, Integer width) {
        this(height, width);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

}
