package com.aikamsoft.testTask.dao;

import com.aikamsoft.testTask.databaseConnection.DatabaseConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CustomerDao {

    public void findCustomers(String outputFile, String inputFile) throws IOException, ParseException {
        String lastName;
        String productName;
        Long minTimes;
        Long minExpenses;
        Long maxExpenses;
        Long badCustomers;
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
                    CustomerDao customerDao = new CustomerDao();
                    System.out.println(customerDao.findCustomerByLastName(lastName));
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

    private JSONArray findCustomerByLastName(String lastName) {
        String name;
        JSONArray jsonArray = new JSONArray();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT last_name, name FROM customers WHERE last_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, lastName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString("name");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(name, lastName);
                    jsonArray.add(jsonObject);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jsonArray;
    }

    private JSONArray findByProductNameAndMinTimes(String productName, String minTimes){

    }
}
