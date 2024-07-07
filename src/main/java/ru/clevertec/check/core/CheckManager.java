package ru.clevertec.check.core;

import ru.clevertec.check.models.CheckInfo;

import java.io.IOException;

public class CheckManager {
    private final ResultTableWriter resultTableWriter = new ResultTableWriter();
    private final ConsoleWriter consoleWriter = new ConsoleWriter();

    public void process(CheckInfo checkInfo) throws IOException {
        switch (checkInfo.getError()) {
            case NO_ERROR -> processNoErrorCheckInfo(checkInfo);
            case BAD_REQUEST -> processBadRequestCheckInfo(checkInfo);
            case NOT_ENOUGH_MONEY -> processNotEnoughMoneyCheckInfo(checkInfo);
        }
    }

    private void processNotEnoughMoneyCheckInfo(CheckInfo checkInfo) throws IOException {
        resultTableWriter.writeError(checkInfo);
        consoleWriter.writeError(checkInfo);
    }

    private void processBadRequestCheckInfo(CheckInfo checkInfo) throws IOException {
        resultTableWriter.writeError(checkInfo);
        consoleWriter.writeError(checkInfo);
    }

    private void processNoErrorCheckInfo(CheckInfo checkInfo) throws IOException {
        resultTableWriter.writeInfo(checkInfo);
        consoleWriter.writeInfo(checkInfo);
    }
}
