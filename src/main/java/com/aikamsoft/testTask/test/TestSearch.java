package com.aikamsoft.testTask.test;

import com.aikamsoft.testTask.services.FileWriterService;
import com.aikamsoft.testTask.services.SearchService;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class TestSearch {
    public static void main(String[] args) throws IOException, ParseException {
        String inputFile = "C:/Users/lanxe/Desktop/test1.json";

        SearchService searchService = new SearchService();
        JSONObject resultJsonObject = searchService.performSearchByCriteria(inputFile);
        System.out.println(resultJsonObject);

        new FileWriterService().writeResultsToFile("output.json", resultJsonObject);
    }
}
