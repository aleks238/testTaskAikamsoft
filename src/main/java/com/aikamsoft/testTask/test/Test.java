package com.aikamsoft.testTask.test;

import com.aikamsoft.testTask.dao.CustomerDao;
import com.aikamsoft.testTask.dto.CriteriaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {

        CustomerDao customerDao = new CustomerDao();
        JSONObject outputJsonObject = customerDao.findCustomers("a","b");
        System.out.println(outputJsonObject);
        //customerDao.writeResultsToFile("output.json", outJsonArray);



    }
}
