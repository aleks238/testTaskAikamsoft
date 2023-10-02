package com.aikamsoft.testTask.services;

import com.aikamsoft.testTask.databaseConnection.DatabaseConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

@SuppressWarnings("unchecked")
public class StatisticService {
    public JSONObject getStatisticsForPeriod(String inputFile) throws IOException, ParseException {
        StatisticService statisticService = new StatisticService();

        JSONObject jsonObject = new FileReaderService().readStatPeriod(inputFile);
        JSONObject resultJsonObject = new JSONObject();
        JSONArray resultJsonArray = new JSONArray();
        String startDate = (String) jsonObject.get("startDate");
        String endDate = (String) jsonObject.get("endDate");
        Integer period = statisticService.getPeriod(startDate, endDate);

        statisticService.getStatisticForPeriod(startDate,endDate,resultJsonArray);
        statisticService.createResultJson(resultJsonArray, resultJsonObject, period);
        return resultJsonObject;
    }

    private void getStatisticForPeriod(String startDate, String endDate, JSONArray resultJsonArray) {
        String name;
        String lastName;
        String productTitle;
        Integer price;

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        Connection connection = DatabaseConnection.getConnection();
        /*
        String query = "SELECT last_name, name, title, price FROM customers " +
                "LEFT JOIN purchases ON customers.id=purchases.customer_id " +
                "LEFT JOIN products ON products.id=purchases.product_id " +
                "WHERE purchase_date BETWEEN ? AND ? ";

         */

        String query = "SELECT last_name, name, title, price FROM customers " +
                "JOIN purchases ON customers.id=purchases.customer_id " +
                "JOIN products ON products.id=purchases.product_id " +
                "WHERE purchase_date BETWEEN ? AND ?";


        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString("name");
                    lastName = rs.getString("last_name");
                    productTitle = rs.getString("title");
                    price = rs.getInt("price");

                    JSONObject nameJsonObject = new JSONObject();
                    nameJsonObject.put("name", lastName + " " + name);
                    resultJsonArray.add(nameJsonObject);

                    JSONObject jsonObjectOfArray = new JSONObject();
                    jsonObjectOfArray.put("title", productTitle);
                    jsonObjectOfArray.put("price", price);
                    jsonArray.add(jsonObjectOfArray);
                }
            }
            object.put("purchases", jsonArray);// Затем в JSONObject array с покупками
            resultJsonArray.add(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private int getPeriod(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        Period period = Period.between(start, end);
        return Math.abs(period.getDays());
    }

    private void createResultJson(JSONArray resultJsonArray, JSONObject resultJsonObject, Integer period) {
        resultJsonObject.put("type", "stat");
        resultJsonObject.put("totalDays", period);
        resultJsonObject.put("customers", resultJsonArray);
    }
}
