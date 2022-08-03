package rest.retail.testcases;

import static io.restassured.RestAssured.given;

import java.util.Hashtable;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.retail.base.BaseTest;
import com.retail.base.Category;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AddCategory extends BaseTest {

	@Test
	public void addCategory(ITestContext context) {

		JSONObject data = (JSONObject) context.getAttribute("data");
		String iteration = (String) context.getAttribute("iteration");

		String categoryName = (String) data.get("categoryname");

		Category c = new Category();
		c.setCategoryname(categoryName);

		log("Adding category named " + categoryName);

		Response resp = given().filter(new RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON)
				.headers("sessionid", sessionId).log().all().when().body(c).post();

		// given().filter(new
		// RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON).headers("sessionid",sessionId).log().body().delete("/2");

		// .log().all().when().body(s).post();

		// resp.prettyPrint();

		log(resp.prettyPrint());

		addRequestToLink(this.getClass().getSimpleName() + " Request",
				this.getClass().getSimpleName() + " Request-" + iteration, requestWriter.toString());
		JsonPath extractor = resp.jsonPath();
		String actualStatus = extractor.getString("status");
		if (!actualStatus.equals("success")) {
			String errorMessage = extractor.getString("errMsg");
			reportFailure("Failed to add category named   " + categoryName + " to db.The status is --> " + actualStatus
					+ " and the error message we are getting is --> " + errorMessage, true);
		}

		// If control reaches here i.e test did not fail

		if (!databaseValidations.isCategoryAdded(categoryName)) {
			reportFailure("Category name " + categoryName + " not added to db.", true);
		}

		testPass();
	}

}
