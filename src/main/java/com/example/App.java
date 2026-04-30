package com.example;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;

public class App {
    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Serve HTML
        server.createContext("/", exchange -> {
            InputStream is = App.class.getClassLoader().getResourceAsStream("index.html");
            byte[] response = is.readAllBytes();

            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        });

        // Calculator API
        server.createContext("/calc", exchange -> {
            URI uri = exchange.getRequestURI();
            String query = uri.getQuery();

            HashMap<String, String> params = new HashMap<>();
            for (String pair : query.split("&")) {
                String[] kv = pair.split("=");
                params.put(kv[0], kv[1]);
            }

            double a = Double.parseDouble(params.get("a"));
            double b = Double.parseDouble(params.get("b"));
            String op = params.get("op");

            double result = 0;

            switch (op) {
                case "+": result = a + b; break;
                case "-": result = a - b; break;
                case "*": result = a * b; break;
                case "/": result = (b != 0) ? a / b : 0; break;
            }

            String response = String.valueOf(result);

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Server running at http://localhost:8080");
    }
}