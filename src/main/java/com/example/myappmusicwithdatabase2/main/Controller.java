package com.example.myappmusicwithdatabase2.main;


import com.example.myappmusicwithdatabase2.additionals.AdderSongsToDataBase;
import com.example.myappmusicwithdatabase2.daoClasses.Song;
import javafx.scene.media.MediaPlayer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

    private Model model = new Model();

    public void loadPlaylists(){
        //model.loadPlaylists();
    }

    public HashMap<String, HashMap<ArrayList<String>, ArrayList<MediaPlayer>>> getPlaylists(){
        return model.getPlaylists();
    }

    public String playSong(String playlistName) {
        return model.playSong(playlistName);
    }

    public boolean stopSong(String playlistName) {
        return model.stopSong(playlistName);
    }

    public void loadPlaylist() {
        model.loadPlaylist();
    }

    public String playPreviousSong(String playlistName) {
        return model.playPreviousSong(playlistName);
    }

    public String playNextSong(String playlistName) {
        return model.playNextSong(playlistName);
    }

    public void turnUpVolume() {
        model.turnUpVolume();
    }

    public void turnDownVolume() {
        model.turnDownVolume();
    }

    public String getCurrentSongName() {
        return model.getCurrentSongName();
    }
    public void setCurrentPlaylist(String playlistName) {
        model.setCurrentPlaylist(playlistName);
    }
    public void setOnEndingOfSong(String playlistName, MediaPlayer song){
        model.setOnEndingOfSong(playlistName, song);
    }
    public ArrayList<String> getNamesOfAllSongs() {
        return model.getNamesOfAllSongs();
    }

    public ArrayList<MediaPlayer> getAllMediaPlayers() {
        return model.getAllMediaPlayers();
    }

    public void playChoosenSong(MediaPlayer song, String nameOfPlaylistWhereIsSong){
        model.playChosenSong(song, nameOfPlaylistWhereIsSong);
    }

    public void setWayOfPlayingSongs(String wayOfPlaying){
        model.setWayOfPlayingSongs(wayOfPlaying);
    }
    public String[] addCompletelyNewSongs(String input){
        String output = AdderSongsToDataBase.addDoubleSlashes(input);
        String[] splitted = output.split(" C:");
        String[] finalSplitted = new String[splitted.length];
        for(int i = 0; i < splitted.length; i++){
            if(!splitted[i].equals(splitted[0])){
                finalSplitted[i] = "C:" + splitted[i];
            }else {
                finalSplitted[0] = splitted[0];
            }
        }

        for(String element : finalSplitted){
            System.out.println(element);
        };

        AdderSongsToDataBase.saveToSongDao(finalSplitted);
        return finalSplitted;
    }

    public static void main(String[] args) {
        View view = new View(new Controller());
    }

}