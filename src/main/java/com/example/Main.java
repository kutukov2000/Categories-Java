package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        System.out.println();
        createCategoriesTable(strConn, userName, password);

        System.out.println();
        getCategories(strConn, userName, password);

        System.out.println();
        insertCategory(strConn, userName, password);

        System.out.println();
        getCategories(strConn, userName, password);
    }

    private static void getCategories(String strConn, String userName, String password) {
        try (Connection conn = DriverManager.getConnection(strConn, userName, password)) {
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM categories";

            // Print table header
            System.out.printf("%-5s %-15s %-20s\n", "ID", "Name", "Description");

            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");
                // Print table row with data
                System.out.printf("%-5d %-15s %-20s\n", id, name, description);
            }

            statement.close();
            System.out.println("Table 'categories' SELECTED!");
        } catch (Exception ex) {
            System.out.println("Connection error: " + ex.getMessage());
        }
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