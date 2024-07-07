package ru.clevertec.check.core;

import ru.clevertec.check.models.CheckInfo;
import ru.clevertec.check.models.Error;

import java.io.IOException;

public interface Writer {
    void writeInfo(CheckInfo checkInfo) throws IOException;
    void writeError(CheckInfo checkInfo) throws IOException;
}
