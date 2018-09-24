package com.codecool.test.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbLoginDAO implements LoginDAO {

    private Connection connection;

    public DbLoginDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getPasswordByLogin(String login) {
        String sql = "SELECT password FROM users WHERE login = ?";
        String pass = "";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                pass = resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pass;
    }

    @Override
    public boolean addUser(String login, String pass) {
        String sql = "INSERT INTO users(login, password) VALUES (?, ?)";
        int i = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, pass);
            i = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i == 1;
    }
}
