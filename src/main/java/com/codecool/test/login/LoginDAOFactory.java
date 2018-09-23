package com.codecool.test.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginDAOFactory {

    final String URL = "jdbc:postgresql://localhost:5432/logintest";
    final String USER = "logintest";
    final String PASS = "cookie";

    private static Connection connection = null;

    public LoginDAOFactory() {
        if (connection == null) {
            createConnection();
        }
    }

    public LoginDAO getDao() {
        return new DbLoginDAO(connection);
    }

    private void createConnection() {
        try {
            connection = DriverManager.getConnection(this.URL, this.USER, this.PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
