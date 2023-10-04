package com.aikamsoft.testTask.test;

import com.aikamsoft.testTask.services.ExceptionWriter;
import com.aikamsoft.testTask.services.FileWriterService;
import com.aikamsoft.testTask.services.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.format.DateTimeParseException;

@Slf4j
public class TestStat {
    public static void main(String[] args) throws IOException{
        String inputFile = "C:/Users/lanxe/Desktop/statCriterias.json";
        String outputFile = "outputStat.json";

        StatisticService statisticService = new StatisticService();
        JSONObject resultJsonObject = null;

        try {
            resultJsonObject = statisticService.getStatisticsForPeriod(inputFile);
        } catch (DateTimeParseException | ParseException e) {
            new ExceptionWriter().writeDateTimeParseException(outputFile);
            throw new RuntimeException();
        }

        System.out.println(resultJsonObject);
        new FileWriterService().writeResultsToFile(outputFile, resultJsonObject);

    }
}
