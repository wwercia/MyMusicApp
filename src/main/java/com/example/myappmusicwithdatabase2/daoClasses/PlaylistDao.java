package com.example.myappmusicwithdatabase2.daoClasses;


import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class PlaylistDao {

    private final Connection connection;

    public PlaylistDao() {
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
    public void save(Playlist playlist) {
        final String sql = "INSERT INTO songs.playlists (playlist_name, x, y) VALUES ('"+ playlist.getName() + "', "+ playlist.getX() + ", " + playlist.getY() + ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generetedKeys = statement.getGeneratedKeys();
            if (generetedKeys.next()) {
                playlist.setId(generetedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql);
        }
    }

    //metoda usuwania z bazy danych
    boolean delete(int id) {
        final String sql = "DELETE FROM songs.playlists WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // metoda aktualizująca nazwe
    boolean update(Playlist playlist) {
        final String sql = String.format("UPDATE songs.playlists SET playlist_name = '%s' WHERE id = %d", playlist.getName(), playlist.getId());
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // metoda wyszukująca playliste w bazie
    Optional<Playlist> finsByName(Playlist searchPlaylist) {
        final String sql = "SELECT id, playlist_name FROM songs.playlists WHERE playlist_name = '" + searchPlaylist + "'";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String playlistName = resultSet.getString("playlist_name");
                Integer x = resultSet.getInt("x");
                Integer y = resultSet.getInt("y");
                return Optional.of(new Playlist(id, playlistName, x, y));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    // metoda zwracająca wszystkie playlisty
    public ArrayList<Playlist> getAllPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        final String sql = "SELECT * FROM songs.playlists;";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("playlist_name");
                Integer x = resultSet.getInt("x");
                Integer y = resultSet.getInt("y");
                playlists.add(new Playlist(id, name, x,y));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return playlists;
    }


    public void createTableForPlaylist(String name){
        final String sql = String.format("""
                                CREATE TABLE %s (
                                                    id INT PRIMARY KEY AUTO_INCREMENT
                                );
                """, name);
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql);
            throw new RuntimeException();
        }

        final String sql2 = String.format("""
                                ALTER TABLE %s
                                ADD song_id INT NOT NULL UNIQUE,
                                ADD FOREIGN KEY (song_id) REFERENCES songs (id);
                """, name);
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql2);
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql2);
            throw new RuntimeException();
        }
    }


    public void changeNameOfPlaylist(String oldPlaylistName, String newPlaylistName){
        String newName = newPlaylistName.trim();
        String oldName = oldPlaylistName.trim();
        String sql = String.format("""
                ALTER TABLE `songs`.`%s`
                RENAME TO  `songs`.`%s` ;
                """, oldName, newName);
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql + ";");
            throw new RuntimeException();
        }

        ArrayList<Playlist> playlists = getAllPlaylists();
        Playlist playlist = null;
        for(Playlist playlist1 : playlists){
            if(playlist1.getName().equals(oldName)){
                playlist = playlist1;
            }
        }

        String sql2 = String.format("UPDATE `songs`.`playlists` SET `playlist_name` = '%s' WHERE (`id` = '%d');", newName, playlist.getId());
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql2);
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql2);
            throw new RuntimeException();
        }
    }

    public void changeNameOfPlaylistOnlyInPlaylists(String oldPlaylistName, String newName){
        ArrayList<Playlist> playlists = getAllPlaylists();
        Playlist playlist = null;
        for(Playlist playlist1 : playlists){
            if(playlist1.getName().equals(oldPlaylistName)){
                playlist = playlist1;
            }
        }
        String sql2 = String.format("UPDATE `songs`.`playlists` SET `playlist_name` = '%s' WHERE (`id` = '%d');",newName, playlist.getId());
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql2);
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql2);
            throw new RuntimeException();
        }
    }

}