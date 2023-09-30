package com.aikamsoft.testTask.test;

import com.aikamsoft.testTask.dao.CustomerDao;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
        CustomerDao customerDao = new CustomerDao();
        customerDao.findCustomers("a","b");



    }
}
