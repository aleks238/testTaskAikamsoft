package com.aikamsoft.testTask.services;

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
import java.time.LocalDate;
import java.time.Period;

@SuppressWarnings("unchecked")
public class StatisticService {
    public JSONObject parseInput(String inputFile) throws IOException, ParseException {
        StatisticService statisticService = new StatisticService();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(inputFile));

        JSONObject resultJsonObject = new JSONObject();
        JSONArray resultJsonArray = new JSONArray();

        String startDate = (String) jsonObject.get("startDate");
        String endDate = (String) jsonObject.get("endDate");
        Integer period = statisticService.getPeriod(startDate, endDate);

        statisticService.createResultJson(resultJsonArray,resultJsonObject,period);

        return resultJsonObject;
    }

    private void getStatisticForPeriod(String startDate, String endDate, JSONArray resultJsonArray){
        String name;
        String lastName;
        String productTitle = null;
        String cost = null;

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        Connection connection = DatabaseConnection.getConnection();
        String query = "";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString("name");
                    lastName = rs.getString("last_name");
                    object.put("name", name + " " + lastName);

                    JSONObject jsonObjectOfArray = new JSONObject();
                    jsonObjectOfArray.put("title", productTitle);
                    jsonObjectOfArray.put("cost", cost);
                    jsonArray.add(jsonObjectOfArray);
                }
            }
            object.put("purchases", jsonArray);
            resultJsonArray.add(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private int getPeriod(String startDate, String endDate){
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        Period period = Period.between(start, end);
        return Math.abs(period.getDays());
    }
    private void createResultJson(JSONArray resultJsonArray, JSONObject resultJsonObject,Integer period){
        resultJsonObject.put("type", "stat");
        resultJsonObject.put("totalDays", period);
        resultJsonObject.put("customers", resultJsonArray);
    }
}
