package com.aikamsoft.testTask.services;

import com.aikamsoft.testTask.databaseConnection.DatabaseConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unchecked")
public class SearchService {
    public JSONObject performSearchByCriteria(String inputFile) throws IOException, ParseException {
        String lastName;
        String productName;
        Long minTimes;
        Long minExpenses;
        Long maxExpenses;
        Long badCustomersNumber;

        JSONArray inputJsonArray = new FileReaderService().redSearchCriteria(inputFile);
        JSONArray resultJsonArray = new JSONArray();
        JSONObject resultJsonObject = new JSONObject();
        SearchService searchService = new SearchService();

        for (Object o : inputJsonArray) {
            JSONObject criteria = (JSONObject) o;
            if (criteria.containsKey("lastName")) {
                lastName = (String) criteria.get("lastName");
                searchService.findCustomerByLastName(lastName, resultJsonArray);
            }
            if (criteria.containsKey("productName")) {
                productName = (String) criteria.get("productName");
                minTimes = (Long) criteria.get("minTimes");
                searchService.findCustomerByProductNameAndMinTimes(productName, minTimes, resultJsonArray);
            }
            if (criteria.containsKey("minExpenses")) {
                minExpenses = (Long) criteria.get("minExpenses");
                maxExpenses = (Long) criteria.get("maxExpenses");
                searchService.findCustomersByMinAndMaxPrice(minExpenses, maxExpenses, resultJsonArray);
            }
            if (criteria.containsKey("badCustomers")) {
                badCustomersNumber = (Long) criteria.get("badCustomers");
                searchService.findBadCustomers(badCustomersNumber, resultJsonArray);
            }
        }
        searchService.createResultJson(resultJsonArray, resultJsonObject);
        return resultJsonObject;
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
                if (!rs.isBeforeFirst()) {
                    JSONObject notFound = new JSONObject();
                    notFound.put("lastName", lastName + " not found");
                    jsonArray.add(notFound);
                    object.put("results", jsonArray);
                } else {
                    while (rs.next()) {
                        name = rs.getString("name");
                        JSONObject jsonObjectOfArray = new JSONObject();
                        jsonObjectOfArray.put("name", name);
                        jsonObjectOfArray.put("lastName", lastName);
                        jsonArray.add(jsonObjectOfArray);
                    }
                    object.put("results", jsonArray);
                }
            }
            resultJsonArray.add(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findCustomerByProductNameAndMinTimes(String productName, Long minTimes, JSONArray resultJsonArray) {
        String name;
        String lastName;
        JSONObject object = new JSONObject();

        JSONObject criteria = new JSONObject();
        criteria.put("productName", productName);
        criteria.put("minTimes", minTimes);
        object.put("criteria", criteria);

        JSONArray jsonArray = new JSONArray();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT last_name, name, COUNT(pur.id) FROM customers AS c " +
                "INNER JOIN purchases AS pur ON c.id=pur.customer_id " +
                "INNER JOIN products AS pr ON pr.id=pur.product_id " +
                "WHERE pr.title=? " +
                "GROUP BY last_name, name " +
                "having count(pur.id) > ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, productName);
            ps.setLong(2, minTimes);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    JSONObject notFound = new JSONObject();
                    notFound.put("productName", productName + " not found");
                    jsonArray.add(notFound);
                    object.put("results", jsonArray);
                } else {
                    while (rs.next()) {
                        name = rs.getString("name");
                        lastName = rs.getString("last_name");
                        JSONObject jsonObjectOfArray = new JSONObject();
                        jsonObjectOfArray.put("name", name);
                        jsonObjectOfArray.put("lastName", lastName);
                        jsonArray.add(jsonObjectOfArray);
                    }
                    object.put("results", jsonArray);
                }
            }
            resultJsonArray.add(object);
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
                if (!rs.isBeforeFirst()) {
                    JSONObject notFound = new JSONObject();
                    notFound.put("minAndMax", "покупки в диапазоне " + minPrice + " и " + maxPrice + " не найдены");
                    jsonArray.add(notFound);
                    object.put("results", jsonArray);
                } else {
                    while (rs.next()) {
                        name = rs.getString("name");
                        lastName = rs.getString("last_name");
                        JSONObject jsonObjectOfArray = new JSONObject();
                        jsonObjectOfArray.put("name", name);
                        jsonObjectOfArray.put("lastName", lastName);
                        jsonArray.add(jsonObjectOfArray);
                    }
                    object.put("results", jsonArray);
                }
            }
            resultJsonArray.add(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findBadCustomers(Long badCustomersNumber, JSONArray resultJsonArray) {
        String name;
        String lastName;

        JSONObject object = new JSONObject();
        JSONObject criteria = new JSONObject();
        criteria.put("badCustomers", badCustomersNumber);
        object.put("criteria", criteria);

        JSONArray jsonArray = new JSONArray();
        Connection connection = DatabaseConnection.getConnection();

        String query = "SELECT last_name, name, purchase_count " +
                "FROM (SELECT last_name, name, count(pur.id) purchase_count " +
                "FROM customers AS c " +
                "JOIN purchases AS pur ON c.id=pur.customer_id " +
                "JOIN products AS pr ON pr.id=pur.product_id " +
                "GROUP BY last_name, name " +
                ") total_purchase_count " +
                "ORDER BY purchase_count " +
                "LIMIT ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, badCustomersNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString("name");
                    lastName = rs.getString("last_name");
                    JSONObject jsonObjectOfArray = new JSONObject();
                    jsonObjectOfArray.put("name", name);
                    jsonObjectOfArray.put("lastName", lastName);
                    jsonArray.add(jsonObjectOfArray);
                }
                object.put("results", jsonArray);
            }
            resultJsonArray.add(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createResultJson(JSONArray resultJsonArray, JSONObject resultJsonObject) {
        resultJsonObject.put("type", "search");
        resultJsonObject.put("results", resultJsonArray);
    }
}
