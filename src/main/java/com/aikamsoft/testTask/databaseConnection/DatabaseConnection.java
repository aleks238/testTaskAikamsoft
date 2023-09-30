package com.aikamsoft.testTask.databaseConnection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DatabaseConnection {
    private static Connection connection;
    private static final String URL = "jdbc:postgresql://localhost:5432/dump_store";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                if (connection != null) {
                    log.info("Connected to the database!");
                } else {
                    log.info("Failed to make connection!");
                }
            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
