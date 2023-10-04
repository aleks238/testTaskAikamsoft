package com.aikamsoft.testTask.test;

import com.aikamsoft.testTask.services.ExceptionWriter;
import com.aikamsoft.testTask.services.FileWriterService;
import com.aikamsoft.testTask.services.SearchService;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
@Slf4j
public class TestSearch {
    public static void main(String[] args) throws IOException {
        String inputFile = "C:/Users/lanxe/Desktop/searchCriterias.json";
        String outputFile = "outputSearch.json";
        SearchService searchService = new SearchService();
        JSONObject resultJsonObject;
        try {
            resultJsonObject = searchService.performSearchByCriteria(inputFile);
        } catch (ParseException e) {
            new ExceptionWriter().writeParseException(outputFile);
            throw new RuntimeException(e);
        }
        System.out.println(resultJsonObject);
        new FileWriterService().writeResultsToFile("outputSearch.json", resultJsonObject);
    }
}
