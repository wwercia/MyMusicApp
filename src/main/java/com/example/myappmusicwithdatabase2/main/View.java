package com.example.myappmusicwithdatabase2.main;


import com.example.myappmusicwithdatabase2.daoClasses.*;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class View {

    Controller controller;
    private final JFrame frame = new JFrame("MusicApp");
    private final JPanel buttonPanelForPlaying = new JPanel();
    private final JPanel buttonPanelForVolume = new JPanel();
    private final JPanel buttonPanelForManagingPlaylists = new JPanel();
    private final JPanel buttonPanelFor123Playlist = new JPanel();
    private final JButton firstButton = new JButton("1");
    private final JButton secondButton = new JButton("2");
    private final JButton thirdButton = new JButton("3");
    private final ArrayList<String> playlists = new ArrayList<>();
    private final JPanel buttonPanelForManagingSongsInPlaylists = new JPanel();
    private final JPanel buttonPanelForCompletelyNewSongs = new JPanel();

    public JPanel getPlaylistPanel() {
        return playlistPanel;
    }

    private final JPanel playlistPanel = new JPanel();
    private final JScrollPane playlistScrollPane = new JScrollPane(playlistPanel);
    private final JTextArea playlistName = new JTextArea(50, 50);
    private final JButton playSongButton = new JButton("play");
    private final JButton stopSongButton = new JButton("stop");
    private final JButton nextSongButton = new JButton("next song");
    private final JButton previousSongButton = new JButton("previous song");
    private final JButton playRandomSongButton = new JButton("play random songs");
    private boolean playingSongsRandom = false;
    private final JButton playSongsInOrderButton = new JButton("play songs in order");
    private boolean playingsongsInOrder = false;
    private final JButton turnUpSongButton = new JButton("turn up the song");
    private final JButton turnDownSongButton = new JButton("turn down the song");
    private final JTextArea volumeWord = new JTextArea(50, 50);
    private final JTextArea currentlyPlayingWord = new JTextArea(50, 50);
    private final JTextArea nameOfCurrentSong = new JTextArea(50, 50);
    private final JTextArea playlistsWord = new JTextArea(50, 50);
    private final JButton createPlaylistButton = new JButton("create playlist");
    private final JButton deletePlaylistButton = new JButton("delete playlist");
    private final JButton changePlaylistNameButton = new JButton("change name of playlist");
    private final JButton addToPlaylistButton = new JButton("add to playlist");

    public boolean getAddingToPlaylist() {
        return addingToPlaylist;
    }

    private boolean addingToPlaylist = false;

    public String getPlaylistNameForAddingSongs() {
        return playlistNameForAddingSongs;
    }

    private String playlistNameForAddingSongs = "";
    private final JButton deleteFromPlaylistButton = new JButton("delete from playlist");

    private boolean deletingFromPlaylist = false;
    private final JButton clearEntirePlaylistButton = new JButton("clear entire playlist");
    private final JButton addCompletelyNewSongsButton = new JButton("add completely new song(s)");

    private final SongDao songDao = new SongDao();
    private final PlaylistDao playlistDao = new PlaylistDao();
    private final UniversalPlaylistsDao universalPlaylistsDao = new UniversalPlaylistsDao();

    private final PanelsManager panelsManager;
    private String currentPlaylist = null;

    public View(Controller controller) {
        this.controller = controller;
        panelsManager = new PanelsManager(frame, controller, this);
        controller.loadPlaylist();
        initView();

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Add a component listener to handle frame hiding
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {

                if (panelsManager.getNewNameArea() != null) {
                    playlistDao.changeNameOfPlaylist(panelsManager.getOldNameArea().getText(), panelsManager.getNewNameArea().getText());
                }

                ((JFrame) e.getComponent()).dispose(); // Close the frame gracefully
                System.exit(0);
            }
        });
    }

    public void initView() {
        // Panels
        buttonPanelForPlaying.setBounds(10, 10, 232, 140);
        buttonPanelForPlaying.setBackground(new Color(166, 166, 166));

        buttonPanelForManagingPlaylists.setBounds(504, 50, 250, 80);
        buttonPanelForManagingPlaylists.setBackground(new Color(166, 166, 166));

        buttonPanelFor123Playlist.setBounds(504, 130, 250, 30);
        buttonPanelFor123Playlist.setBackground(new Color(166, 166, 166));

        buttonPanelForManagingSongsInPlaylists.setBounds(504, 160, 250, 140);
        buttonPanelForManagingSongsInPlaylists.setBackground(new Color(166, 166, 166));

        buttonPanelForCompletelyNewSongs.setBounds(504, 300, 250, 50);
        buttonPanelForCompletelyNewSongs.setBackground(new Color(166, 166, 166));


        buttonPanelForVolume.setBounds(10, 185, 232, 85);
        buttonPanelForVolume.setBackground(new Color(166, 166, 166));

        //area for playlist
        playlistScrollPane.setBounds(248, 40, 250, 311);
        playlistScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        playlistPanel.setBackground(Color.lightGray);
        playlistPanel.setLayout(new BoxLayout(playlistPanel, BoxLayout.Y_AXIS));

        //area for playlist name
        //playlistName.setBounds(248, 10, 250, 25);
        playlistName.setBounds(248, 10, 170, 25);
        playlistName.setBackground(Color.LIGHT_GRAY);
        playlistName.setFont(new Font("name", Font.BOLD, 18));
        playlistName.setText("              all songs");
        playlistName.setEditable(false);

        //adding button 15.12.2023
        JPanel panelForButton = new JPanel();
        panelForButton.setBackground(Color.lightGray);
        panelForButton.setBounds(418, 10, 80, 25);
        JButton buttonToMarkPlaylist = new JButton();
        buttonToMarkPlaylist.setPreferredSize(new Dimension(15, 15));
        panelForButton.add(buttonToMarkPlaylist);
        buttonToMarkPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getPreviousButton() != null) {
                    getPreviousButton().setBackground(Color.white);
                }
                buttonToMarkPlaylist.setBackground(new Color(156, 225, 255));
                setPreviousButton(buttonToMarkPlaylist);
                setCurrentPlaylist("all songs");
            }
        });

        volumeWord.setBounds(10, 150, 232, 40);
        volumeWord.setBackground(new Color(166, 166, 166));
        volumeWord.setFont(new Font("name", Font.BOLD, 20));
        volumeWord.setText("              Volume:");
        volumeWord.setEditable(false);

        currentlyPlayingWord.setBounds(10, 270, 232, 40);
        currentlyPlayingWord.setBackground(new Color(166, 166, 166));
        currentlyPlayingWord.setFont(new Font("name", Font.BOLD, 20));
        currentlyPlayingWord.setText("  Currently playing:");
        currentlyPlayingWord.setEditable(false);


        nameOfCurrentSong.setBounds(10, 310, 232, 40);
        nameOfCurrentSong.setBackground(new Color(166, 166, 166));
        nameOfCurrentSong.setFont(new Font("name", Font.BOLD, 14));
        nameOfCurrentSong.setEditable(false);

        playlistsWord.setBounds(504, 10, 250, 40);
        playlistsWord.setBackground(new Color(166, 166, 166));
        playlistsWord.setFont(new Font("name", Font.BOLD, 18));
        playlistsWord.setText("         Manage playlists:");
        playlistsWord.setEditable(false);

        // Buttons
        nextSongButton.setBounds(20, 20, 40, 30);
        previousSongButton.setBounds(20, 80, 40, 30);
        playSongButton.setBounds(100, 100, 40, 30);
        stopSongButton.setBounds(50, 80, 40, 30);
        playRandomSongButton.setBounds(50, 80, 40, 30);
        turnUpSongButton.setBounds(15, 170, 60, 30);
        turnDownSongButton.setBounds(50, 80, 40, 30);
        createPlaylistButton.setBounds(50, 80, 40, 30);
        deletePlaylistButton.setBounds(50, 80, 40, 30);
        addToPlaylistButton.setBounds(50, 80, 40, 30);
        deleteFromPlaylistButton.setBounds(50, 80, 40, 30);

        nextSongButton.setSize(200, 200);

        nextSongButton.setBackground(new Color(103, 212, 255));
        previousSongButton.setBackground(new Color(103, 212, 255));
        playSongButton.setBackground(new Color(103, 212, 255));
        stopSongButton.setBackground(new Color(103, 212, 255));
        playRandomSongButton.setBackground(new Color(103, 212, 255));
        playSongsInOrderButton.setBackground(new Color(103, 212, 255));
        turnUpSongButton.setBackground(new Color(102, 235, 255));
        turnDownSongButton.setBackground(new Color(102, 235, 255));
        createPlaylistButton.setBackground(new Color(103, 212, 255));
        deletePlaylistButton.setBackground(new Color(103, 212, 255));
        changePlaylistNameButton.setBackground(new Color(103, 212, 255));
        firstButton.setBackground(new Color(103, 212, 255));
        secondButton.setBackground(new Color(103, 212, 255));
        thirdButton.setBackground(new Color(103, 212, 255));
        addToPlaylistButton.setBackground(new Color(103, 212, 255));
        deleteFromPlaylistButton.setBackground(new Color(103, 212, 255));
        clearEntirePlaylistButton.setBackground(new Color(103, 212, 255));
        addCompletelyNewSongsButton.setBackground(new Color(103, 212, 255));


        frame.setLayout(null);
        frame.add(playlistScrollPane);
        frame.add(buttonPanelForPlaying);
        frame.add(buttonPanelForVolume);
        frame.add(buttonPanelForManagingPlaylists);
        frame.add(buttonPanelFor123Playlist);
        frame.add(buttonPanelForManagingSongsInPlaylists);
        frame.add(buttonPanelForCompletelyNewSongs);
        frame.add(panelForButton);

        currentPlaylist = "all songs";

        ArrayList<String> songs = controller.getNamesOfAllSongs();

        for (String value : songs) {
            JButton button = new JButton(value);
            //added setName 16.12.2023
            button.setName(value);
            button.setBounds(15, 170, 60, 30);
            button.setBackground(new Color(156, 225, 255));
            playlistPanel.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (addingToPlaylist) {
                        System.out.println("entry.getValue : ");
                        System.out.println(value.trim());
                        Song song = songDao.findByPath(value.trim());
                        System.out.println(song);
                        universalPlaylistsDao.save(playlistNameForAddingSongs, song);
                        System.out.println("nazwa playlisty do której dodaje piosenke: " + playlistNameForAddingSongs);
                        panelsManager.addSongToPanel(playlistNameForAddingSongs, value);
                    } else {
                        ArrayList<MediaPlayer> mediaPlayers = controller.getAllMediaPlayers();

                        int index = 0;
                        for (String value2 : songs) {
                            if (value2.equals(value)) {
                                break;
                            }
                            index++;
                        }
                        MediaPlayer songToPlay = mediaPlayers.get(index);

                        controller.playChoosenSong(songToPlay, currentPlaylist);

                        displayPlayingSong(value);
                    }
                }
            });

        }

        frame.add(playlistName);
        buttonPanelForPlaying.add(previousSongButton);
        buttonPanelForPlaying.add(nextSongButton);
        buttonPanelForPlaying.add(playSongButton);
        buttonPanelForPlaying.add(stopSongButton);
        buttonPanelForPlaying.add(playRandomSongButton);
        buttonPanelForPlaying.add(playSongsInOrderButton);
        buttonPanelForVolume.add(turnUpSongButton);
        buttonPanelForVolume.add(turnDownSongButton);
        buttonPanelForManagingPlaylists.add(createPlaylistButton);
        buttonPanelForManagingPlaylists.add(deletePlaylistButton);
        buttonPanelForManagingPlaylists.add(changePlaylistNameButton);
        buttonPanelFor123Playlist.add(firstButton);
        buttonPanelFor123Playlist.add(secondButton);
        buttonPanelFor123Playlist.add(thirdButton);
        buttonPanelForManagingSongsInPlaylists.add(addToPlaylistButton);
        buttonPanelForManagingSongsInPlaylists.add(deleteFromPlaylistButton);
        buttonPanelForManagingSongsInPlaylists.add(clearEntirePlaylistButton);
        buttonPanelForCompletelyNewSongs.add(addCompletelyNewSongsButton);

        frame.add(volumeWord);
        frame.add(currentlyPlayingWord);
        frame.add(nameOfCurrentSong);
        frame.add(playlistsWord);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelsManager.createPanel();

        ArrayList<Playlist> play = playlistDao.getAllPlaylists();
        for (Playlist playlist1 : play) {
            panelsManager.loadSongsToPanel(playlist1.getName());
            playlists.add(playlist1.getName());
        }

        panelsManager.packAndSetLastSize();

        frame.setVisible(true);
        frame.setResizable(true);

        playRandomSongButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playRandomSongButton.setBackground(new Color(177, 219, 238));
                playSongsInOrderButton.setBackground(new Color(103, 212, 255));
                playingsongsInOrder = false;
                playingSongsRandom = true;
                controller.setWayOfPlayingSongs("random");
            }
        });
        playSongsInOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playSongsInOrderButton.setBackground(new Color(177, 219, 238));
                playRandomSongButton.setBackground(new Color(103, 212, 255));
                playingSongsRandom = false;
                playingsongsInOrder = true;
                controller.setWayOfPlayingSongs("in order");
            }
        });
        nextSongButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = controller.playNextSong(currentPlaylist);
                displayPlayingSong(name);
            }
        });
        previousSongButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = controller.playPreviousSong(currentPlaylist);
                displayPlayingSong(name);
            }
        });
        playSongButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = controller.playSong(currentPlaylist);
                displayPlayingSong(name);
            }
        });
        stopSongButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.stopSong(currentPlaylist);
            }
        });
        turnUpSongButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.turnUpVolume();
            }
        });
        turnDownSongButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.turnDownVolume();
            }
        });

        createPlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panelsManager.createPanelForNewPlaylist();
            }
        });
        deletePlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panelsManager.deletePlaylist();
            }
        });
        changePlaylistNameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String oldNameOfPlaylist = JOptionPane.showInputDialog("Enter the name of the playlist you want to rename");
                String newNameOfPlaylist = JOptionPane.showInputDialog("Enter a new name for the playlist");
                panelsManager.changeNameOfPlaylist(oldNameOfPlaylist, newNameOfPlaylist);
            }
        });
        firstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(addingToPlaylist){
                    if(playlistNameForAddingSongs.equals(playlists.get(0))){
                        firstButton.setBackground(new Color(103, 212, 255));
                        playlistNameForAddingSongs = "";
                    }else {
                        playlistNameForAddingSongs = playlists.get(0);
                        firstButton.setBackground(new Color(177, 219, 238));
                        secondButton.setBackground(new Color(103, 212, 255));
                        thirdButton.setBackground(new Color(103, 212, 255));
                    }
                }//else {
                //    if(playlistNameForAddingSongs.equals(playlists.get(0))){
                //        firstButton.setBackground(new Color(103, 212, 255));
                //        playlistNameForAddingSongs = "";
                //    }
                //}
            }
        });
        secondButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(addingToPlaylist) {
                    if (playlistNameForAddingSongs.equals(playlists.get(1))) {
                        secondButton.setBackground(new Color(103, 212, 255));
                        playlistNameForAddingSongs = "";
                    } else {
                        playlistNameForAddingSongs = playlists.get(1);
                        secondButton.setBackground(new Color(177, 219, 238));
                        firstButton.setBackground(new Color(103, 212, 255));
                        thirdButton.setBackground(new Color(103, 212, 255));
                    }
                }
            }
        });
        thirdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(addingToPlaylist) {
                    if (playlistNameForAddingSongs.equals(playlists.get(2))) {
                        thirdButton.setBackground(new Color(103, 212, 255));
                        playlistNameForAddingSongs = "";
                    } else {
                        playlistNameForAddingSongs = playlists.get(2);
                        thirdButton.setBackground(new Color(160, 222, 253));
                        firstButton.setBackground(new Color(103, 212, 255));
                        secondButton.setBackground(new Color(103, 212, 255));
                    }
                }
            }
        });
        addToPlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (addingToPlaylist) {
                    addingToPlaylist = false;
                    addToPlaylistButton.setBackground(new Color(103, 212, 255));
                    firstButton.setBackground(new Color(103, 212, 255));
                    secondButton.setBackground(new Color(103, 212, 255));
                    thirdButton.setBackground(new Color(103, 212, 255));
                    playlistNameForAddingSongs = "";
                } else {
                    addingToPlaylist = true;
                    addToPlaylistButton.setBackground(new Color(160, 222, 253));
                }
            }
        });
        deleteFromPlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (deletingFromPlaylist) {
                    deletingFromPlaylist = false;
                    deleteFromPlaylistButton.setBackground(new Color(103, 212, 255));
                } else {
                    deletingFromPlaylist = true;
                    deleteFromPlaylistButton.setBackground(new Color(160, 222, 253));
                }
            }
        });
        clearEntirePlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nameOfPlaylistToClear = JOptionPane.showInputDialog("Enter the name of the playlist from which you want to remove all songs");
                panelsManager.clearEntirePlaylist(nameOfPlaylistToClear);
            }
        });
        addCompletelyNewSongsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Enter paths");
                String[] songs = controller.addCompletelyNewSongs(input);
                for (String nameOfSong : songs) {
                    panelsManager.addSongToAllSongsPanel(nameOfSong);
                }
            }
        });

    }

    public void displayPlayingSong(String name) {
        nameOfCurrentSong.setText("   " + name);
        panelsManager.packAndSetLastSize();
        System.out.println();
        System.out.println("In displayPlayingSong method. This method thinks that title is:   " + name);
    }

    public void setCurrentPlaylist(String nameOfPlaylist) {
        currentPlaylist = nameOfPlaylist.trim();
        controller.setCurrentPlaylist(nameOfPlaylist.trim());
        System.out.println(currentPlaylist);

        System.out.println();
        System.out.println("current playlist " + currentPlaylist);
    }

    public JButton getPreviousButton() {
        return previousButton;
    }

    public void setPreviousButton(JButton previousButton) {
        this.previousButton = previousButton;
    }

    private JButton previousButton = null;

    public boolean getDeletingFromPlaylist() {
        return deletingFromPlaylist;
    }

}

