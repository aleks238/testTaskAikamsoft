package com.aikamsoft.testTask;

import com.aikamsoft.testTask.services.ExceptionWriter;
import com.aikamsoft.testTask.services.SearchService;
import com.aikamsoft.testTask.services.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;

import java.io.IOException;

@Slf4j
@SuppressWarnings("unchecked")
public class Main {
    public static void main(String[] args) throws IOException {
        Main mainObject = new Main();
        String outputFile = args[1];
        try {
            mainObject.parseInputArgs(mainObject, args);
        } catch (IOException e) {
            e.printStackTrace();
            new ExceptionWriter().writeToFileIOException(outputFile);
        } catch (ParseException e) {
            e.printStackTrace();
            new ExceptionWriter().writeToFileParseException(outputFile);
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
            mainObject.findCustomersByCriteria(outputFile, inputFile);
        } else if (operation.equals("stat")) {
            mainObject.getStatistics(outputFile, inputFile);
        } else {
            throw new RuntimeException("Введен некорректный тип операции");
        }
    }

    private void findCustomersByCriteria(String outputFile, String inputFile) throws IOException, ParseException {
        SearchService searchService = new SearchService();

    }

    private void getStatistics(String outputFile, String inputFile) {
        StatisticService statisticService = new StatisticService();

    }


}
