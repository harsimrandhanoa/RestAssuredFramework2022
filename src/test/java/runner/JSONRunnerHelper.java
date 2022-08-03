package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONRunnerHelper {

	public String getJsonFolderPath() {
		return System.getProperty("user.dir") + "//src//test//resources//json//";
	}

	public JSONObject getTestConfigObject() {

		File file = new File(getJsonFolderPath() + "testconfig.json");
		JSONParser parser = new JSONParser();

		try {
			return (JSONObject) parser.parse(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public TestNGRunner createTestNgRunner(JSONObject json) {
		String parallelSuites = (String) json.get("parallelsuites");
		return new TestNGRunner(Integer.parseInt(parallelSuites));
	}

	public JSONArray getSuitesArray(JSONObject json) {
		return (JSONArray) json.get("testsuites");
	}

	public Map<String, String> getTestSuiteData(JSONObject testSuite) {
		String runMode = (String) testSuite.get("runmode");
		Map<String, String> testSuiteData = new HashMap<String, String>();

		if (runMode.equals("Y")) {

			testSuiteData.put("name", (String) testSuite.get("name"));
			testSuiteData.put("jsonFilePath", (String) testSuite.get("testdatajsonfile"));
			testSuiteData.put("xlsFilePath", (String) testSuite.get("testdataxlsfile"));
			testSuiteData.put("suiteFileName", (String) testSuite.get("suitefilename"));
			testSuiteData.put("runmode", (String) testSuite.get("runmode"));
			testSuiteData.put("parallelTests", (String) testSuite.get("paralleltests"));

		}
		return testSuiteData;
	}

	public void addSuiteToRunner(TestNGRunner testNG, Map<String, String> suiteData) {

		boolean pTests = false;
		String parallelTests = suiteData.get("parallelTests");
		String name = suiteData.get("name");
		if (parallelTests.equals("Y"))
			pTests = true;
		testNG.createSuite(name, pTests);

	}

	public void addTestNGListener(TestNGRunner testNG) {
		testNG.addListener("listeners.MyTestNGListener");
	}

	public JSONArray getTestCasesFromSuite(Map<String, String> testSuiteData) {
		JSONArray suiteTestCases = null;

		String pathSuiteJson = getJsonFolderPath() + testSuiteData.get("suiteFileName");
		JSONParser suiteParser = new JSONParser();
		JSONObject suiteJSON;
		try {
			suiteJSON = (JSONObject) suiteParser.parse(new FileReader(pathSuiteJson));
			suiteTestCases = (JSONArray) suiteJSON.get("testcases");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return suiteTestCases;
	}

	public Map<String, Object> getTestCaseData(JSONObject suiteTestCase) {

		Map<String, Object> testCaseData = new HashMap<String, Object>();
		testCaseData.put("testName", (String) suiteTestCase.get("name"));
		testCaseData.put("runmode", (String) suiteTestCase.get("runmode"));
		testCaseData.put("methods", (JSONArray) suiteTestCase.get("methods"));
		testCaseData.put("dataflag", (String) suiteTestCase.get("dataflag"));
		return testCaseData;
	}

	public Map<String, Object> getExecutionData(JSONObject execution) {

		Map<String, Object> executionData = new HashMap<String, Object>();

		executionData.put("runmode", (String) execution.get("runmode"));
		executionData.put("name", (String) execution.get("executionname"));
		executionData.put("dataFlag", (String) execution.get("dataflag"));
		executionData.put("parameterValues", execution.get("parametervalues"));
		executionData.put("methods", execution.get("methods"));

		return executionData;

	}

	public void addParametersToRunner(TestNGRunner testNG, JSONArray parameterNames, JSONArray parameterValues) {

		for (int pId = 0; pId < parameterNames.size(); pId++) {
			testNG.addTestParameter((String) parameterNames.get(pId), (String) parameterValues.get(pId));

		}
	}

	public void addMethods(TestNGRunner testNG, JSONArray methods, Map<String, String> classMethods) {

		List<String> includedMethods = new ArrayList<String>();

		for (int mId = 0; mId < methods.size(); mId++) {
			String method = (String) methods.get(mId);
			String methodClass = classMethods.get(method);

			if (mId == methods.size() - 1
					|| !((String) classMethods.get((String) methods.get(mId + 1))).equals(methodClass)) {
				// next method is from different class
				includedMethods.add(method);
				testNG.addTestClass(methodClass, includedMethods);

				includedMethods = new ArrayList<String>();
				;
			} else {
				// same class
				includedMethods.add(method);
			}

		}
	}
}
