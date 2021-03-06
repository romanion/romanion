package ru.geekbrains.java2.server.auth;

import ru.geekbrains.java2.server.connpool.BasicConnectionPool;

import java.sql.*;

public class BaseAuthService implements AuthService {

//    private static class UserData {
//        private String login;
//        private String password;
//        private String username;
//
//        public UserData(String login, String password, String username) {
//            this.login = login;
//            this.password = password;
//            this.username = username;
//        }
//    }

    private BasicConnectionPool connectionPool;

    public BaseAuthService(){
        initConnectionPool();
    }

    public Connection getConnection() {
        return this.connectionPool.getConnection();
    }

    public void releaseConnection(Connection connection){
        connectionPool.releaseConnection(connection);
    }

    public void initConnectionPool(){
        try {
            Class.forName("org.sqlite.JDBC"); //Проверяем наличие JDBC драйвера для работы с БД
            this.connectionPool = BasicConnectionPool.create("jdbc:sqlite:C:/sqlite/java3.db");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        String nick = null;
        Connection localConnection = getConnection();
        try {
            PreparedStatement ps =
                    localConnection.prepareStatement("SELECT nick as nick FROM users WHERE login = ? and password = ?");
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nick = rs.getString("nick");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(localConnection);
            return nick;
        }
    }

    @Override
    public String getHistoryFileByLoginAndPassword(String login, String password) {
        String file = null;
        Connection localConnection = getConnection();
        try {
            PreparedStatement ps =
                    localConnection.prepareStatement("SELECT file_name FROM users WHERE login = ? and password = ?");
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                file = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(localConnection);
            return file;
        }
    }

    @Override
    public void updateHistoryFileName(String login, String password, String historyFile) {
        Connection localConnection = getConnection();
        try {
            PreparedStatement ps =
                    localConnection.prepareStatement("UPDATE users SET file_name = ? WHERE login = ? and password = ?");
            ps.setString(1, historyFile);
            ps.setString(2, login);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(localConnection);
        }
    }

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        try {
            connectionPool.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Сервис аутентификации оставлен");
    }
}
