package com.retail.base.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.aventstack.extentreports.ExtentTest;

public class DatabaseValidations {
	Connection conn;
	Statement stmt;
	PreparedStatement myStmt;
	ExtentTest test;


	public DatabaseValidations(Connection conn,ExtentTest test) {
		this.conn = conn;
		this.test = test;
	}

	public Boolean isCategoryAdded(String categoryName) {
		try {

			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Select cat_name as catName from Category");
			while (rs.next()) {
				if (rs.getString("catName").equals(categoryName.trim()))
					return true;
			}
		} catch (Exception e) {
			test.fail("Error while fetching  data related to " + categoryName +" from Category table .Error is " + e);
			return false;
		}

		return false;

	}

	public Boolean isProductAdded(String productName, double productPrice, int productQuantity, int categoryId) {

		try {

			myStmt = conn.prepareStatement("Select prod_name as productName from Product where prod_price = ?  and prod_quantity = ? and cat_id = ?");
			myStmt.setDouble(1, productPrice);
			myStmt.setInt(2, productQuantity);
			myStmt.setInt(3, categoryId);

			ResultSet rs = myStmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("productName").equals(productName.trim()))
					return true;
			}
		} catch (Exception e) {
			test.fail("Error while fetching  data related to " + productName +" from Product table .Error is " + e);
			return false;
		}

		return false;

	}

	public Boolean isProductDeleted(String productName, double productPrice, int productQuantity, String categoryName) {
	
		try {
	
			myStmt = conn.prepareStatement("Select count(*) as Count from Product  where prod_name = ? and prod_price = ? and prod_quantity = ? and cat_id = (Select cat_id from Category where cat_name = ?)");
			myStmt.setString(1, productName);
			myStmt.setDouble(2, productPrice);
			myStmt.setInt(3, productQuantity);
			myStmt.setString(4, categoryName);
	
			ResultSet rs = myStmt.executeQuery();
	
			while (rs.next()) {
				if (rs.getInt("Count") == 0)
					return true;
			}
		} catch (Exception e) {
			test.fail("Error while fetching  data related to " + productName +" from Product table .Error is " + e);
			return false;
		}
	
		return false;
	
	}

}
