package com.aikamsoft.testTask.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class FileReaderService {
    public JSONArray redSearchCriteria(String inputFile) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(inputFile));
        return (JSONArray) jsonObject.get("criterias");
    }

    public JSONObject readStatPeriod(String inputFile) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(new FileReader(inputFile));
    }
}
