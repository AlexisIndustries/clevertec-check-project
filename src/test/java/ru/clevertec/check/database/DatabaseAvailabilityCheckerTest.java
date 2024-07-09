package ru.clevertec.check.database;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.check.utils.DatabaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


@Testcontainers
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class DatabaseAvailabilityCheckerTest {

    private static final int containerPort = 5432 ;
    private static final int localPort = 7890 ;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer<>()
            .withDatabaseName("clevertec")
            .withUsername("postgres")
            .withPassword("test12345")
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd ->
                    cmd.withHostConfig(
                            new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
                    ));

    @Test
    void isDatabaseAvailable() {
        boolean isAvailable = DatabaseAvailabilityChecker.isDatabaseAvailable("jdbc:postgresql://localhost:7890/clevertec", "postgres", "test12345");

        Assertions.assertTrue(isAvailable);
    }

    @Test
    void testIsDatabaseAvailable() throws IOException {
        boolean isAvailable = DatabaseAvailabilityChecker.isDatabaseAvailable("", "", "", "");

        Assertions.assertFalse(isAvailable);
    }
    @Test
    void testIsDatabaseAvailableTrue() throws IOException {
        boolean isAvailable = DatabaseAvailabilityChecker.isDatabaseAvailable("jdbc:postgresql://localhost:7890/clevertec", "postgres", "test12345", "");

        Assertions.assertTrue(isAvailable);
    }

    @Test
    void testIsDatabaseAvailableWithoutDatabase() throws IOException {
        boolean isAvailable = DatabaseAvailabilityChecker.isDatabaseAvailable("", "", "");

        Assertions.assertFalse(isAvailable);
    }
    @Test
    void testIsDatabaseAvailableWithDatabaseCleverTec() throws IOException {
        PGSimpleDataSource dataSource = (PGSimpleDataSource) DatabaseUtils.createDataSourceWithoutDatabase("jdbc:postgresql://localhost:7890/", "postgres", "test12345");
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
            statement.execute("CREATE DATABASE clevertec_check;");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        boolean isAvailable = DatabaseAvailabilityChecker.isDatabaseAvailable("jdbc:postgresql://localhost:7890/", "postgres", "test12345");

        Assertions.assertTrue(isAvailable);
    }
        @Test
    void isNecessaryTablesAvailable() {
        PGSimpleDataSource dataSource = (PGSimpleDataSource) DatabaseUtils.createDataSource("jdbc:postgresql://localhost:7890/clevertec", "postgres", "test12345", "clevertec");
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement(); Statement statement1 = connection.createStatement()) {
            statement.execute("CREATE TABLE product (\n" +
                    "    id bigserial,\n" +
                    "    description varchar(50),\n" +
                    "    price decimal,\n" +
                    "    quantity_in_stock integer,\n" +
                    "    wholesale_product boolean\n" +
                    ");"
            );

            statement1.execute("CREATE TABLE discount_card (\n" +
                    "    id bigserial,\n" +
                    "    number integer,\n" +
                    "    amount smallint check ( amount between 0 and 100)\n" +
                    ");");

            boolean isTablesAvailable = DatabaseAvailabilityChecker.isNecessaryTablesAvailable("jdbc:postgresql://localhost:7890/clevertec", "postgres", "test12345", "clevertec");
            Assertions.assertTrue(isTablesAvailable);

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}