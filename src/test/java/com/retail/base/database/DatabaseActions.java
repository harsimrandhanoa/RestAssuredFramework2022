package com.retail.base.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.aventstack.extentreports.ExtentTest;
import com.retail.base.BaseTest;

public class DatabaseActions {
	Connection conn;
	Statement stmt;
	PreparedStatement myStmt;
	DatabaseValidations dbValidations;
	ExtentTest test;

	public DatabaseActions(Connection conn, DatabaseValidations dbValidations,ExtentTest test) {
		this.conn = conn;
		this.dbValidations = dbValidations;
		this.test = test;
	}

	public int getCategoryId(String categoryName) {

		try {
			myStmt = conn.prepareStatement("Select cat_id as Id from Category where cat_name =?");
			myStmt.setString(1, categoryName);
			ResultSet rs = myStmt.executeQuery();

			while (rs.next()) {
				return rs.getInt("Id");
			}
		} catch (Exception e) {
			test.fail("Could not get Category id for category name  " +categoryName + " from db.Error is \n "+e);
		}
		return 0;

	}

	public int getProductId(String productName, double productPrice, int productQuantity, String categoryName) {
		try {

			// myStmt = conn.prepareStatement("Select prod_id as productId from
			// Product where prod_name = ? and prod_price = ? and prod_quantity
			// = ? and cat_id = (Select cat_id from Category where cat_name =
			// ?)");

			myStmt = conn.prepareStatement("Select prod_id as productId from Product p left Join Category c on p.cat_id = c.cat_id  where prod_name = ? and prod_price = ? and prod_quantity = ? and c.cat_name  = ?");

			myStmt.setString(1, productName);
			myStmt.setDouble(2, productPrice);
			myStmt.setInt(3, productQuantity);
			myStmt.setString(4, categoryName);

			ResultSet rs = myStmt.executeQuery();

			while (rs.next()) {
				return rs.getInt("productId");
			}
		} catch (Exception e) {
			test.fail("Could not get product id for product name  " +productName + " from db.Error is \n "+e);
		}
		return 0;

	}

	public int addCategory(String categoryName) {

		try {

			if (!dbValidations.isCategoryAdded(categoryName)) {
				myStmt = conn.prepareStatement("INSERT INTO Category (cat_name) VALUES (?);");
				myStmt.setString(1, categoryName);
				myStmt.executeUpdate();
			}
			return getCategoryId(categoryName);
		} catch (Exception e) {
			test.fail("Error while adding Category named "+categoryName+" to db.Error is " + e);
		}
		return 0;
	}

	public int addProduct(String productName, double productPrice, int productQuantity, int categoryId,
			String categoryName) {
		try {
			if (!dbValidations.isProductAdded(productName, productPrice, productQuantity, categoryId)) {
				myStmt = conn.prepareStatement("INSERT INTO Product (cat_id,prod_name,prod_price,prod_quantity) VALUES (?,?,?,?)");
				myStmt.setInt(1, categoryId);
				myStmt.setString(2, productName);
				myStmt.setDouble(3, productPrice);
				myStmt.setInt(4, productQuantity);
				myStmt.executeUpdate();
			}

			return getProductId(productName, productPrice, productQuantity, categoryName);
		} catch (Exception e) {
			test.fail("Error while adding Product  named "+productName+" to db.Error is " + e);
		}
		return 0;
	}

	public void deleteAll() {
		try {
            stmt = conn.createStatement();
			stmt.executeUpdate("Delete  from Product");
			stmt.executeUpdate("Delete  from Category");
		} catch (Exception e) {
			test.fail("Error while deleting everything from db.Error is " + e);
		}

	}

}
