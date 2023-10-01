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
@SuppressWarnings("unchecked")
public class CustomerDao {

    public JSONArray findCustomers(String outputFile, String inputFile) {
        String lastName;
        String productName;
        Long minTimes;
        Long minExpenses;
        Long maxExpenses;
        Long badCustomersNumber;
        CustomerDao customerDao = new CustomerDao();
        inputFile = "C:/Users/lanxe/Desktop/test1.json";
        outputFile = "C:/Users/lanxe/Desktop/output.json";
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        JSONArray resultJsonArray = new JSONArray(); //Ответ состоит из array of JSON, ответ на каждый критерий нужно оборачивать в отдельный JSON, который состоит из названия критерия и sub array.
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader(inputFile));
            JSONArray inputJsonArray = (JSONArray) jsonObject.get("criterias");

            for (int i = 0; i < inputJsonArray.size(); i++) {
                JSONObject criteria = (JSONObject) inputJsonArray.get(i);
                if (criteria.containsKey("lastName")) {
                    lastName = (String) criteria.get("lastName");
                    customerDao.findCustomerByLastName(lastName, resultJsonArray);
                }
                if (criteria.containsKey("productName")) {
                    productName = (String) criteria.get("productName");
                    minTimes = (Long) criteria.get("minTimes");
                    //System.out.println(customerDao.findCustomerByProductNameAndMinTimes(productName, minTimes));
                }
                if (criteria.containsKey("minExpenses")) {
                    minExpenses = (Long) criteria.get("minExpenses");
                    maxExpenses = (Long) criteria.get("maxExpenses");
                    customerDao.findCustomersByMinAndMaxPrice(minExpenses, maxExpenses, resultJsonArray);
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
        return resultJsonArray;
    }

    private void findCustomerByLastName(String lastName, JSONArray resultJsonArray) {
        String name;
        JSONObject object = new JSONObject();
        JSONObject criteria = new JSONObject();
        criteria.put("lastName", lastName);
        object.put("criteria", criteria);
        JSONArray jsonArray = new JSONArray();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT last_name, name FROM customers WHERE last_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, lastName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString("name");
                    JSONObject jsonObjectOfArray = new JSONObject();
                    jsonObjectOfArray.put("name", name);
                    jsonObjectOfArray.put("lastName", lastName);
                    //jsonObjectOfArray.put(name, lastName);
                    jsonArray.add(jsonObjectOfArray);
                }
            }
            object.put("results", jsonArray);
            resultJsonArray.add(object);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void findCustomerByProductNameAndMinTimes(String productName, Long minTimes) {
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
            e.printStackTrace();
        }
    }

    private void findCustomersByMinAndMaxPrice(Long minPrice, Long maxPrice, JSONArray resultJsonArray) {
        String name;
        String lastName;
        JSONObject object = new JSONObject();
        JSONObject criteria = new JSONObject();
        criteria.put("minPrice", minPrice);
        criteria.put("maxPrice", maxPrice);
        object.put("criteria", criteria);
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
                    jsonObjectOfArray.put("name", name);
                    jsonObjectOfArray.put("lastName", lastName);
                    //jsonObjectOfArray.put(name, lastName);
                    jsonArray.add(jsonObjectOfArray);
                }
            }
            object.put("results", jsonArray);
            resultJsonArray.add(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findBadCustomers(Long badCustomersNumber) {
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
    }

    public void writeResultsToFile(String nameOfOutputFile, JSONObject resultJsonObject) throws IOException {
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
        fileWriter.write(resultJsonObject.toJSONString());
        fileWriter.close();
    }
}
