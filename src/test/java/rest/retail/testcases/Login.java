package rest.retail.testcases;

import static io.restassured.RestAssured.given;

import java.util.Hashtable;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;
//import com.aventstack.extentreports.Status;
import com.retail.base.BaseTest;
import com.retail.base.Session;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Login extends BaseTest {

	@Test()
	public void doLogin(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");

		String iteration = (String) context.getAttribute("iteration");
		Session s = new Session();

		String username = (String) data.get("username");
		String password = (String) data.get("password");
		String expectedStatus = (String) data.get("expectedstatus");

		log("Logging into app using username :" + username + " and  password: " + password
				+ " The expected login status is " + expectedStatus);

		s.setUsername(username);
		s.setPassword(password);
		// Response resp = given().filter(new
		// RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON).log().all().when().body(s).post();

		// Without logging the request

		Response resp = given().filter(new RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON).log()
				.all().when().body(s).post();

		sessionId = resp.getHeader("sessionId");
		log("The session id is " + sessionId);

		JsonPath extractor = resp.jsonPath();
		String actualStatus = extractor.getString("loginStatus");
		addRequestToLink(this.getClass().getSimpleName() + " Request",
				this.getClass().getSimpleName() + " Request-" + iteration, requestWriter.toString());
		log(resp.prettyPrint());

		if (expectedStatus.equals("Login failed")) {
			String errorMessage = extractor.getString("errMsg");

			if (!actualStatus.equals("failure") && !errorMessage.equals("invalid username/password")) {
				reportFailure(
						"Login should have failed with appropriate message but instead the message we are getting is errorMessage and the login status is "
								+ actualStatus,
						true);
			}
		}

		if (expectedStatus.equals("Login succeed") && !sessionId.matches("^\\w+$")) {
			reportFailure("Failed to login into session as sessionId is " + sessionId, true);
		}

		// If control reaches here i.e test did not fail
		testPass();
	}

}
