package com.codecool.test.login;

public interface LoginDAO {
    String getPasswordByLogin(String login);
    boolean addUser(String login, String pass);
}
