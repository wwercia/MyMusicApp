package com.example.myappmusicwithdatabase2.daoClasses;


import java.sql.*;
import java.util.ArrayList;

public class SongDao {

    private final Connection connection;

    public SongDao() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/songs?serverTimezone=UTC", "root", "wwercia1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // metoda zapisywania
    public void save(Song song) {
        final String sql = String.format("INSERT INTO songs.songs (path) VALUES ('%s')", song.getPath());
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generetedKeys = statement.getGeneratedKeys();
            if (generetedKeys.next()) {
                song.setId(generetedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql);
        }
    }

    // metoda wyszukująca piosenke w bazie
    public Song findByPath(String searchPath) {
        ArrayList<Song> songs =  getAllSongs();
        Song song = new Song("");
        for(Song songg : songs){
            System.out.println(songg.getPath());
            System.out.println(searchPath);
            if(songg.getPath().contains(searchPath)){
                //if(songg.getPath().matches(".*" + searchPath + ".*")){

                //song = new Song(songg.getId(), songg.getPath());
                System.out.println(songg.getId());
                song.setId(songg.getId());
                song.setPath(songg.getPath());
                System.out.println(songg.getPath().contains(searchPath));
                System.out.println(songg.getPath() + "    " + songg.getId());
            }
        }
        System.out.println("Wazne czy bedzie id tutaj:");
        System.out.println(song.getId());

        //String songPath = SongAdder.addDoubleSlashes(song.getPath());

        System.out.println(song.getPath());

        final String sql = "SELECT id, path FROM songs.songs WHERE id = '" + song.getId() + "';";
        System.out.println(sql);

/*
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println("In try statement");
            //System.out.println(statement.getGeneratedKeys().next());
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String path = resultSet.getString("path");
                return new Song(id, path);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

 */
        return song;
    }

    public Song findByID(int id) {
        ArrayList<Song> songs =  getAllSongs();
        Song song = new Song("");
        for(Song songg : songs){
            System.out.println("id " + song.getId());
            System.out.println("id of song to delete " + id);
            if(songg.getId().equals(id)){
                System.out.println(songg.getId());
                song.setId(songg.getId());
                song.setPath(songg.getPath());
                System.out.println(songg.getPath() + "    " + songg.getId());
                break;
            }
        }
        System.out.println("Wazne czy bedzie id tutaj:");
        System.out.println(song.getId());
        System.out.println(song.getPath());
        return song;
    }

    //metoda usuwania z bazy danych
    public boolean delete(int id) {
        final String sql = "DELETE FROM songs.songs WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // metoda aktualizująca ściezke
    boolean update(Song song) {
        final String sql = String.format("UPDATE songs.songs SET path = '%s' WHERE id = %d", song.getPath(), song.getId());
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // metoda zwracająca wszystkie piosenki
    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        final String sql = "SELECT * FROM songs.songs;";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String path = resultSet.getString("path");
                songs.add(new Song(id, path));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return songs;
    }

}