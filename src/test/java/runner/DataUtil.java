package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataUtil {

	public Map<String, String> loadClassMethods() throws FileNotFoundException, IOException, ParseException {

		File file = new File(System.getProperty("user.dir") + "//src//test//resources//json//classmethods.json");

		Map<String, String> classMethodsMap = new HashMap<String, String>();

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(file));

		JSONArray classDetails = (JSONArray) json.get("classdetails");

		for (int cmId = 0; cmId < classDetails.size(); cmId++) {
			JSONObject classDetail = (JSONObject) classDetails.get(cmId);
			String className = (String) classDetail.get("class");
			JSONArray classMethods = (JSONArray) classDetail.get("methods");

			for (int mId = 0; mId < classMethods.size(); mId++) {
				String method = (String) classMethods.get(mId);
				classMethodsMap.put(method, className);
			}

		}

		return classMethodsMap;
	}

	public int getTestDataSets(String pathFile, String dataFlag)
			throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(pathFile)));

		JSONArray testDataSets = (JSONArray) json.get(dataFlag);
		if (testDataSets.size() != 0)
			return testDataSets.size();
		else
			return 0;
	}

	public JSONObject getTestData(String pathFile, String dataFlag, int iteration)
			throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(pathFile)));

		JSONArray testArray = (JSONArray) json.get(dataFlag);
		JSONObject testData = (JSONObject) testArray.get(iteration);
		return testData;
	}

}
