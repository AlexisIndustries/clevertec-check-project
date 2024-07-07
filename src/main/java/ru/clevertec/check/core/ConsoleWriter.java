package ru.clevertec.check.core;

import ru.clevertec.check.models.CheckInfo;
import ru.clevertec.check.models.Error;
import ru.clevertec.check.models.ProductCheckRecord;
import ru.clevertec.check.utils.DecimalRoundPrecisionUtils;

import java.time.format.DateTimeFormatter;

public class ConsoleWriter implements Writer {
    public void writeError(CheckInfo checkInfo) {
        System.out.println("ERROR\n" + checkInfo.getError());
        System.out.println();
        System.out.println("Произошла ошибка транзакции!");
        switch (checkInfo.getError()) {
            case NOT_ENOUGH_MONEY -> System.out.println("У вас недостаточно денег на балансе. Пожалуйста укажите большую сумму.");
            case BAD_REQUEST -> System.out.println("Вы ввели неправильный запрос. Пожалуйста, перепроверьте аргументы которые вы ввели.");
            case INTERNAL_SERVER_ERROR -> System.out.println("Произошла внутренняя ошибка.");
        }
    }

    public void writeInfo(CheckInfo checkInfo) {
        double totalDiscount = checkInfo.getProductCheckRecordList().stream().mapToDouble(ProductCheckRecord::getDiscount).sum();

        System.out.println("Транзакция выполнена успешно!");
        System.out.println("Содержание чека:");
        if (checkInfo.getDiscountCard() != null) {
            printPartialData(checkInfo);
            System.out.println();
            System.out.println("Дисконтная карта: номер " + checkInfo.getDiscountCard().getNumber() + ", размер скидки: " + checkInfo.getDiscountCard().getDiscountAmount());
            System.out.println("Итого: " + checkInfo.getTotalPrice() + ", размер скидки: " + totalDiscount + ", итого со скидкой: " + DecimalRoundPrecisionUtils.round(checkInfo.getTotalPrice() - totalDiscount, 2));
            System.out.println();
            System.out.println("Чек записан в файл result.csv.");
            return;
        }
        printPartialData(checkInfo);
        System.out.println("Дискотной карты нет.");
        System.out.println("Итого: " + DecimalRoundPrecisionUtils.round(checkInfo.getTotalPrice(), 2) + ", размер скидки: " + DecimalRoundPrecisionUtils.round(totalDiscount, 2) + ", итого со скидкой: " + DecimalRoundPrecisionUtils.round(checkInfo.getTotalPrice() - totalDiscount, 2));
        System.out.println();
        System.out.println("Чек записан в файл result.csv.");
    }

    private void printPartialData(CheckInfo checkInfo) {
        System.out.println("Время совершения покупки: " + checkInfo.getCheckTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + checkInfo.getCheckTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println();
        System.out.println("Перечень товаров в чеке: название, цена, количество, всего, скидка");
        System.out.println();
        checkInfo.getProductCheckRecordList().forEach(f -> System.out.println(f.getDescription() + "\t\t" + f.getPrice() + "\t\t" + f.getQuantity() + "\t\t" + f.getTotal() + "\t\t" + f.getDiscount()));
    }
}
