package com.aikamsoft.testTask.services;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

import java.io.IOException;
@Slf4j
@SuppressWarnings("unchecked")
public class ExceptionWriter {
    public void  writeParseException(String outputFile) throws IOException {
        log.info("Неправильный формат входных данных");
        JSONObject wrongDataFormat = new JSONObject();
        wrongDataFormat.put("message", "Неправильный формат входных данных");
        new FileWriterService().writeResultsToFile(outputFile, wrongDataFormat);
    }

    public void  writeDateTimeParseException(String outputFile) throws IOException {
        log.info("Неправильный формат даты");
        JSONObject wrongDataFormat = new JSONObject();
        wrongDataFormat.put("message", "Неправильный формат даты");
        new FileWriterService().writeResultsToFile(outputFile, wrongDataFormat);
    }

}
