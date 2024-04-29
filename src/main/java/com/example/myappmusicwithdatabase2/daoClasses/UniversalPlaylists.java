package com.example.myappmusicwithdatabase2.daoClasses;


public class UniversalPlaylists {

    private Integer id;

    private Integer songId;

    public UniversalPlaylists(Integer songId) {
        this.songId = songId;
    }

    public UniversalPlaylists(Integer id, Integer songId) {
        this(songId);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

}