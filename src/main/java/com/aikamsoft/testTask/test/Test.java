package com.aikamsoft.testTask.test;

import com.aikamsoft.testTask.dao.CustomerDao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {

        CustomerDao customerDao = new CustomerDao();
        JSONArray resultJsonArray = customerDao.findCustomers("a","b");

        JSONObject resultJsonObject = new JSONObject();
        resultJsonObject.put("type", "search");
        resultJsonObject.put("results", resultJsonArray);
        System.out.println(resultJsonObject);

        customerDao.writeResultsToFile("output.json", resultJsonObject);



    }
}
