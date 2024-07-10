package ru.clevertec.check.database;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.postgresql.ds.PGSimpleDataSource;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.utils.DatabaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
public class DatabaseResourceManager {
    private static final String PRODUCT_TABLE_QUERY = "SELECT id, description, price, quantity_in_stock, wholesale_product from product";
    private static final String DISCOUNT_CARD_TABLE_QUERY = "SELECT id, number, amount from discount_card";

    private String url;
    private String username;
    private String password;

    public List<Product> getDatabaseProducts() throws SQLException, IOException {
        List<Product> products = new ArrayList<>();

        if (DatabaseAvailabilityChecker.isNecessaryTablesAvailable(url, username, password)) {
            PGSimpleDataSource dataSource = (PGSimpleDataSource) DatabaseUtils.createDataSource(url, username, password);
            try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(PRODUCT_TABLE_QUERY);
                while (rs.next()) {
                    products.add(
                            Product.builder()
                                    .id(Integer.parseInt(rs.getString(1)))
                                    .description(rs.getString(2))
                                    .price(Double.parseDouble(rs.getString(3)))
                                    .quantityInStock(Integer.parseInt(rs.getString(4)))
                                    .wholesaleProduct(Objects.equals(rs.getString(5), "t"))
                                    .build()
                    );
                }
                return products;
            }
        }
        return null;
    }

    public List<DiscountCard> getDatabaseDiscountCards() throws SQLException, IOException {
        List<DiscountCard> discountCards = new ArrayList<>();
        if (DatabaseAvailabilityChecker.isNecessaryTablesAvailable(url, username, password)) {
            PGSimpleDataSource dataSource = (PGSimpleDataSource) DatabaseUtils.createDataSource(url, username, password);
            try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(DISCOUNT_CARD_TABLE_QUERY);
                while (rs.next()) {
                    discountCards.add(DiscountCard.builder()
                            .id(Integer.parseInt(rs.getString(1)))
                            .number(Integer.parseInt(rs.getString(2)))
                            .discountAmount(Integer.parseInt(rs.getString(3)))
                            .build());
                }
                return discountCards;
            }
        }
        return null;
    }
}
