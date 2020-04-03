package ru.geekbrains.java2.server.connpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool implements ConnectionPool {

    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private final static int INITIAL_POOL_SIZE = 10;

    BasicConnectionPool(List<Connection> connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static BasicConnectionPool create(
            String url) throws SQLException {

        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url));
        }
        return new BasicConnectionPool(pool);
    }

    @Override
    public Connection getConnection() {
        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        printState();
        return connection;
    }

    public void printState() {
        System.out.printf("Pool size = %d Used = %d \n", connectionPool.size(), usedConnections.size());
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        printState();
        return usedConnections.remove(connection);
    }

    private static Connection createConnection(
            String url)
            throws SQLException {
        return DriverManager.getConnection(url);
    }

    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }
}
