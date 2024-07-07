package ru.clevertec.check.core;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import ru.clevertec.check.models.CheckInfo;
import ru.clevertec.check.models.Error;
import ru.clevertec.check.models.ProductCheckRecord;
import ru.clevertec.check.utils.DecimalRoundPrecisionUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class ResultTableWriter implements Writer{
    private final String RESULT_FILENAME = "./result.csv";

    private final String[] RESULT_DATETIME_HEADERS = {"Date", "Time"};
    private final String[] RESULT_PRODUCTS_HEADERS = {"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"};
    private final String[] RESULT_DISCOUNT_CARD_HEADERS = {"DISCOUNT CARD", "DISCOUNT PERCENTAGE"};
    private final String[] RESULT_TOTAL_PRICE_HEADERS = {"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"};

    public void writeError(CheckInfo checkInfo) throws IOException {
        File file;
        if (checkInfo.getSaveToFile() != null) {
            file = new File(checkInfo.getSaveToFile());
        }
        else {
            file = new File(RESULT_FILENAME);
        }
        if (!file.exists()) {
            Path filePath = file.toPath();
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("ERROR\n");
            fw.write(checkInfo.getError().toString().replaceAll("_", " "));
        }
    }

    public void writeInfo(CheckInfo checkInfo) throws IOException {
        File file;
        if (checkInfo.getSaveToFile() != null) {
            file = new File(checkInfo.getSaveToFile());
        }
        else {
            file = new File(RESULT_FILENAME);
        }
        if (!file.exists()) {
            Path filePath = file.toPath();
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }
        CSVPrinter printer = new CSVPrinter(new PrintWriter(file), CSVFormat.DEFAULT.builder().setDelimiter(";").build());

        double totalDiscount  = checkInfo.getProductCheckRecordList().stream().mapToDouble(ProductCheckRecord::getDiscount).sum();

        if (checkInfo.getDiscountCard() != null) {
            printPartialData(checkInfo, printer, RESULT_DISCOUNT_CARD_HEADERS);
            printer.printRecord(checkInfo.getDiscountCard().getNumber(), checkInfo.getDiscountCard().getDiscountAmount() + "%");
            printer.println();
            printer.printRecord((Object[]) RESULT_TOTAL_PRICE_HEADERS);
            printer.printRecord((DecimalRoundPrecisionUtils.round(checkInfo.getTotalPrice(), 2)) + "$", (DecimalRoundPrecisionUtils.round(totalDiscount, 2)) + "$", DecimalRoundPrecisionUtils.round(checkInfo.getTotalPrice() - totalDiscount, 2) + "$");            printer.flush();
            return;
        }

        printPartialData(checkInfo, printer, RESULT_TOTAL_PRICE_HEADERS);
        printer.printRecord((DecimalRoundPrecisionUtils.round(checkInfo.getTotalPrice(), 2)) + "$", (DecimalRoundPrecisionUtils.round(totalDiscount, 2)) + "$", DecimalRoundPrecisionUtils.round(checkInfo.getTotalPrice() - totalDiscount, 2) + "$");
        printer.flush();
    }

    private void printPartialData(CheckInfo checkInfo, CSVPrinter printer, Object[] resultDiscountCardHeaders) throws IOException {
        printer.printRecord((Object[]) RESULT_DATETIME_HEADERS);
        printer.printRecord(checkInfo.getCheckTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), checkInfo.getCheckTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        printer.println();
        printer.printRecord((Object[]) RESULT_PRODUCTS_HEADERS);
        checkInfo.getProductCheckRecordList().forEach(f -> {
            try {
                printer.printRecord(f.getQuantity(), f.getDescription(), f.getPrice() + "$", f.getDiscount() + "$", f.getTotal() + "$");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        printer.println();
        printer.printRecord(resultDiscountCardHeaders);
    }
}
