package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String userName = "root";
        String password = "";
        String host = "localhost";
        String port = "3306";
        String database = "javaDb";
        String strConn = "jdbc:mariadb://" + host + ":" + port + "/" + database;

        createCategoriesTable(strConn, userName, password);
        insertCategory(strConn, userName, password);
    }

    private static void insertCategory(String strConn, String userName, String password) {
        Scanner input = new Scanner(System.in);

        CategoryCreate categoryCreate = new CategoryCreate();
        System.out.printf("Enter category name: ");
        categoryCreate.setName(input.nextLine());
        System.out.printf("Enter category description: ");
        categoryCreate.setDescription(input.nextLine());

        try (Connection conn = DriverManager.getConnection(strConn, userName, password)) {
            String insertQuery = "INSERT INTO categories (name, description) VALUES (?, ?)";
            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            // Set values for the placeholders
            preparedStatement.setString(1, categoryCreate.getName());
            preparedStatement.setString(2, categoryCreate.getDescription());
            // Execute the SQL query
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the resources
            preparedStatement.close();
            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("Category inserted successfully.");
        } catch (Exception ex) {
            System.out.println("Category creation error: " + ex.getMessage());
        }

        input.close();
    }

    private static void createCategoriesTable(String strConn, String userName, String password) {
        try (Connection conn = DriverManager.getConnection(strConn, userName, password)) {
            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS categories ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255) NOT NULL,"
                    + "description TEXT"
                    + ")";
            statement.execute(sql);
            statement.close();
            System.out.println("Table 'categories' successfully created!");
        } catch (Exception ex) {
            System.out.println("Connection error: " + ex.getMessage());
        }
    }
}