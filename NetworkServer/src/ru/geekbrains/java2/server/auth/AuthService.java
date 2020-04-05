package ru.geekbrains.java2.server.auth;

public interface AuthService {

    String getUsernameByLoginAndPassword(String login, String password);
    String getHistoryFileByLoginAndPassword(String login, String password);
    void updateHistoryFileName(String login, String password, String historyFile);

    void start();
    void stop();

}
