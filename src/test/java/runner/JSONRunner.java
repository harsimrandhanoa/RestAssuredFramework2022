package runner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class JSONRunner {

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub

		Map<String, String> classMethods = new DataUtil().loadClassMethods();

		JSONRunnerHelper jsonRunnerHelper = new JSONRunnerHelper();

		JSONObject json = jsonRunnerHelper.getTestConfigObject();

		TestNGRunner testNG = jsonRunnerHelper.createTestNgRunner(json);

		JSONArray testSuites = jsonRunnerHelper.getSuitesArray(json);

		for (int tsId = 0; tsId < testSuites.size(); tsId++) {

			Map<String, String> testSuiteData = jsonRunnerHelper.getTestSuiteData((JSONObject) testSuites.get(tsId));

			if (!testSuiteData.isEmpty()) {
				jsonRunnerHelper.addSuiteToRunner(testNG, testSuiteData); // add
																			// test
																			// suite
																			// to
																			// runner

				if (tsId == 0)
					jsonRunnerHelper.addTestNGListener(testNG); // add listener
																// for very
																// first suite
				JSONArray suiteTestCases = jsonRunnerHelper.getTestCasesFromSuite(testSuiteData);

				for (int sId = 0; sId < suiteTestCases.size(); sId++) {

					Map<String, Object> testCaseData = jsonRunnerHelper
							.getTestCaseData((JSONObject) suiteTestCases.get(sId));

					String tRunMode = (String) testCaseData.get("runmode");
					String dataflag = (String) testCaseData.get("dataflag");
					JSONArray methods = (JSONArray) testCaseData.get("methods");

					String testDataJsonFilePath = System.getProperty("user.dir") + "//src//test//resources//json//"
							+ testSuiteData.get("jsonFilePath");

					if (tRunMode != null && tRunMode.equals("Y")) {
						int testdatasets = new DataUtil().getTestDataSets(testDataJsonFilePath, dataflag);
						for (int dsId = 0; dsId < testdatasets; dsId++) {
							testNG.addTest(testCaseData.get("testName") + "-It." + (dsId + 1)); // adding
																								// a
																								// test
																								// case
																								// to
																								// suite
							testNG.addTestParameter("datafilepath", testDataJsonFilePath);
							testNG.addTestParameter("dataflag", dataflag);
							testNG.addTestParameter("iteration", String.valueOf(dsId));
							jsonRunnerHelper.addMethods(testNG, methods, classMethods);

						}

					}

				}
			}

		}
		testNG.run();

	}

}
