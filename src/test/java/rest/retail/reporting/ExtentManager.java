package rest.retail.reporting;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.retail.base.BaseTest;

public class ExtentManager {

	static ExtentReports reports;

	public static ExtentReports getInstance(String path) {

		if (reports == null) {// first test

			reports = new ExtentReports();
			// init the report folder

			Date date = new Date();
			String folderName = date.toString().replace(":", "_ ").replace(" ", "_");

			new File(path + folderName).mkdir();
			new File(path + folderName + "\\log").mkdir(); // create a new
															// directory named
															// logs
			BaseTest.reportFolder = path + folderName; // give this new folder
														// name to reportFolder
			String filename = path + folderName + "\\report.html";
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(BaseTest.reportFolder);
			sparkReporter.config().setReportName("Rest Asured Framework 2022");
			sparkReporter.config().setDocumentTitle("Rest Assured Framework Reports");
			/*
			 * sparkReporter.config().setTheme(Theme.STANDARD);
			 * sparkReporter.config().setEncoding("utf-8");
			 */

			reports.attachReporter(sparkReporter);
		}
		return reports;

	}

	public static ExtentReports getReports() {
		if (reports == null) {// first test

			reports = new ExtentReports();
			// init the report folder
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String folderName = dateFormat.format(date).replaceAll(":", "_");

			BaseTest.reportFolder = System.getProperty("user.dir") + "//reports//" + folderName;
			File f = new File(BaseTest.reportFolder);
			f.mkdir();

			new File(BaseTest.reportFolder + "\\log").mkdir(); // create a new
																// directory
																// named logs
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(BaseTest.reportFolder);
			sparkReporter.config().setReportName("Rest Assured  Framework 2022");
			sparkReporter.config().setDocumentTitle("Rest Assured Framework Reports");
			/*
			 * sparkReporter.config().setTheme(Theme.STANDARD);
			 * sparkReporter.config().setEncoding("utf-8");
			 */

			reports.attachReporter(sparkReporter);
		}

		return reports;

	}

}
