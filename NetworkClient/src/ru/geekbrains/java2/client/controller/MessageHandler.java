package ru.geekbrains.java2.client.controller;

import java.io.IOException;

@FunctionalInterface
public interface MessageHandler {
    void handle(String message) throws IOException;
}
