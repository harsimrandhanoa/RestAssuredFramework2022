package com.retail.base.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.retail.base.BaseTest;

public class DatabaseConnection{
	Connection conn = null;
	Statement stmt = null;


	public Connection connectToDb(Properties testProp) {
		String db = testProp.getProperty("db");
		String dbUser = testProp.getProperty("dbUser");
		String dbPassword = testProp.getProperty("dbPassword");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = null;
			conn = DriverManager.getConnection(db,dbUser,dbPassword);
			System.out.println("Database is connected !");

		} catch (Exception e) {
			System.out.print("Do not connect to DB - Error:" + e);
		}
		return conn;

	}
}
