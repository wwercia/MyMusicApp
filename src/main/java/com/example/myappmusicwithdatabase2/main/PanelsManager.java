package com.example.myappmusicwithdatabase2.main;


import com.example.myappmusicwithdatabase2.additionals.AdderSongsToDataBase;
import com.example.myappmusicwithdatabase2.daoClasses.*;
import com.example.myappmusicwithdatabase2.daoClasses.Frame;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelsManager {

    private final HashMap<String, JPanel> panelsForPlaylists = new HashMap<>();
    private final HashMap<String, JTextArea> areasForPlaylistNames = new HashMap<>();

    private final JFrame frame;
    private final Controller controller;
    private final View view;
    private final PlaylistDao playlistDao = new PlaylistDao();
    private final SongDao songDao = new SongDao();
    private final UniversalPlaylistsDao universalPlaylistsDao = new UniversalPlaylistsDao();
    private final FrameDao frameDao = new FrameDao();

    public PanelsManager(JFrame frame, Controller controller, View view) {
        this.frame = frame;
        this.controller = controller;
        this.view = view;
    }

    public void createPanelForNewPlaylist() {
        String message = JOptionPane.showInputDialog("Please enter the playlist name");
        if (message != null && message.length() != 0) {
            JTextArea playlistname = new JTextArea();

            ArrayList<Playlist> playlists = playlistDao.getAllPlaylists();
            Playlist lastPlaylist = null;
            for (Playlist playlist : playlists) {
                lastPlaylist = playlist;
            }

            Playlist newPlaylist = new Playlist(message, lastPlaylist.getX() + 257, 10);
            playlistname.setBounds(newPlaylist.getX(), 10, 250, 25);
            playlistname.setBackground(Color.LIGHT_GRAY);
            playlistname.setFont(new Font("name", Font.BOLD, 18));
            playlistname.setText("    " + newPlaylist.getName());
            playlistname.setEditable(false);

            playlistDao.save(newPlaylist);

            JPanel playlistPanel = new JPanel();
            JScrollPane playlistScrollPane = new JScrollPane(playlistPanel);

            playlistScrollPane.setBounds(newPlaylist.getX(), 40, 250, 311);
            playlistScrollPane.getVerticalScrollBar().setUnitIncrement(16);
            playlistPanel.setBackground(Color.lightGray);
            playlistPanel.setLayout(new BoxLayout(playlistPanel, BoxLayout.Y_AXIS));

            frame.add(playlistname);
            frame.add(playlistScrollPane);
            Dimension size = frame.getSize();
            frame.pack();
            frame.setSize((int) size.getWidth() + 255, (int) size.getHeight());

            panelsForPlaylists.put(message, playlistPanel);
            areasForPlaylistNames.put(message, playlistname);
            frameDao.save(new Frame((int) size.getHeight(), (int) size.getWidth() + 255));
            playlistDao.createTableForPlaylist(message);
        }

    }

    public void createPanel() {
        ArrayList<Playlist> playlists = playlistDao.getAllPlaylists();
        for (Playlist playlist : playlists) {
            JTextArea playlistname = new JTextArea();

            playlistname.setBounds(playlist.getX(), 10, 170, 25);
            playlistname.setBackground(Color.LIGHT_GRAY);
            playlistname.setFont(new Font("name", Font.BOLD, 18));
            playlistname.setText("    " + playlist.getName());
            playlistname.setEditable(false);

            //adding button 15.12.2023
            JPanel panelForButton = new JPanel();
            panelForButton.setBackground(Color.lightGray);
            panelForButton.setBounds(playlist.getX() + 170, 10, 80, 25);
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(15, 15));
            panelForButton.add(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (view.getPreviousButton() != null) {
                        view.getPreviousButton().setBackground(Color.white);
                    }
                    button.setBackground(new Color(156, 225, 255));
                    view.setCurrentPlaylist(playlistname.getText());
                    view.setPreviousButton(button);
                }
            });

            JPanel playlistPanel = new JPanel();
            JScrollPane playlistScrollPane = new JScrollPane(playlistPanel);

            playlistScrollPane.setBounds(playlist.getX(), 40, 250, 311);
            playlistScrollPane.getVerticalScrollBar().setUnitIncrement(16);
            playlistPanel.setBackground(Color.lightGray);
            playlistPanel.setLayout(new BoxLayout(playlistPanel, BoxLayout.Y_AXIS));

            panelsForPlaylists.put(playlist.getName(), playlistPanel);
            areasForPlaylistNames.put(playlist.getName(), playlistname);

            frame.add(playlistname);
            frame.add(playlistScrollPane);
            frame.add(panelForButton);
            Dimension size = frame.getSize();
            frame.pack();
            frame.setSize((int) size.getWidth() + 255, (int) size.getHeight());
        }

    }

    public void addSongToPanel(String nameOfPlaylist, String nameOfSongToAdd) {
        System.out.println();
        System.out.println("In addSongToPanel method");
        System.out.println("name of playlist: " + nameOfPlaylist + " name of song: " + nameOfSongToAdd);
        HashMap<ArrayList<String>, ArrayList<MediaPlayer>> m = controller.getPlaylists().get(nameOfPlaylist);
        List<ArrayList<MediaPlayer>> mediaPlayersNotRready = m.values().stream().toList();
        List<ArrayList<String>> namesNotReady = m.keySet().stream().toList();

        ArrayList<MediaPlayer> medias = mediaPlayersNotRready.get(0);
        ArrayList<String> na = namesNotReady.get(0);


        ArrayList<String> namesToAdd = new ArrayList<>();
        ArrayList<MediaPlayer> songsToAdd = new ArrayList<>();

        ArrayList<String> names = controller.getNamesOfAllSongs();
        ArrayList<MediaPlayer> songs = controller.getAllMediaPlayers();

        if (!na.contains(nameOfSongToAdd.trim())) {
            for (Map.Entry<String, JPanel> entry : panelsForPlaylists.entrySet()) {
                if (entry.getKey().equals(nameOfPlaylist)) {
                    for (int i = 0; i < names.size(); i++) {
                        if (names.get(i).equals(nameOfSongToAdd)) {
                            System.out.println();
                            System.out.println("this 2 should be the same: ");
                            System.out.println(names.get(i));
                            System.out.println(nameOfSongToAdd);
                            String name = names.get(i);
                            MediaPlayer song = songs.get(i);
                            namesToAdd.add(name);
                            songsToAdd.add(song);
                            JButton button = new JButton(nameOfSongToAdd);
                            button.setBounds(15, 170, 60, 30);
                            button.setBackground(new Color(156, 225, 255));
                            entry.getValue().add(button);
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    System.out.println();
                                    System.out.println("Song was clicked : " + name);
                                    System.out.println("Value of deletingplaylist value is: " + view.getDeletingFromPlaylist());
                                    System.out.println();
                                    if (view.getDeletingFromPlaylist()) {
                                        System.out.println("entry.getValue : ");
                                        System.out.println(name.trim());
                                        Song song = songDao.findByPath(name.trim());
                                        System.out.println(song);
                                        universalPlaylistsDao.delete(nameOfPlaylist, song);
                                        deleteSongFromPanel(nameOfPlaylist, name);
                                    } else {
                                        controller.playChoosenSong(song, nameOfPlaylist);
                                        view.displayPlayingSong(name);
                                    }
                                }
                            });
                            packAndSetLastSize();
                            break;
                        }
                    }
                }
            }
            na.addAll(namesToAdd);
            medias.addAll(songsToAdd);
        }
    }

    public void addSongToAllSongsPanel(String nameOfSongToAdd) {
        ArrayList<MediaPlayer> medias = controller.getAllMediaPlayers();
        ArrayList<String> names = controller.getNamesOfAllSongs();

        String readyPath = AdderSongsToDataBase.removeDoubleSlashes(nameOfSongToAdd);

        StringBuilder resultName = new StringBuilder();
        String[] splitted1 = readyPath.split("Music\\\\.*\\\\\\\\");
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


        System.out.println();
        System.out.println(readyPath);
        System.out.println(new File(readyPath).toURI().toString());
        Media hit = new Media(new File(readyPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.setVolume(0.07);
        mediaPlayer.onEndOfMediaProperty().set(new Runnable() {
            @Override
            public void run() {
                controller.playNextSong("all songs");
            }
        });
        names.add(result);
        medias.add(mediaPlayer);

        JButton button = new JButton(result);
        button.setBounds(15, 170, 60, 30);
        button.setBackground(new Color(156, 225, 255));
        JPanel panel = view.getPlaylistPanel();
        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.getAddingToPlaylist()) {
                    System.out.println("entry.getValue : ");
                    System.out.println(result.trim());
                    Song song = songDao.findByPath(result.trim());
                    System.out.println(song);
                    universalPlaylistsDao.save(view.getPlaylistNameForAddingSongs(), song);
                    addSongToPanel(view.getPlaylistNameForAddingSongs(), result);
                } else {
                    ArrayList<MediaPlayer> mediaPlayers = controller.getAllMediaPlayers();
                    int index = 0;
                    for (String value2 : names) {
                        if (value2.equals(result)) {
                            break;
                        }
                        index++;
                    }
                    MediaPlayer songToPlay = mediaPlayers.get(index);
                    controller.playChoosenSong(songToPlay, "all songs");
                    view.displayPlayingSong(result);
                }
            }
        });
        packAndSetLastSize();

    }


    public void deleteSongFromPanel(String nameOfPlaylist, String nameOfSongToDelete) {
        HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songs = controller.getPlaylists().get(nameOfPlaylist);
        List<ArrayList<MediaPlayer>> mediaPlayersNotRready = songs.values().stream().toList();
        List<ArrayList<String>> namesNotReady = songs.keySet().stream().toList();

        ArrayList<MediaPlayer> mediaPlayers = mediaPlayersNotRready.get(0);
        ArrayList<String> names = namesNotReady.get(0);

        for (Map.Entry<String, JPanel> entry : panelsForPlaylists.entrySet()) {
            if (entry.getKey().equals(nameOfPlaylist)) {
                for (int i = 0; i < names.size(); i++) {
                    String name = names.get(i);
                    if (name.equals(nameOfSongToDelete)) {
                        MediaPlayer song = mediaPlayers.get(i);
                        mediaPlayers.remove(song);
                        names.remove(name);

                        JPanel panel = entry.getValue();

                        Component[] components = panel.getComponents();
                        Component component = null;
                        for (Component comp : components) {
                            if (comp instanceof JButton button) {
                                System.out.println(button.getText());
                                System.out.println(button);
                                if (button.getText().equals(name)) {
                                    component = button;
                                    break;
                                }
                            }
                        }
                        panel.remove(component);
                        packAndSetLastSize();
                    }
                }
            }
        }
    }


    public void loadSongsToPanel(String nameOfPlaylist) {
        JPanel panel = null;
        for (Map.Entry<String, JPanel> entry : panelsForPlaylists.entrySet()) {
            if (entry.getKey().equals(nameOfPlaylist)) {
                panel = entry.getValue();
            }
        }

        HashMap<String, HashMap<ArrayList<String>, ArrayList<MediaPlayer>>> map = controller.getPlaylists();
        ArrayList<String> namesToAdd = new ArrayList<>();
        ArrayList<MediaPlayer> songsToAdd = new ArrayList<>();

        ArrayList<Playlist> playlists = playlistDao.getAllPlaylists();
        System.out.println("Loading songs to: " + nameOfPlaylist);
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(nameOfPlaylist)) {
                ArrayList<UniversalPlaylists> songsInPlaylist = universalPlaylistsDao.getAllsongs(nameOfPlaylist);
                ArrayList<Song> songs = songDao.getAllSongs();

                for (Song song : songs) {
                    for (UniversalPlaylists song2 : songsInPlaylist) {
                        if (song.getId().equals(song2.getSongId())) {

                            String nameOfSong = null;
                            double volume = 0;
                            MediaPlayer player = null;


                            ArrayList<String> namess = controller.getNamesOfAllSongs();
                            ArrayList<MediaPlayer> songss = controller.getAllMediaPlayers();

                            for (int i = 0; i < namess.size(); i++) {
                                String name = namess.get(i);
                                MediaPlayer songg = songss.get(i);

                                if (song.getPath().contains(name)) {
                                    nameOfSong = name;
                                    volume = songg.getVolume();
                                    player = songg;
                                }
                            }
                            System.out.println(nameOfSong);
                            namesToAdd.add(nameOfSong);

                            JButton button = new JButton(nameOfSong);
                            button.setBounds(15, 170, 60, 30);
                            button.setBackground(new Color(156, 225, 255));
                            panel.add(button);

                            String finalNameOfSong = nameOfSong;
                            MediaPlayer finalMedia = new MediaPlayer(player.getMedia());
                            finalMedia.setVolume(volume);

                            songsToAdd.add(finalMedia);

                            controller.setOnEndingOfSong(nameOfPlaylist, finalMedia);


                            String finalNameOfSong1 = nameOfSong;
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    System.out.println();
                                    System.out.println("Song was clicked : " + finalNameOfSong1);
                                    System.out.println("Value of deletingPlaylist value is: " + view.getDeletingFromPlaylist());
                                    System.out.println();
                                    if (view.getDeletingFromPlaylist()) {
                                        System.out.println("entry.getValue : ");
                                        System.out.println(finalNameOfSong1.trim());
                                        Song song = songDao.findByPath(finalNameOfSong1.trim());
                                        System.out.println(song);
                                        universalPlaylistsDao.delete(nameOfPlaylist, song);
                                        deleteSongFromPanel(nameOfPlaylist, finalNameOfSong1);
                                    } else {
                                        System.out.println("view.getDeletingFromPlaylist() is false");
                                        System.out.println(finalMedia + "     " + nameOfPlaylist);
                                        controller.playChoosenSong(finalMedia, nameOfPlaylist);
                                        System.out.println();
                                        System.out.println("name of playling song: " + finalNameOfSong);
                                        view.displayPlayingSong(finalNameOfSong);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
        HashMap<ArrayList<String>, ArrayList<MediaPlayer>> lists = new HashMap<>();
        lists.put(namesToAdd, songsToAdd);
        map.put(nameOfPlaylist, lists);
    }


    // new 21.12.2023
    public void loadGivenSongsToPanel(String oldPlaylistName, String newPlaylistName) {

        panelsForPlaylists.remove(oldPlaylistName);
        JPanel panel = new JPanel();
        panelsForPlaylists.put(newPlaylistName, panel);

        HashMap<String, HashMap<ArrayList<String>, ArrayList<MediaPlayer>>> map = controller.getPlaylists();
        ArrayList<String> namesToAdd = new ArrayList<>();
        ArrayList<MediaPlayer> songsToAdd = new ArrayList<>();

        ArrayList<Playlist> playlists = playlistDao.getAllPlaylists();
        System.out.println("Loading songs to: " + newPlaylistName);

        clearEntirePlaylist(oldPlaylistName);

        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(oldPlaylistName)) {
                ArrayList<UniversalPlaylists> songsInPlaylist = universalPlaylistsDao.getAllsongs(oldPlaylistName);
                ArrayList<Song> songs = songDao.getAllSongs();

                for (Song song : songs) {
                    for (UniversalPlaylists song2 : songsInPlaylist) {
                        if (song.getId().equals(song2.getSongId())) {

                            String nameOfSong = null;
                            double volume = 0;
                            MediaPlayer player = null;


                            ArrayList<String> namess = controller.getNamesOfAllSongs();
                            ArrayList<MediaPlayer> songss = controller.getAllMediaPlayers();

                            for (int i = 0; i < namess.size(); i++) {
                                String name = namess.get(i);
                                MediaPlayer songg = songss.get(i);

                                if (song.getPath().contains(name)) {
                                    nameOfSong = name;
                                    volume = songg.getVolume();
                                    player = songg;
                                }
                            }
                            System.out.println(nameOfSong);
                            namesToAdd.add(nameOfSong);

                            JButton button = new JButton(nameOfSong);
                            button.setBounds(15, 170, 60, 30);
                            button.setBackground(new Color(156, 225, 255));
                            panel.add(button);

                            String finalNameOfSong = nameOfSong;
                            MediaPlayer finalMedia = new MediaPlayer(player.getMedia());
                            finalMedia.setVolume(volume);

                            songsToAdd.add(finalMedia);

                            controller.setOnEndingOfSong(newPlaylistName, finalMedia);


                            String finalNameOfSong1 = nameOfSong;
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    System.out.println();
                                    System.out.println("Song was clicked : " + finalNameOfSong1);
                                    System.out.println("Value of deletingPlaylist value is: " + view.getDeletingFromPlaylist());
                                    System.out.println();
                                    if (view.getDeletingFromPlaylist()) {
                                        System.out.println("entry.getValue : ");
                                        System.out.println(finalNameOfSong1.trim());
                                        Song song = songDao.findByPath(finalNameOfSong1.trim());
                                        System.out.println(song);
                                        universalPlaylistsDao.delete(newPlaylistName, song);
                                        deleteSongFromPanel(newPlaylistName, finalNameOfSong1);
                                    } else {
                                        System.out.println("view.getDeletingFromPlaylist() is false");
                                        System.out.println(finalMedia + "     " + newPlaylistName);
                                        controller.playChoosenSong(finalMedia, newPlaylistName);
                                        System.out.println();
                                        System.out.println("name of playling song: " + finalNameOfSong);
                                        view.displayPlayingSong(finalNameOfSong);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public void packAndSetLastSize() {
        frame.pack();
        ArrayList<Frame> frames = frameDao.getAllFrames();
        Frame lastFrame = null;
        for (Frame frame : frames) {
            lastFrame = frame;
        }
        frame.setSize(lastFrame.getWidth(), lastFrame.getHeight());
    }


    public void deletePlaylist() {

    }


    public JTextArea getNewNameArea() {
        return newNameArea;
    }

    private JTextArea newNameArea = null;

    public JTextArea getOldNameArea() {
        return oldNameArea;
    }

    private JTextArea oldNameArea = null;
    public void changeNameOfPlaylist(String oldPlaylistName, String newNameOfPlaylist) {
        if (oldPlaylistName != null && newNameOfPlaylist != null && newNameOfPlaylist.length() != 0 && oldPlaylistName.length() != 0) {

            //playlistDao.changeNameOfPlaylistOnlyInPlaylists(oldPlaylistName, newNameOfPlaylist);
            if(newNameArea == null){
                newNameArea = new JTextArea();
                newNameArea.setBounds(areasForPlaylistNames.get(oldPlaylistName).getX(), 10, 250, 25);
                newNameArea.setBackground(Color.LIGHT_GRAY);
                newNameArea.setFont(new Font("name", Font.BOLD, 18));
                newNameArea.setEditable(false);
                oldNameArea = areasForPlaylistNames.get(oldPlaylistName);
                areasForPlaylistNames.get(oldPlaylistName).setVisible(false);
                frame.add(newNameArea);
            }
            newNameArea.setText("   " + newNameOfPlaylist);
/*
            System.out.println();
            System.out.println("In changingName method");
            System.out.println("old name " + oldPlaylistName);
            System.out.println("new name " + newNameOfPlaylist);

            //HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songs = controller.getPlaylists().get(oldPlaylistName);
            HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songss = new HashMap<>(controller.getPlaylists().get(oldPlaylistName));

            for (Map.Entry<ArrayList<String>, ArrayList<MediaPlayer>> entry : songss.entrySet()) {
                System.out.println("size " + entry.getKey().size());
                for (String name : entry.getKey()) {
                    System.out.println(name);
                }
            }
            controller.getPlaylists().put(newNameOfPlaylist, songss);

            HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songsss = new HashMap<>(controller.getPlaylists().get(newNameOfPlaylist));

            for (Map.Entry<ArrayList<String>, ArrayList<MediaPlayer>> entry : songsss.entrySet()) {
                System.out.println("size " + entry.getKey().size());
                for (String name : entry.getKey()) {
                    System.out.println(name);
                }
            }


            ArrayList<UniversalPlaylists> list = universalPlaylistsDao.getAllsongs(oldPlaylistName);

            //loadGivenSongsToPanel(oldPlaylistName, newNameOfPlaylist);
            List<ArrayList<String>> s = songss.keySet().stream().toList();
            ArrayList<String> names = s.get(0);

            //clearEntirePlaylist(oldPlaylistName);
            //clearEntirePlaylist(oldPlaylistName);

            playlistDao.changeNameOfPlaylist(oldPlaylistName, newNameOfPlaylist);
            JTextArea nameOfPlaylist = areasForPlaylistNames.get(oldPlaylistName);
            nameOfPlaylist.setText("   " + newNameOfPlaylist);
            //areasForPlaylistNames.remove(oldPlaylistName, nameOfPlaylist);
            areasForPlaylistNames.put(newNameOfPlaylist, nameOfPlaylist);

            JPanel panel = panelsForPlaylists.get(oldPlaylistName);

            JPanel newPanel = new JPanel();
            JScrollPane playlistScrollPane = new JScrollPane(newPanel);

            playlistScrollPane.setBounds(panel.getX(), 40, 250, 311);
            playlistScrollPane.getVerticalScrollBar().setUnitIncrement(16);
            newPanel.setBackground(Color.lightGray);
            newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));


            panelsForPlaylists.put(newNameOfPlaylist, newPanel);
            frame.add(newPanel);
            //panelsForPlaylists.remove(oldPlaylistName);

            System.out.println();
            System.out.println("adding songs to " + newNameOfPlaylist);
            for (Map.Entry<ArrayList<String>, ArrayList<MediaPlayer>> entry : songsss.entrySet()) {
                System.out.println("size " + entry.getKey().size());
                for (String name : entry.getKey()) {
                    System.out.println(name);
                    addSongToPanel(newNameOfPlaylist, name);
                }
            }
            //loadSongsToPanel(newNameOfPlaylist);

            /*
            System.out.println();
            System.out.println("adding songs to panel");
            System.out.println(names.size());
            for(String name : names){
                System.out.println(name);
                addSongToPanel(newNameOfPlaylist, name);
            }






            controller.getPlaylists().remove(oldPlaylistName);
            areasForPlaylistNames.remove(oldPlaylistName);
            panelsForPlaylists.remove(oldPlaylistName);


 */

        }
    }



    public void clearEntirePlaylist(String nameOfPlaylistToClear) {

        System.out.println();
        System.out.println("in clearEntirePlaylist method");
        ArrayList<UniversalPlaylists> songs = universalPlaylistsDao.getAllsongs(nameOfPlaylistToClear);

        //delete buttons
        HashMap<ArrayList<String>, ArrayList<MediaPlayer>> playlist = controller.getPlaylists().get(nameOfPlaylistToClear);
        List<ArrayList<String>> namesNotReady = playlist.keySet().stream().toList();
        List<ArrayList<MediaPlayer>> mediasNotReady = playlist.values().stream().toList();
        ArrayList<String> names = namesNotReady.get(0);
        ArrayList<MediaPlayer> mediaPlayers = mediasNotReady.get(0);
        System.out.println();
        System.out.println("size of playlist " + names.size());
        for (int i = 0; i < names.size(); i++) {
            String songName = names.get(i);
            System.out.println(songName);
            deleteButton(nameOfPlaylistToClear, songName);
        }

        //delete from lists
        names.clear();
        mediaPlayers.clear();

        //delete from database
        for (UniversalPlaylists sth : songs) {
            universalPlaylistsDao.delete(nameOfPlaylistToClear, songDao.findByID(sth.getSongId()));
        }
        packAndSetLastSize();

    }

    public void deleteButton(String nameOfPlaylist, String nameOfSongToDelete) {
        HashMap<ArrayList<String>, ArrayList<MediaPlayer>> songs = controller.getPlaylists().get(nameOfPlaylist);
        List<ArrayList<MediaPlayer>> mediaPlayersNotRready = songs.values().stream().toList();
        List<ArrayList<String>> namesNotReady = songs.keySet().stream().toList();

        ArrayList<MediaPlayer> mediaPlayers = mediaPlayersNotRready.get(0);
        ArrayList<String> names = namesNotReady.get(0);

        for (Map.Entry<String, JPanel> entry : panelsForPlaylists.entrySet()) {
            if (entry.getKey().equals(nameOfPlaylist)) {
                for (int i = 0; i < names.size(); i++) {
                    String name = names.get(i);
                    if (name.equals(nameOfSongToDelete)) {
                        MediaPlayer song = mediaPlayers.get(i);

                        JPanel panel = entry.getValue();

                        Component[] components = panel.getComponents();
                        Component component = null;
                        for (Component comp : components) {
                            if (comp instanceof JButton button) {
                                System.out.println(button.getText());
                                System.out.println(button);
                                if (button.getText().equals(name)) {
                                    component = button;
                                    break;
                                }
                            }
                        }
                        panel.remove(component);
                    }
                }
            }
        }
    }

}
