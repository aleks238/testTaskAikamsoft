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
        /*Этот SQL запрос у меня не совсем получился, он достает не полностью ту информацию которая требуется в ТЗ. */
        String query = "SELECT " +
                "c.name AS customer_name, c.last_name AS customer_last_name, p.title AS product_title," +
                "SUM(p.price) AS expenses, " +
                "cust_total.totalExpenses " +
                "FROM customers AS c " +
                "INNER JOIN purchases AS pu ON c.id = pu.customer_id " +
                "INNER JOIN products AS p ON p.id = pu.product_id " +
                "LEFT JOIN ( " +
                "SELECT " +
                "c.id AS customer_id, " +
                "SUM(pr.price) AS totalExpenses " +
                "FROM customers AS c " +
                "INNER JOIN purchases AS pu ON c.id = pu.customer_id " +
                "INNER JOIN products AS pr ON pr.id = pu.product_id " +
                "WHERE pu.purchase_date BETWEEN ? AND ? " +
                "GROUP BY c.id " +
                ") AS cust_total ON c.id = cust_total.customer_id " +
                "WHERE pu.purchase_date BETWEEN ? AND ? " +
                "GROUP BY c.name, c.last_name, p.title, cust_total.totalExpenses";
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
