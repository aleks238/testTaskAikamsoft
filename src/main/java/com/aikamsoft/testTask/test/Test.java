package com.aikamsoft.testTask.test;

import com.aikamsoft.testTask.services.FileWriterService;
import com.aikamsoft.testTask.services.SearchService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
        String inputFile = "C:/Users/lanxe/Desktop/test1.json";

        SearchService searchService = new SearchService();
        JSONObject resultJsonObject = searchService.parseInput(inputFile);
        System.out.println(resultJsonObject);

        new FileWriterService().writeResultsToFile("output.json", resultJsonObject);
    }
}
