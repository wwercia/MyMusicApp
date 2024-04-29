package com.example.myappmusicwithdatabase2.daoClasses;


import java.sql.*;
import java.util.ArrayList;

public class UniversalPlaylistsDao {

    private final Connection connection;

    public UniversalPlaylistsDao() {
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
    public void save(String nameOfTable, Song song) {

        System.out.println(nameOfTable);

        final String sql = String.format("INSERT INTO songs.%s (song_id) VALUES (%d)", nameOfTable, song.getId());
        System.out.println(sql);
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

    public boolean delete(String nameOfTable, Song song) {
        System.out.println("deleting song " + song.getPath() + " from table " + nameOfTable);
        final String sql = String.format("DELETE FROM songs.%s WHERE song_id = %d", nameOfTable, song.getId());
        System.out.println(sql);
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql);
            throw new RuntimeException(e);
        }
    }

    public boolean delete(int id) {
        final String sql = "DELETE FROM songs.songs WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // metoda wyszukująca piosenke  w bazie
    /*
    Optional<UniversalPlaylists> findByMethodName(String nameOfTable, UniversalPlaylists searchUniversalPlaylistsDao) {
        final String sql = String.format("SELECT id, song_id FROM songs.%s WHERE  = '" + searchUniversalPlaylistsDao.ge + "'";)
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String method_name = resultSet.getString("method_name");
                return Optional.of(new UniversalPlaylists(id, method_name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


     */


    public ArrayList<UniversalPlaylists> getAllsongs(String playlistName) {
        ArrayList<UniversalPlaylists> songs = new ArrayList<>();
        final String sql = String.format("SELECT * FROM `songs`.`%s`;", playlistName);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int songId = resultSet.getInt("song_id");
                songs.add(new UniversalPlaylists(id, songId));
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql);
            throw new RuntimeException(e);
        }
        return songs;
    }



}