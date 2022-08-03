package rest.retail.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJsonData {

	public Object[][] getTestData(String path, String testName)
			throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(path)));

		JSONArray testArray = (JSONArray) json.get(testName);

		Object[][] data = new Object[testArray.size()][1];

		Hashtable<String, String> table = null;

		for (int i = 0; i < testArray.size(); i++) {

			table = new Hashtable<String, String>();

			JSONObject testData = (JSONObject) testArray.get(i);

			Set<String> keys = testData.keySet();

			for (String key : keys) {
				table.put(key, (String) testData.get(key));
			}

			data[i][0] = table;
		}

		return data;

	}

}