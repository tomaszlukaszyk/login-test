package com.codecool.test.login;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
        httpServer.createContext("/login", new Login());
        httpServer.createContext("/static", new Static());
        httpServer.createContext("/add_user", new AddUser());
        httpServer.setExecutor(null);
        httpServer.start();
    }
}
