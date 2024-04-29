package com.example.myappmusicwithdatabase2.additionals;


import com.example.myappmusicwithdatabase2.daoClasses.Song;
import com.example.myappmusicwithdatabase2.daoClasses.SongDao;

import java.util.Map;
import java.util.regex.Matcher;

public class AdderSongsToDataBase {

    private static final SongDao dao = new SongDao();

    public static void main(String[] args) {
        String input = "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Chord Overstreet - Hold On Official Lyric Video (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Gym Class Heroes Stereo Hearts ft Adam Levine OFFICIAL VIDEO (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Hozier - Take Me To Church Lyric Video (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Imagine Dragons - Believer Lyrics (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Imagine Dragons - Bones (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Imagine Dragons - Demons Lyrics (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Imagine Dragons - Sharks Lyrics (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Imagine Dragons JID - Enemy Lyrics oh the misery everybody wants to be my enemy (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Lukas Graham - 7 Years Lyrics (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Maroon 5 - Memories (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\OneRepublic - Counting Stars Lyrics (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\OneRepublic - I Aint Worried Lyrics (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\RagnBone Man - Human Lyrics (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Rosa Linn - SNAP Lyrics (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Shawn Mendes - Mercy (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Stressed Out - twenty one pilots Lyrics vietsub  (videoo.info).mp3\n" +
                "C:\\Users\\wwerc\\Music\\TakeMeToChurchMusic\\Vance Joy - Riptide Lyrics (videoo.info).mp3";
        String output = addDoubleSlashes(input);

        String[] splitted = output.split("\n");

        for(String song : splitted){
            dao.save(new Song(song));
        }

    }

    public static void saveToSongDao(String[] splitted){
        for(String song : splitted){
            dao.save(new Song(song));
        }
    }

    public static String addDoubleSlashes(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == '\\') {
                result.append("\\\\\\\\");
            } else if(c == '\''){
                result .append("''");
            }
            else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String removeDoubleSlashes(String input){
        System.out.println(input);
        System.out.println("regex " + "\\\\\\\\");

        String res = input.replaceAll("\\\\\\\\", "~");
        String result = res.replaceAll("~", "\\\\");
        System.out.println(result);
        
        return result;
    }
}