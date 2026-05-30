package com.yarin.screening;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Component
public class DBInitializer {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @PostConstruct
    public void init() throws Exception {
        // Extract the database name from the URL
        String databaseName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);

        // Prepare a connection to the default database (postgres)
        String defaultUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/") + 1) + "postgres";
        try (Connection connection = DriverManager.getConnection(defaultUrl, dbUsername, dbPassword);
             Statement stmt = connection.createStatement()) {

            // Check if the database exists
            String checkDatabaseQuery = "SELECT 1 FROM pg_database WHERE datname = '" + databaseName + "';";
            var rs = stmt.executeQuery(checkDatabaseQuery);

            if (rs.next()) {
                // Database exists, drop it
                String dropDatabaseQuery = "DROP DATABASE " + databaseName + ";";
                stmt.executeUpdate(dropDatabaseQuery);
                System.out.println("Database " + databaseName + " was deleted.");
            }

            // Create the database
            String createDatabaseQuery = "CREATE DATABASE " + databaseName + ";";
            stmt.executeUpdate(createDatabaseQuery);
            System.out.println("Database " + databaseName + " created successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error dropping/creating the database.", e);
        }
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }
}
