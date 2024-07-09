package ru.clevertec.check.utils;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DatabaseUtils {
    public static DataSource createDataSource(String url, String username, String password) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setDatabaseName("clevertec_check");

        return dataSource;
    }

    public static DataSource createDataSource(String url, String username, String password, String databaseName) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setDatabaseName(databaseName);

        return dataSource;
    }

    public static DataSource createDataSourceWithoutDatabase(String url, String username, String password) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
