package com.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionManager {
    private final static Logger logger = LogManager.getLogger();
    private static Connection connection;
    private static String connectionString = "jdbc:postgresql://dumbo.db.elephantsql.com/xvwxelvu";
    private static String username = "xvwxelvu";
    private static String password = "PHm0jpvwUdwMtElhSwmHCA85Ucl9G6l8";

    public static Connection getConnection() {
        try {
            if (connection == null) {
                Class.forName("org.postgresql.Driver");
                connection = Objects.requireNonNull(DriverManager.getConnection(connectionString, username, password));
            }
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("ConnectionManager-error: ", e);
        }
        return null;
    }

    @Override
    public void finalize() {
        try {
            Objects.requireNonNull(ConnectionManager.getConnection()).close();
        } catch (SQLException e) {
            logger.error("ConnectionManager-error: ", e);
        }
    }
}
