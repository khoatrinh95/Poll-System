package db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conn {

    //Driver
    static String jdbcDriver;
    //Url of Database
    static String dbUrl;

    //Desired Database to access
    static String dbName;

    //Credentials
    static String dbUser;
    static String dbPassword;

    static Connection conn = null;

    public Conn() {
	System.out.println(jdbcDriver);
        getConnectionInfo();
    }

    public static void closeConnection() throws SQLException {
        //Close connection
        if (conn != null) conn.close();
    }

    public static Connection getConnection() {
        if (conn != null)
            return conn;
        try {
            //Register JDBC driver
            Class.forName(jdbcDriver);
            //Open a connection
            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(dbUrl + dbName, dbUser, dbPassword);
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error Class Not Found", e);
        }
    }

    private void getConnectionInfo() {
        // get the property value
        jdbcDriver = "com.mysql.cj.jdbc.Driver";
        dbUrl = "jdbc:mysql://localhost:3306/";
        dbUser = "root";
        dbPassword = "";
        dbName = "PollMaster";
    }

}
