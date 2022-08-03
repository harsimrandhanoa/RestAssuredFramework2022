package com.retail.base;

import static io.restassured.RestAssured.given;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.io.output.WriterOutputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.SkipException;
//import org.apache.commons.io.output.WriterOutputStream;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.retail.base.database.DatabaseActions;
import com.retail.base.database.DatabaseConnection;
import com.retail.base.database.DatabaseValidations;

import io.restassured.RestAssured;
import rest.retail.reporting.ExtentManager;
import rest.retail.util.ReadJsonData;
import runner.DataUtil;

public class BaseTest {

	public static String sessionId = "asd";
	ReadJsonData jsonData;
	public static SoftAssert softAssert = new SoftAssert();
	public static Properties testProp;

	public ExtentReports rep;
	public static String reportFolder;
	public ExtentTest test;
	String testname;
	public static String jsonDataPath;

	public StringWriter requestWriter;
	public PrintStream requestCapture;
	public static Connection conn;
	public DatabaseValidations databaseValidations;
	public DatabaseActions databaseActions;

	@BeforeSuite
	public void beforeSuite(ITestContext context) {
		try {
			testProp = new Properties();
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir") + "//src//test//resources//project.properties");
			testProp.load(fs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		conn = new DatabaseConnection().connectToDb(testProp);
        Util util = new Util();
		sessionId = util.getSession(testProp);
	}

	@BeforeTest
	public void init(ITestContext context)
			throws NumberFormatException, FileNotFoundException, IOException, ParseException {

		jsonDataPath = System.getProperty("user.dir") + "//src//test//resources//json//data//Data.json";

		// if we weant to read data from excel file
		// xls = new
		// ReadExcel(System.getProperty("user.dir")+"//src//test//resources//xls//"+testProp.getProperty("xlspath"));
		// jsonData = new ReadJsonData();

		String dataFilePath = context.getCurrentXmlTest().getParameter("datafilepath");
		String dataFlag = context.getCurrentXmlTest().getParameter("dataflag");
		String iteration = context.getCurrentXmlTest().getParameter("iteration");

		JSONObject data = new DataUtil().getTestData(dataFilePath, dataFlag, Integer.parseInt(iteration));

		context.setAttribute("data", data);
		context.setAttribute("iteration", iteration);

		RestAssured.baseURI = testProp.getProperty("baseurl");
		testname = this.getClass().getSimpleName();
		RestAssured.basePath = testProp.getProperty(testname);
		rep = ExtentManager.getReports();
		test = rep.createTest(testname + " Test Iteration:-" + iteration);

		context.setAttribute("report", rep); // set rep and test objects in
												// context
		context.setAttribute("test", test);

		databaseValidations = new DatabaseValidations(conn,test);
		databaseActions = new DatabaseActions(conn, databaseValidations,test);
	}

	@BeforeMethod
	public void before(ITestContext context) {
		if (!sessionId.matches("^\\w+$")) {
			log("Failed to login into session as sessionId is " + sessionId);
			context.setAttribute("skipReason", "Failed to login into session as sessionId is " + sessionId);
			throw new SkipException("Failed to login into session as sessionId is " + sessionId);

		}
		requestWriter = new StringWriter();
		requestCapture = new PrintStream(new WriterOutputStream(requestWriter), true);
	}

	@AfterTest
	public void after() {
	}

	protected void reportFailure(String errorMsg, boolean stopOnFailure) {
		test.log(Status.FAIL, errorMsg);// failure in extent reports
		softAssert.fail(errorMsg);
		if (stopOnFailure) {
			softAssert.assertAll();
		}
	}

	public void log(String msg) {
		test.log(Status.INFO, msg);
	}

	public void testPass() {
		test.log(Status.PASS, this.getClass().getSimpleName() + " has passed");
	}

	public void addRequestToLink(String message, String fileName, String Content) {
		try {
			new File(reportFolder + "\\log\\" + fileName + ".html").createNewFile();
			FileWriter fw = new FileWriter(reportFolder + "\\log\\" + fileName + ".html");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(Content);
			bw.close();
			fw.close();
		} catch (IOException e) {
			// // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@AfterSuite
	public void afterSuite() {

		try {
			databaseActions.deleteAll();
			conn.close();
			rep.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
