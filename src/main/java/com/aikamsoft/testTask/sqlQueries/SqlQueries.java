package com.aikamsoft.testTask.sqlQueries;

public final class SqlQueries {
     public static final String findCustomerByLastName = "SELECT last_name, name FROM customers WHERE last_name = ?";
     public static final String findCustomerByProductNameAndMinTimes = "SELECT last_name, name, COUNT(pur.id) FROM customers AS c " +
             "INNER JOIN purchases AS pur ON c.id=pur.customer_id " +
             "INNER JOIN products AS pr ON pr.id=pur.product_id " +
             "WHERE pr.title=? " +
             "GROUP BY last_name, name " +
             "having count(pur.id) > ?";
     public static final String findCustomersByMinAndMaxPrice = "SELECT last_name, name FROM customers " +
             "LEFT JOIN purchases ON customers.id=purchases.customer_id " +
             "LEFT JOIN products ON products.id=purchases.product_id " +
             "WHERE price <= ? AND price >= ?";
     public static final String findBadCustomers = "SELECT last_name, name, purchase_count " +
             "FROM (SELECT last_name, name, count(pur.id) purchase_count " +
             "FROM customers AS c " +
             "JOIN purchases AS pur ON c.id=pur.customer_id " +
             "JOIN products AS pr ON pr.id=pur.product_id " +
             "GROUP BY last_name, name " +
             ") total_purchase_count " +
             "ORDER BY purchase_count " +
             "LIMIT ?";
    /*Этот SQL запрос у меня не совсем получился, он достает не полностью ту информацию которая требуется в ТЗ. */
     public static final String getStatistic = "SELECT " +
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
}
