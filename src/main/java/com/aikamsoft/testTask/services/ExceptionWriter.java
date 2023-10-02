package com.aikamsoft.testTask.services;

import org.json.simple.JSONObject;

import java.io.IOException;
@SuppressWarnings("unchecked")
public class ExceptionWriter {

    public void writeToFileIOException(String outputFile) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "error");
        jsonObject.put("message", "Неправильный путь к входному файлу");
        new FileWriterService().writeResultsToFile(outputFile,jsonObject);
    }
    public void writeToFileParseException(String outputFile) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "error");
        jsonObject.put("message", "Ошибка при обработке файла: ParseException");
        new FileWriterService().writeResultsToFile(outputFile,jsonObject);
    }
}
