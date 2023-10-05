package com.aikamsoft.testTask;

import com.aikamsoft.testTask.services.ExceptionWriter;
import com.aikamsoft.testTask.services.FileWriterService;
import com.aikamsoft.testTask.services.SearchService;
import com.aikamsoft.testTask.services.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.format.DateTimeParseException;

@Slf4j
@SuppressWarnings("unchecked")
public class Main {
    public static void main(String[] args) {
        Main mainObject = new Main();
        try {
            mainObject.parseInputArgs(mainObject, args);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void parseInputArgs(Main mainObject, String[] args) throws IOException, ParseException {
        if (args.length < 3) {
            throw new RuntimeException("Неправильное количество входных параметров");
        }
        String operation = args[0];
        String outputFile = args[1];
        String inputFile = args[2];
        if (operation.equals("search")) {
            mainObject.findCustomersByCriteria(inputFile,outputFile);
        } else if (operation.equals("stat")) {
            mainObject.getStatistics(inputFile,outputFile);
        } else {
            throw new RuntimeException("Введен некорректный тип операции");
        }
    }

    private void findCustomersByCriteria(String inputFile,String outputFile) throws IOException {
        JSONObject resultJsonObject = null;
        try {
            resultJsonObject = new SearchService().performSearchByCriteria(inputFile);
        } catch (ParseException e) {
            new ExceptionWriter().writeParseException(outputFile);
            e.printStackTrace();
        }
        new FileWriterService().writeResultsToFile(outputFile, resultJsonObject);
    }

    private void getStatistics(String inputFile, String outputFile) throws IOException {
        JSONObject resultJsonObject = null;
        try {
            resultJsonObject = new StatisticService().getStatisticsForPeriod(inputFile);
        } catch (DateTimeParseException  e) {
            new ExceptionWriter().writeDateTimeParseException(outputFile);
            e.printStackTrace();
        } catch (ParseException e) {
            new ExceptionWriter().writeParseException(outputFile);
            e.printStackTrace();
        }
        new FileWriterService().writeResultsToFile(outputFile, resultJsonObject);
    }
}
