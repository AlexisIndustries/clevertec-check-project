package ru.clevertec.check.database;

import org.postgresql.ds.PGSimpleDataSource;
import ru.clevertec.check.core.ConsoleWriter;
import ru.clevertec.check.core.ResultTableWriter;
import ru.clevertec.check.models.CheckInfo;
import ru.clevertec.check.models.Error;
import ru.clevertec.check.utils.DatabaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAvailabilityChecker {
    public static boolean isDatabaseAvailable(String url, String username, String password) {
        try {
            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUrl(url);
            dataSource.setUser(username);
            dataSource.setPassword(password);

            try {
                Connection conn = dataSource.getConnection();
                return conn != null;
            } catch (SQLException e) {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isDatabaseAvailable(String url, String username, String password, String databaseName) throws IOException {
        try {
            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUrl(url);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setDatabaseName(databaseName);

            try (Connection conn = dataSource.getConnection()) {
                return conn != null;
            } catch (SQLException e) {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isNecessaryTablesAvailable(String url, String username, String password) throws SQLException, IOException {
        if (isDatabaseAvailable(url, username, password, "clevertec_check")) {
            PGSimpleDataSource dataSource = (PGSimpleDataSource) DatabaseUtils.createDataSource(url, username, password);
            boolean productsExists = false, discountCardsExists = false;

            try (Connection connection = dataSource.getConnection(); Statement stmt1 = connection.createStatement(); Statement stmt2 = connection.createStatement()) {
                String queryProductTable = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'product' and table_catalog = 'clevertec_check'";
                String queryDiscountCardTable = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'discount_card' and table_catalog = 'clevertec_check'";

                try (ResultSet rs1 = stmt1.executeQuery(queryProductTable); ResultSet rs2 = stmt2.executeQuery(queryDiscountCardTable)) {
                    rs1.next();
                    rs2.next();

                    productsExists = Integer.parseInt(rs1.getString(1)) > 0;
                    discountCardsExists = Integer.parseInt(rs2.getString(1)) > 0;
                }
            }
            return productsExists && discountCardsExists;
        }

        return false;
    }

    public static boolean isNecessaryTablesAvailable(String url, String username, String password, String databaseName) throws SQLException, IOException {
        if (isDatabaseAvailable(url, username, password, databaseName)) {
            PGSimpleDataSource dataSource = (PGSimpleDataSource) DatabaseUtils.createDataSource(url, username, password, databaseName);
            boolean productsExists = false, discountCardsExists = false;

            try (Connection connection = dataSource.getConnection(); Statement stmt1 = connection.createStatement(); Statement stmt2 = connection.createStatement()) {
                String queryProductTable = String.format("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'product' and table_catalog = '%s'", databaseName);
                String queryDiscountCardTable = String.format("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'discount_card' and table_catalog = '%s'", databaseName);

                try (ResultSet rs1 = stmt1.executeQuery(queryProductTable); ResultSet rs2 = stmt2.executeQuery(queryDiscountCardTable)) {
                    rs1.next();
                    rs2.next();

                    productsExists = Integer.parseInt(rs1.getString(1)) > 0;
                    discountCardsExists = Integer.parseInt(rs2.getString(1)) > 0;
                }
            }
            return productsExists && discountCardsExists;
        }

        return false;
    }
}
