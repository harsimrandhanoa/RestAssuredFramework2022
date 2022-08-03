package com.retail.base;

import static io.restassured.RestAssured.given;

import java.util.Properties;

import org.testng.Reporter;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Util extends BaseTest {
	public String getSession(Properties testProp) {
		RestAssured.baseURI = testProp.getProperty("baseurl");
		RestAssured.basePath = testProp.getProperty("Login");

		String username = "admin";
		String password = "whizdom";
		Session s = new Session();

		s.setUsername(username);
		s.setPassword(password);

		Response resp = given().contentType(ContentType.JSON).log().all().when().body(s).post();

		sessionId = resp.getHeader("sessionId");

		JsonPath extractor = resp.jsonPath();
		String actualStatus = extractor.getString("loginStatus");
		System.out.println(resp.prettyPrint());

		return sessionId;
	}

}
