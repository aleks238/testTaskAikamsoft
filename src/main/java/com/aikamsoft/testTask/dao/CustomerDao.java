package com.aikamsoft.testTask.dao;

import com.aikamsoft.testTask.databaseConnection.DatabaseConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDao {


    public JSONObject findCustomers(String outputFile, String inputFile) throws IOException, ParseException {
        String lastName;
        String productName;
        Long minTimes;
        Long minExpenses;
        Long maxExpenses;
        Long badCustomersNumber;
        CustomerDao customerDao = new CustomerDao();
        JSONObject outputJsonObject = new JSONObject();
        /*
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.readValue(new File("C:/Users/lanxe/Desktop/test.json"), Customer.class);
        System.out.println(customer.getName() + " " + customer.getLastName());
         */
        inputFile = "C:/Users/lanxe/Desktop/test1.json";
        outputFile = "C:/Users/lanxe/Desktop/output.json";
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader(inputFile));
            JSONArray inputJsonArray = (JSONArray) jsonObject.get("criterias");

            for (int i = 0; i < inputJsonArray.size(); i++) {
                JSONObject criteria = (JSONObject) inputJsonArray.get(i);
                if (criteria.containsKey("lastName")) {
                    lastName = (String) criteria.get("lastName");
                    customerDao.findCustomerByLastName(lastName, outputJsonObject);
                }
                if (criteria.containsKey("productName")) {
                    productName = (String) criteria.get("productName");
                    minTimes = (Long) criteria.get("minTimes");
                    //System.out.println(customerDao.findCustomerByProductNameAndMinTimes(productName, minTimes));
                }
                if (criteria.containsKey("minExpenses")) {
                    minExpenses = (Long) criteria.get("minExpenses");
                    maxExpenses = (Long) criteria.get("maxExpenses");
                    customerDao.findCustomersByMinAndMaxPrice(minExpenses, maxExpenses, outputJsonObject);
                }
                if (criteria.containsKey("badCustomers")) {
                    badCustomersNumber = (Long) criteria.get("badCustomers");
                    //System.out.println(customerDao.findBadCustomers(badCustomersNumber));
                }
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e.getMessage());
            //write to file
        }
        return outputJsonObject;
    }

    private void findCustomerByLastName(String lastName, JSONObject outputJsonObject) {
        String name;

        JSONObject criteria = new JSONObject();
        criteria.put("lastName", lastName);
        outputJsonObject.put("criteria1", criteria);

        JSONArray jsonArray = new JSONArray();

        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT last_name, name FROM customers WHERE last_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, lastName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString("name");
                    JSONObject jsonObjectOfArray = new JSONObject();
                    jsonObjectOfArray.put(name, lastName);
                    jsonArray.add(jsonObjectOfArray);
                }
            }
            outputJsonObject.put("results1", jsonArray);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private void findCustomersByMinAndMaxPrice(Long minPrice, Long maxPrice, JSONObject outputJsonObject) {
        String name;
        String lastName;

        JSONObject criteria = new JSONObject();
        criteria.put("minPrice", minPrice);
        criteria.put("maxPrice", maxPrice);
        outputJsonObject.put("criteria", criteria);

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
                    JSONObject jsonObjectOfArray = new JSONObject();
                    jsonObjectOfArray.put(name, lastName);
                    jsonArray.add(jsonObjectOfArray);
                }
            }
            outputJsonObject.put("results", jsonArray);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONArray findBadCustomers(Long badCustomersNumber) {
        String name;
        String lastName;
        JSONArray jsonArray = new JSONArray();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT MIN(purchases.id) " +
                "FROM (SELECT last_name, name, COUNT(purchases.id) FROM customers " +
                "INNER JOIN purchases ON customers.id=purchases.customer_id) " +
                "CROUP BY customers.last_name " +
                " LIMIT ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, badCustomersNumber);
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


    public void writeResultsToFile(String nameOfOutputFile, JSONArray outJsonArray) throws IOException {
        String str = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + nameOfOutputFile;
        String desktopPath = str.replace("\\", "/");
        File outputFile = new File(desktopPath);
        boolean isCreated = outputFile.createNewFile();
        int ind = 1;
        while (!isCreated) {
            String[] tokens = nameOfOutputFile.split("\\.");
            str = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + tokens[0] + ind + "." + tokens[1];
            desktopPath = str.replace("\\", "/");
            outputFile = new File(desktopPath);
            isCreated = outputFile.createNewFile();
            ind++;
        }
        FileWriter fileWriter = new FileWriter(outputFile);
        JSONArray.writeJSONString(outJsonArray, fileWriter);

    }
}
