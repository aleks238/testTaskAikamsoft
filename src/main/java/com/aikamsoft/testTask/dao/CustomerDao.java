package com.aikamsoft.testTask.dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class CustomerDao {

    public void findCustomers(String outputFile, String inputFile) throws IOException, ParseException {
        String lastName;
        String productName;
        Long minTimes;
        Long minExpenses;
        Long maxExpenses;
        Long badCustomers;

        //Connection connection = DatabaseConnection.getConnection();
        /*
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.readValue(new File("C:/Users/lanxe/Desktop/test.json"), Customer.class);
        System.out.println(customer.getName() + " " + customer.getLastName());
         */

        inputFile = "C:/Users/lanxe/Desktop/test1.json";
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader(inputFile));
            JSONArray jsonArray = (JSONArray) jsonObject.get("criterias");


            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject criteria = (JSONObject) jsonArray.get(i);
                if (criteria.containsKey("lastName")) {
                    lastName = (String) criteria.get("lastName");
                    System.out.println(lastName);
                }
                if (criteria.containsKey("productName")) {
                    productName = (String) criteria.get("productName");
                    System.out.println(productName);
                }
                if (criteria.containsKey("minTimes")) {
                    minTimes = (Long) criteria.get("minTimes");
                    System.out.println(minTimes);
                }
                if (criteria.containsKey("minExpenses")) {
                    minExpenses = (Long) criteria.get("minExpenses");
                    System.out.println(minExpenses);
                }
                if (criteria.containsKey("maxExpenses")) {
                    maxExpenses = (Long) criteria.get("maxExpenses");
                    System.out.println(maxExpenses);
                }
                if (criteria.containsKey("badCustomers")) {
                    badCustomers = (Long) criteria.get("badCustomers");
                    System.out.println(badCustomers);
                }
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e.getMessage());
            //write to file
        }
    }
}
