package com.aikamsoft.testTask.services;

import com.aikamsoft.testTask.databaseConnection.DatabaseConnection;
import lombok.extern.slf4j.Slf4j;
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
import com.aikamsoft.testTask.sqlQueries.SqlQueries;
@Slf4j
@SuppressWarnings("unchecked")
public class StatisticService {
    public JSONObject getStatisticsForPeriod(String inputFile) throws IOException, ParseException {
        StatisticService statisticService = new StatisticService();
        JSONObject jsonObject = new FileReaderService().readStatPeriod(inputFile);
        JSONObject resultJsonObject = new JSONObject();
        JSONArray resultJsonArray = new JSONArray();
        //Получение дат и периода
        String startDate = (String) jsonObject.get("startDate");
        String endDate = (String) jsonObject.get("endDate");
        Integer period = statisticService.getPeriod(startDate, endDate);

        statisticService.getStatistic(startDate, endDate, resultJsonArray);
        statisticService.createResultJson(resultJsonArray, resultJsonObject, period);
        return resultJsonObject;
    }

    private void getStatistic(String startDate, String endDate, JSONArray resultJsonArray) {
        String name;
        String lastName;
        String productTitle;
        Integer price;
        Integer totalExpenses = null;

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Connection connection = DatabaseConnection.getConnection();
        String query = SqlQueries.getStatistic;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            ps.setDate(3, Date.valueOf(startDate));
            ps.setDate(4, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    //Получение статистики
                    name = rs.getString("customer_name");
                    lastName = rs.getString("customer_last_name");
                    productTitle = rs.getString("product_title");
                    price = rs.getInt("expenses");
                    totalExpenses = rs.getInt("totalExpenses");
                    //JSONObject для имени покупателя
                    JSONObject nameJsonObject = new JSONObject();
                    nameJsonObject.put("name", lastName + " " + name);
                    resultJsonArray.add(nameJsonObject);
                    //JsonArray c покупками покупателя
                    JSONObject jsonObjectOfArray = new JSONObject();
                    jsonObjectOfArray.put("title", productTitle);
                    jsonObjectOfArray.put("price", price);
                    jsonArray.add(jsonObjectOfArray);

                }
                object.put("purchases", jsonArray);
                object.put("totalExpenses", totalExpenses);
            }
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
