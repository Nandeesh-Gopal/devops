package com.example;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            File file = new File("src/main/resources/index.html");
            byte[] response = new byte[0];

            try {
                response = java.nio.file.Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                response = "<h1>File not found</h1>".getBytes();
            }

            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        });

        server.start();

        System.out.println("Server started at http://localhost:8080");
    }
}