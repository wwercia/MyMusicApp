package com.example.myappmusicwithdatabase2.daoClasses;


import java.sql.*;
import java.util.ArrayList;

public class FrameDao {

    private final Connection connection;

    public FrameDao() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/songs?serverTimezone=UTC", "root", "wwercia1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // metoda zapisywania
    public void save(Frame frame) {
        final String sql = "INSERT INTO songs.frame (height, width) VALUES ("+ frame.getHeight() + ", "+ frame.getWidth() + ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generetedKeys = statement.getGeneratedKeys();
            if (generetedKeys.next()) {
                frame.setId(generetedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with the query: " + sql);
        }
    }

    //metoda usuwania z bazy danych
    public boolean delete(int id) {
        final String sql = "DELETE FROM songs.frame WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // metoda aktualizująca ściezke
    public boolean update(Frame searchFrame) {
        final String sql = String.format("UPDATE songs.frame SET height = %d, width = %d WHERE id = %d", searchFrame.getHeight(), searchFrame.getWidth(), searchFrame.getId());
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // metoda zwracająca wszystkie frame
    public ArrayList<Frame> getAllFrames() {
        ArrayList<Frame> frames = new ArrayList<>();
        final String sql = "SELECT * FROM songs.frame;";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Integer height = resultSet.getInt("height");
                Integer width = resultSet.getInt("width");
                frames.add(new Frame(id, height, width));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return frames;
    }

}