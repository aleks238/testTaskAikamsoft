package com.aikamsoft.testTask.test;

import com.aikamsoft.testTask.services.FileWriterService;
import com.aikamsoft.testTask.services.StatisticService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class TestStat {
    public static void main(String[] args) throws IOException, ParseException {
        String inputFile = "C:/Users/lanxe/Desktop/statCriterias.json";

        StatisticService statisticService = new StatisticService();
        JSONObject resultJsonObject = statisticService.getStatisticsForPeriod(inputFile);
        System.out.println(resultJsonObject);

        new FileWriterService().writeResultsToFile("outputStat.json", resultJsonObject);

    }
}
