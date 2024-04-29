package com.example.myappmusicwithdatabase2.main;


import java.io.File;
import java.util.*;

import com.example.myappmusicwithdatabase2.daoClasses.Song;
import com.example.myappmusicwithdatabase2.daoClasses.SongDao;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Model {

    public ArrayList<String> getNamesOfAllSongs() {
        return namesOfAllSongs;
    }

    public ArrayList<MediaPlayer> getAllMediaPlayers() {
        return allMediaPlayers;
    }

    private final ArrayList<String> namesOfAllSongs = new ArrayList<>();
    private final ArrayList<MediaPlayer> allMediaPlayers = new ArrayList<>();

    private static MediaPlayer currentSong;

    private static final ArrayList<MediaPlayer> previousSongs = new ArrayList<>();

    private static final HashMap<String, HashMap<ArrayList<String>, ArrayList<MediaPlayer>>> playlists = new HashMap<>();

    private String wayOfPlayingSongs = "random";

    private String currentPlaylist = "all songs";


    final JFXPanel fxPanel = new JFXPanel();

    private final SongDao dao = new SongDao();

    public void loadPlaylist() {

        ArrayList<String> songsURI = new ArrayList<>();
        for (Song song : dao.getAllSongs()) {
            songsURI.add(song.getPath());
        }

        ArrayList<MediaPlayer> songs = new ArrayList<>();
        for (String song : songsURI) {
            Media hit = new Media(new File(song).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            //code if music is quieter than others
            //if (song.equals("C:\\a-moje-rzeczy\\programowanie\\projekty\\MyMusic\\betterchristmasSongs\\Gene Autry - Rudolph the Red-Nosed Reindeer.mp3")){
            //        || song.equals("C:\\Users\\wwerc\\IdeaProjects\\MyAppMusic\\src\\main\\resources\\com\\example\\myappmusic\\music\\chivas-odkd nauczyem si przeklina (videoo.info).mp3")) {
            //    mediaPlayer.setVolume(0.25);
            //} else {
            mediaPlayer.setVolume(0.07);
            //}
            mediaPlayer.onEndOfMediaProperty().set(new Runnable() {
                @Override
                public void run() {

                    // I think here should be
                    playNextSong("all songs");

                    /*
                    MediaPlayer song;
                    ArrayList<MediaPlayer> list = new ArrayList<>(allMediaPlayers);
                    int index = generator.nextInt(list.size());
                    song = list.get(index);

                    checkVolume(song);
                    previousSongs.add(currentSong);
                    //added stop 30.11 to test
                    currentSong.stop();
                    currentSong = song;
                    System.out.println("Playing automatically:       " + namesOfAllSongs.get(index));
                    displayInformations(song);
                    song.play();

                     */
                }
            });
            mediaPlayer.setOnStalled(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Ths is the problem");
                    System.out.println("STALLED — stan odtwarzacza, gdy dane napływające do bufora uległy spowolnieniu lub zatrzymaniu, a w buforze odtwarzania nie ma wystarczającej ilości danych, aby kontynuować odtwarzanie. Odtwarzanie będzie kontynuowane automatycznie po zgromadzeniu wystarczającej ilości danych, aby wznowić odtwarzanie. Jeśli plik zostanie wstrzymany lub zatrzymany w tym stanie, buforowanie będzie kontynuowane, ale odtwarzanie nie zostanie automatycznie wznowione, gdy zbuforowana zostanie wystarczająca ilość danych.");
                }
            });
            songs.add(mediaPlayer);
        }
        ArrayList<String> namess = new ArrayList<>();
        for (String uri : songsURI) {
            StringBuilder resultName = new StringBuilder();
            String[] splitted1 = uri.split("Music\\\\.*\\\\\\\\");
            String[] splitted2 = splitted1[1].split("\\.mp");
            resultName.append(splitted2[0]);
            String result = resultName.toString().replaceAll("\\(videoo.info\\)", "")
                    .replaceAll("Official Video", "")
                    .replaceAll("prod Imotape Productions official video ", "")
                    .replaceAll("official video", "")
                    .replaceAll("Video", "")
                    .replaceAll("Lyrics", "")
                    .replaceAll("Lyric", "")
                    .replaceAll("Official Music", "")
                    .replaceAll("Shut up count your calories i never looked good in mom jeans", "")
                    .replaceAll("Did you find your btch in me Oh youre abominable socially", "")
                    .replaceAll("Official", "").replaceAll("Official   videoo", "")
                    .replaceAll("videoo", "").replaceAll("Audio", "")
                    .replaceAll("Vertical", "")
                    .replaceAll("vietsub", "")
                    .replaceAll("oh the misery everybody wants to be my enemy", " ")
                    .replaceAll("ft Adam Levine OFFICIAL VIDEO", "");
            namess.add(result.trim());
        }

        for (int i = 0; i < namess.size(); i++) {
            namesOfAllSongs.add(namess.get(i));
            allMediaPlayers.add(songs.get(i));

            System.out.println(songs.get(i) + "    " + namess.get(i));
        }
        HashMap<ArrayList<String>, ArrayList<MediaPlayer>> map = new HashMap<>();
        ArrayList<String> n = new ArrayList<>(namesOfAllSongs);
        ArrayList<MediaPlayer> m = new ArrayList<>(allMediaPlayers);
        map.put(n, m);

        playlists.put("all songs", map);
    }

    public String playPreviousSong(String playlistName) {
        MediaPlayer previous = null;
        if (previousSongs.size() != 0) {
            previous = previousSongs.get(previousSongs.size() - 1);
        }
        String name = getNameForPreviousSong(previous);
        previousSongs.remove(previous);
        if (currentSong != null && previous != null) {
            System.out.print("previous song is playing       " + name);
            System.out.println();
            currentSong.stop();
            checkVolume(previous);
            currentSong = previous;
            previous.play();
        }
        return name;
    }

    public String playNextSong(String playlistName) {

        MediaPlayer nextSong = null;
        if (wayOfPlayingSongs.equals("random")) {
            nextSong = getRandomSong(playlistName);
        } else if (wayOfPlayingSongs.equals("in order")) {
            nextSong = getNextSong(playlistName);
        }

        System.out.print("next song is playing");

        String name = getNameOfSong(playlistName, nextSong);




        if (currentSong != null) {
            previousSongs.add(currentSong);
            currentSong.stop();
        }
        System.out.print("      " + name);
        System.out.println();
        if (nextSong.equals(currentSong)) {
            nextSong = getRandomSong(playlistName);
            if (nextSong.equals(currentSong)) {
                nextSong = getRandomSong(playlistName);
            }
        }
        checkVolume(nextSong);
        currentSong = nextSong;
        nextSong.play();
        displayInformations(nextSong);
        System.out.println();
        System.out.println("Name of the song:   " + name);
        return name;
    }

    public String playSong(String playlistName) {
        System.out.print("song is playing");
        if (currentSong == null) {
            if (wayOfPlayingSongs.equals("random")) {
                currentSong = getRandomSong(playlistName);
            } else if (wayOfPlayingSongs.equals("in order")) {
                currentSong = getNextSong(playlistName);
            }
        }

        //System.out.print("     " + getNameOfSong(playlistName, currentSong));
        System.out.println("song is playing");
        currentSong.play();
        displayInformations(currentSong);
        int index = 0;
        for(MediaPlayer player : allMediaPlayers){
            if(player.getMedia().equals(currentSong.getMedia())){
                break;
            }
            index++;
        }
        //return getNameOfSong(playlistName, currentSong);
        return namesOfAllSongs.get(index);
    }

    //adding 17.12.2023
    public MediaPlayer getNextSong(String playlistName) {

        if (currentSong == null && playlistName.equals("all songs")) {
            ArrayList<MediaPlayer> songs = new ArrayList<>(allMediaPlayers);
            return songs.get(0);
        } else if (currentSong == null) {
            HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songs = playlists.get(playlistName.trim());
            List<ArrayList<MediaPlayer>> list = songs.values().stream().toList();
            ArrayList<MediaPlayer> mediaPlayers = list.get(0);
            return mediaPlayers.get(0);
        }

        if (playlistName.equals("all songs")) {

            ArrayList<MediaPlayer> songs = new ArrayList<>(allMediaPlayers);
            int index = 0;
            for (MediaPlayer player : songs) {
                if (player.getMedia().equals(currentSong.getMedia())) {
                    break;
                }
                index++;
            }
            // it is one more because now its index of current song, and we need song after this so one index more
            index++;
            // it returns first song if
            if(index >= songs.size()){
                return songs.get(0);
            }
            return songs.get(index);

        } else {
            HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songs = playlists.get(playlistName.trim());
            List<ArrayList<MediaPlayer>> list = songs.values().stream().toList();
            ArrayList<MediaPlayer> mediaPlayers = list.get(0);
            int index = 0;
            for (MediaPlayer player : mediaPlayers) {
                if (player.getMedia().equals(currentSong.getMedia())) {
                    break;
                }
                index++;
            }

            // it is one more because now its index of current song, and we need song after this so one index more
            index++;
            // it returns first song if last song from playlist was playing
            if(index >= mediaPlayers.size()){
                return mediaPlayers.get(0);
            }
            return mediaPlayers.get(index);
        }
    }

    private final Random generator = new Random();

    public MediaPlayer getRandomSong(String playlistName) {
        HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songs = playlists.get(playlistName.trim());
        List<ArrayList<MediaPlayer>> list = songs.values().stream().toList();
        ArrayList<MediaPlayer> mediaPlayers = list.get(0);

        return mediaPlayers.get(generator.nextInt(mediaPlayers.size()));
    }

    public String playRandomSong(String playlistName) {
        System.out.print("random song is playing");
        if (currentSong != null) {
            previousSongs.add(currentSong);
            currentSong.stop();
        }
        MediaPlayer m = getRandomSong(playlistName);
        if (m.equals(currentSong)) {
            m = getRandomSong(playlistName);
        }

        String name = getNameOfSong(playlistName, m);

        checkVolume(m);
        currentSong = m;
        System.out.print("      " + name);
        System.out.println();
        m.play();
        displayInformations(m);
        return name;
    }

    public void playChosenSong(MediaPlayer song, String playlistName) {
        System.out.print("chosen song is playing");
        if(playlistName.equals("all songs")){
            System.out.print("     " + namesOfAllSongs.get(allMediaPlayers.indexOf(song)));
        }else {
            System.out.print("     " + getNameOfSong(playlistName, song));
        }
        System.out.println();
        if (currentSong != null && !currentSong.equals(song)) {
            previousSongs.add(currentSong);
            currentSong.stop();
        }
        checkVolume(song);
        currentSong = song;
        displayInformations(song);
        song.play();
    }

    public boolean stopSong(String plalistName) {
        System.out.print("song is paused");
        if (currentSong != null) {
            System.out.println("song is stopped");
            currentSong.pause();
        }
        return true;
    }

    public void turnUpVolume() {
        currentSong.setVolume(currentSong.getVolume() + 0.02);
    }

    public void turnDownVolume() {
        currentSong.setVolume(currentSong.getVolume() - 0.02);
    }

    public void checkVolume(MediaPlayer mediaPlayer) {
        if (currentSong != null) {
            if (mediaPlayer.getVolume() != currentSong.getVolume()) {
                /*
                if (playlist.get(mediaPlayer).equals("Gene Autry - Rudolph the Red-Nosed Reindeer") ){
                    //        || playlist.get(mediaPlayer).equals("chivas-odkd nauczyem si przeklina (videoo.info)")) {
                    double difference = mediaPlayer.getVolume() - currentSong.getVolume();
                    if (difference != 0.18) {
                        mediaPlayer.setVolume(currentSong.getVolume() + difference);
                    }
                } else if (playlist.get(currentSong).equals("Gene Autry - Rudolph the Red-Nosed Reindeer")){
                    //        || playlist.get(currentSong).equals("chivas-odkd nauczyem si przeklina (videoo.info)")) {
                    double difference = currentSong.getVolume() - mediaPlayer.getVolume();
                    if (currentSong.getVolume() - mediaPlayer.getVolume() != 0.18) {
                        mediaPlayer.setVolume(currentSong.getVolume() - difference);
                    }
                } else {

                 */
                mediaPlayer.setVolume(currentSong.getVolume());
                //}
            }
        }
    }
    public String getCurrentSongName() {
        return getNameOfSong("all songs", currentSong);
    }

    public HashMap<String, HashMap<ArrayList<String>, ArrayList<MediaPlayer>>> getPlaylists() {
        return playlists;
    }

    private String getNameOfSong(String playlistName, MediaPlayer song) {

        if(playlistName.equals("all songs")){
            return namesOfAllSongs.get(allMediaPlayers.indexOf(song));
        }

        HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songs = playlists.get(playlistName);
        List<ArrayList<MediaPlayer>> mediaPlayersNotReady = songs.values().stream().toList();
        ArrayList<MediaPlayer> mediaPlayers = mediaPlayersNotReady.get(0);
        List<ArrayList<String>> namesNotReady = songs.keySet().stream().toList();
        ArrayList<String> names = namesNotReady.get(0);
        int index = 0;
        for (MediaPlayer mediaPlayer : mediaPlayers) {
            if (mediaPlayer.equals(song)) {
                break;
            }
            index++;
        }


        if (index > names.size()) {
            //add in 18.12.2023
            if (index > mediaPlayers.size()) {
                HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songssss = playlists.get(playlistName.trim());
                List<ArrayList<String>> listtt = songssss.keySet().stream().toList();
                ArrayList<String> mediaPlayersss = listtt.get(0);
                return mediaPlayersss.get(0);
            }
        }
        System.out.println();
        System.out.println(playlistName);
        System.out.println("Size of playlist " + playlistName + " is + " + names.size());
        System.out.println("index is " + index);
        System.out.println("the song is " + names.get(index));

        return names.get(index);
    }

    public String getNameForPreviousSong(MediaPlayer song) {

        return namesOfAllSongs.get(allMediaPlayers.indexOf(song));
        /*
        HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songs = playlists.get("all songs");
        List<ArrayList<MediaPlayer>> mediaPlayersNotReady = songs.values().stream().toList();
        List<ArrayList<String>> namesNotReady = songs.keySet().stream().toList();

        ArrayList<MediaPlayer> mediaPlayers = mediaPlayersNotReady.get(0);
        ArrayList<String> names = namesNotReady.get(0);

        System.out.println();
        int index = 0;
        for (MediaPlayer mediaPlayer : mediaPlayers) {
            System.out.println(mediaPlayer.getMedia().equals(song.getMedia()));
            if (mediaPlayer.getMedia().equals(song.getMedia())) {
                break;
            }
            index++;
        }
        System.out.println(index);
        return names.get(index);

         */
    }

    public void setOnEndingOfSong(String playlistName, MediaPlayer song) {
        song.onEndOfMediaProperty().set(new Runnable() {
            @Override
            public void run() {
                if (playlistName.equals(currentPlaylist)) {
                    playNextSong(playlistName);
                } else {
                    playNextSong(currentPlaylist);
                }
            }
        });
    }
    public void setCurrentPlaylist(String currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }

    public void displayInformations(MediaPlayer mediaPlayer) {
        System.out.println("Volume  " + mediaPlayer.getVolume());
        System.out.println("balance  " + mediaPlayer.getBalance());
        System.out.println("cycle count" + mediaPlayer.getCycleCount());
        System.out.println("totalDurationProperty" + mediaPlayer.totalDurationProperty());
        System.out.println("status " + mediaPlayer.getStatus());
        System.out.println(mediaPlayer.getVolume());
    }

    public void setWayOfPlayingSongs(String wayOfPlayingSongs) {
        this.wayOfPlayingSongs = wayOfPlayingSongs;
    }

}