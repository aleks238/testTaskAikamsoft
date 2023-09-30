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

public class CustomerDao {

    public void findCustomers(String outputFile, String inputFile) throws IOException, ParseException {
        String lastName;
        String productName;
        Long minTimes;
        Long minExpenses;
        Long maxExpenses;
        Long badCustomers;
        CustomerDao customerDao = new CustomerDao();
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
                    System.out.println(customerDao.findCustomerByLastName(lastName));
                }
                if (criteria.containsKey("productName")) {
                    productName = (String) criteria.get("productName");
                    minTimes = (Long) criteria.get("minTimes");
                    //System.out.println(customerDao.findCustomerByProductNameAndMinTimes(productName, minTimes));
                }
                if (criteria.containsKey("minExpenses")) {
                    minExpenses = (Long) criteria.get("minExpenses");
                    maxExpenses = (Long) criteria.get("maxExpenses");
                    System.out.println(minExpenses + " " + maxExpenses);
                    System.out.println(customerDao.findCustomersByMinAndMaxPrice(minExpenses,maxExpenses));
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

    private JSONArray findCustomerByProductNameAndMinTimes(String productName, Long minTimes) {
        String name;
        String lastName;
        JSONArray jsonArray = new JSONArray();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT count(*) > ? last_name, name FROM customers " +
                "LEFT JOIN customers.id=purchases.customer_id " +
                "LEFT JOIN products.id=purchases.product_id " +
                "WHERE title=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(2, productName);
            ps.setLong(1, minTimes);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString("name");
                    lastName = rs.getString("last_name");
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

    private JSONArray findCustomersByMinAndMaxPrice(Long minPrice, Long maxPrice){
        String name;
        String lastName;
        JSONArray jsonArray = new JSONArray();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT last_name, name FROM customers " +
                "LEFT JOIN purchases ON customers.id=purchases.customer_id " +
                "LEFT JOIN products ON products.id=purchases.product_id " +
                "WHERE price <= ? AND price >= ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, maxPrice);
            ps.setLong(2, minPrice);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString("name");
                    lastName = rs.getString("last_name");
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
}
