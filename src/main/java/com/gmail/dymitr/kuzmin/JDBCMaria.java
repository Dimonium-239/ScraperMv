package com.gmail.dymitr.kuzmin;

import java.sql.*;

public class JDBCMaria implements JDBC{

    private Connection conn;

    private Statement stmt;

    private String DBName;

    private static final String USER = "root";

    private static final String PASSWORD = "2000"; //your computer root password

    JDBCMaria(String DBName) throws SQLException {
        connect(DBName);
    }

    @Override
    public void connect(String DBName_) throws SQLException {
        DBName = DBName_;
        try{
            conn = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/" + DBName,
                    USER, PASSWORD);
        } catch (SQLException e) {
            createDB();
        }
        stmt = conn.createStatement();
    }

    @Override
    public void addToDB(DBElement element) throws SQLException {
        try {
            int price = (element).getPrice();
            String URL = (element).getURL();
            String model = ((Gramophone) element).getModel();
            String VALUES = "VALUES ( '" + URL + "', " + price + ", '" + model + "')";
            String QUERY = "INSERT INTO " + DBName + "." + DBName + " (Link, Price, Model) " + VALUES;
            stmt.executeUpdate(QUERY);
        }catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Element is on base");
        }
    }

    public void deleteAllegro() throws SQLException{
        String strSelect = "select Link from " + DBName + "." + DBName;
        ResultSet rset = stmt.executeQuery(strSelect);
        int rowCount = 0;

        while (rset.next()) {
            String URL = rset.getString("Link");
            if (URL.contains("allegro")) {
                removeFromDB(URL);
            }
            rowCount++;
        }
        System.out.println(rowCount);
    }

    public void deleteTrash() throws SQLException{
        String strSelect = "select Link, Model from " + DBName + "." + DBName;
        ResultSet rset = stmt.executeQuery(strSelect);
        int rowCount = 0;

        while (rset.next()) {
            String URL = rset.getString("Link");
            String Model = rset.getString("Model");
            if (Model.toLowerCase().contains("igły") || Model.toLowerCase().contains("silniczek") || Model.toLowerCase().contains("ramię")) {
                removeFromDB(URL);
            }
            rowCount++;
        }
        System.out.println(rowCount);
    }

    @Override
    public void removeFromDB(String URL) throws SQLException {
        stmt.executeUpdate("DELETE FROM " + DBName + "." + DBName +" WHERE Link= '" + URL + "';");
        System.out.println("Element with URL: " + URL + "\nis deleted from base");
    }

    @Override
    public void refreshDB() {
        //TODO Implement refreshing and actualisation
    }

    @Override
    public DBElement getFromDB(String URL) throws SQLException {
        String selectElem = "SELECT * FROM " + DBName + "." + DBName + " WHERE Link = '" + URL +"'";
        ResultSet rset = stmt.executeQuery(selectElem);
        rset.first();
        return new Gramophone(rset.getString(1), rset.getInt(2), rset.getString(3));
    }

    @Override
    public void disconnect() throws SQLException {
        conn.close();
        System.out.println("Connection closed");
    }

    @Override
    public void createDB() {
        try {
            String url = "jdbc:mariadb://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull";
            Connection connection = DriverManager.getConnection(url, USER, PASSWORD);

            String sql = "CREATE DATABASE " + DBName;

            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

            createTable();

            System.out.println("Database " + DBName + " has been created successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() throws SQLException {
        String createTableStmt="CREATE TABLE IF NOT EXISTS " + DBName + "." + DBName + " (" +
                 "Link text NOT NULL UNIQUE)";
        stmt.executeUpdate(createTableStmt);
        System.out.println("Table " + DBName + "." + DBName + " is successful created");
    }

    @Override
    public void createColumn(String collName, String collType) throws SQLException {
        try {
            String column = "ALTER TABLE " + DBName + " ADD " + collName + " " + collType;
            stmt.executeUpdate(column);
        }catch (java.sql.SQLSyntaxErrorException e){
            System.out.println("Column " + collName + " already exist");
        }
    }
}
