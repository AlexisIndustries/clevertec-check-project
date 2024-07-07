package ru.clevertec.check.core;

import ru.clevertec.check.models.*;
import ru.clevertec.check.models.Error;
import ru.clevertec.check.utils.DecimalRoundPrecisionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionManager {
    public List<Product> products = ResourceManager.getResourceProducts();

    public TransactionManager() throws IOException {
    }

    public CheckInfo process(TransactionInfo transactionInfo) throws IOException {
        if (transactionInfo != null && transactionInfo.getError() == Error.BAD_REQUEST) {
            return CheckInfo.builder()
                    .error(Error.BAD_REQUEST)
                    .build();
        }

        assert transactionInfo != null;
        if (products == null || products.isEmpty()) {
            return CheckInfo.builder()
                    .error(Error.BAD_REQUEST)
                    .build();
        }
        HashMap<Product, Integer> productsWithQuantity = convertIdQuantityToProducts(transactionInfo.getIdQuantityPairs());

        if (productsWithQuantity == null || productsWithQuantity.isEmpty()) {
            return CheckInfo.builder()
                    .error(Error.BAD_REQUEST)
                    .build();
        }

        if (transactionInfo.getDiscountCard() != 0 && transactionInfo.getDiscountCard() <= 9999 && transactionInfo.getDiscountCard() >= 1000) {
            List<DiscountCard> discountCards = ResourceManager.getResourceDiscountCards();
            Optional<DiscountCard> validDiscountCard = discountCards.stream().filter(ds -> ds.getNumber() == transactionInfo.getDiscountCard()).findFirst();

            if (validDiscountCard.isPresent()) {
                List<ProductCheckRecord> productCheckRecords = new LinkedList<>();
                for (Map.Entry<Product, Integer> entry : productsWithQuantity.entrySet()) {
                    if (entry.getKey().isWholesaleProduct()) {
                        if (entry.getKey().getQuantityInStock() < entry.getValue()) {
                            return CheckInfo.builder()
                                    .error(Error.BAD_REQUEST)
                                    .build();
                        }
                        productCheckRecords.add(processWholesaleProduct(entry.getKey(), entry.getValue(), validDiscountCard.get()));
                        continue;
                    }
                    if (entry.getKey().getQuantityInStock() < entry.getValue()) {
                        return CheckInfo.builder()
                                .error(Error.BAD_REQUEST)
                                .build();
                    }
                    productCheckRecords.add(processNonWholesaleProduct(entry.getKey(), entry.getValue(), validDiscountCard.get()));
                }
                return checkTotalPriceAndBalance(transactionInfo, productCheckRecords, validDiscountCard.get());
            }
            List<ProductCheckRecord> productCheckRecords = new LinkedList<>();
            DiscountCard customDiscountCard = DiscountCard.builder().discountAmount(2).number(transactionInfo.getDiscountCard()).build();
            for (Map.Entry<Product, Integer> entry : productsWithQuantity.entrySet()) {
                if (entry.getKey().isWholesaleProduct()) {
                    if (entry.getKey().getQuantityInStock() < entry.getValue()) {
                        return CheckInfo.builder()
                                .error(Error.BAD_REQUEST)
                                .build();
                    }
                    productCheckRecords.add(processWholesaleProduct(entry.getKey(), entry.getValue(), customDiscountCard));
                    continue;
                }
                if (entry.getKey().getQuantityInStock() < entry.getValue()) {
                    return CheckInfo.builder()
                            .error(Error.BAD_REQUEST)
                            .build();
                }
                productCheckRecords.add(processNonWholesaleProduct(entry.getKey(), entry.getValue(), customDiscountCard));
            }
            return checkTotalPriceAndBalance(transactionInfo, productCheckRecords, customDiscountCard);
        }
        else {
            List<ProductCheckRecord> productCheckRecords = new LinkedList<>();
            for (Map.Entry<Product, Integer> entry : productsWithQuantity.entrySet()) {
                if (entry.getKey().isWholesaleProduct()) {
                    if (entry.getKey().getQuantityInStock() < entry.getValue()) {
                        return CheckInfo.builder()
                                .error(Error.BAD_REQUEST)
                                .build();
                    }
                    productCheckRecords.add(processWholesaleProductWithoutDiscountCardPresent(entry.getKey(), entry.getValue()));
                    continue;
                }
                if (entry.getKey().getQuantityInStock() < entry.getValue()) {
                    return CheckInfo.builder()
                            .error(Error.BAD_REQUEST)
                            .build();
                }
                productCheckRecords.add(processNonWholesaleProductWithoutDiscountCardPresent(entry.getKey(), entry.getValue()));
            }
            return checkTotalPriceAndBalance(transactionInfo, productCheckRecords, null);
        }
    }

    private ProductCheckRecord processNonWholesaleProductWithoutDiscountCardPresent(Product key, Integer value) {
        return ProductCheckRecord.builder()
                .price(key.getPrice())
                .total(DecimalRoundPrecisionUtils.round(key.getPrice() * value.doubleValue(), 2))
                .discount(0.00)
                .description(key.getDescription())
                .quantity(value)
                .build();
    }

    private ProductCheckRecord processWholesaleProductWithoutDiscountCardPresent(Product key, Integer value) {
        if (value >= 5) {
            return ProductCheckRecord.builder()
                    .price(key.getPrice())
                    .total(DecimalRoundPrecisionUtils.round(key.getPrice() * value.doubleValue(), 2))
                    .discount(DecimalRoundPrecisionUtils.round(0.1 * key.getPrice() * value, 2))
                    .description(key.getDescription())
                    .quantity(value)
                    .build();

        }
        return ProductCheckRecord.builder()
                .price(key.getPrice())
                .total(DecimalRoundPrecisionUtils.round(key.getPrice() * value, 2))
                .discount(0.00)
                .description(key.getDescription())
                .quantity(value)
                .build();
    }

    private ProductCheckRecord processNonWholesaleProduct(Product key, Integer value, DiscountCard discountCard) {
        return ProductCheckRecord.builder()
                .price(key.getPrice())
                .total(DecimalRoundPrecisionUtils.round(key.getPrice() * value.doubleValue(), 2))
                .discount(DecimalRoundPrecisionUtils.round((double) discountCard.getDiscountAmount() / 100 * key.getPrice() * value, 2))
                .description(key.getDescription())
                .quantity(value)
                .build();
    }

    private ProductCheckRecord processWholesaleProduct(Product key, Integer value, DiscountCard discountCard) {
        if (value >= 5) {
            return ProductCheckRecord.builder()
                    .price(key.getPrice())
                    .total(DecimalRoundPrecisionUtils.round(key.getPrice() * value.doubleValue(), 2))
                    .discount(DecimalRoundPrecisionUtils.round(0.1 * key.getPrice() * value, 2))
                    .description(key.getDescription())
                    .quantity(value)
                    .build();

        }
        return ProductCheckRecord.builder()
                .price(key.getPrice())
                .total(DecimalRoundPrecisionUtils.round(key.getPrice() * value.doubleValue(), 2))
                .discount(DecimalRoundPrecisionUtils.round((double) discountCard.getDiscountAmount() / 100 * key.getPrice() * value, 2))
                .description(key.getDescription())
                .quantity(value)
                .build();

    }

    private CheckInfo checkTotalPriceAndBalance(TransactionInfo transactionInfo, List<ProductCheckRecord> productCheckRecordList, DiscountCard discountCard) throws IOException {
        double totalPrice = productCheckRecordList.stream().mapToDouble(ProductCheckRecord::getTotal).sum();
        double totalDiscount = productCheckRecordList.stream().mapToDouble(ProductCheckRecord::getDiscount).sum();

        if (totalPrice - totalDiscount > transactionInfo.getBalanceDebitCard()) {
            return CheckInfo.builder()
                    .error(Error.NOT_ENOUGH_MONEY)
                    .build();
        }

        return CheckInfo.builder()
                .checkTime(LocalDateTime.now())
                .productCheckRecordList(productCheckRecordList)
                .totalPrice(totalPrice)
                .discountCard(discountCard)
                .error(Error.NO_ERROR)
                .build();
    }

    private HashMap<Product, Integer> convertIdQuantityToProducts(HashMap<Integer, Integer> idQuantityPairs) throws IOException {
        HashMap<Product, Integer> products = new HashMap<>();
        boolean isFall = false;
        for (Map.Entry<Integer, Integer> entry : idQuantityPairs.entrySet()) {
            if (entry.getKey() < 0 || entry.getKey() > 20) {
                isFall = true;
                break;
            }
            Product product = this.products.get(entry.getKey() - 1);
            Integer quantity = entry.getValue();
            products.put(product, quantity);
        }
        return isFall ? null : products;
    }
}
