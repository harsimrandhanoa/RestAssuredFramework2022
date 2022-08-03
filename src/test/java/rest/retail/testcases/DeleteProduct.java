package rest.retail.testcases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.retail.base.BaseTest;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteProduct extends BaseTest {

	@Test()
	public void deleteProduct(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String iteration = (String) context.getAttribute("iteration");

		String categoryName = (String) data.get("categoryname");
		String productName = (String) data.get("productname");
		String price = (String) data.get("price");
		String quantity = (String) data.get("quantity");


		log("Deleting product named : " + productName + " under category : " + categoryName
				+ " .Quantity to be added is " + quantity + " at a price of " + price);

		int categoryId = databaseActions.addCategory(categoryName);

		int productId = databaseActions.addProduct(productName, Double.parseDouble(price), Integer.parseInt(quantity),
				categoryId, categoryName);

		if (productId == 0) {
			reportFailure("Failed to add " + productName + " for category name " + categoryName
					+ " to db.So can't delete it  either", true);
		}

		Response resp = given().filter(new RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON)
				.headers("sessionid", sessionId).log().body().delete("/" + productId);

		// Log the response in extent reports
		test.log(Status.INFO, resp.prettyPrint());

		// Pass the filtered post request to the below method. Same procedure as
		// we have used in AddCategory and LoginTest classes.
		addRequestToLink(this.getClass().getSimpleName() + " Request",
				this.getClass().getSimpleName() + " Request-" + iteration, requestWriter.toString());

		JsonPath extractor = resp.jsonPath();

		String actualStatus = extractor.getString("status");

		if (!actualStatus.equals("product deleted successfully")) {
			String errorMessage = extractor.getString("productName");
			log("error message" + errorMessage);
			reportFailure("Failed to delete product named   " + productName + ".The status is --> " + actualStatus
					+ " and the error message we are getting is --> " + errorMessage, true);
		}

		// If control reaches here i.e test did not fail
		if (!databaseValidations.isProductDeleted(productName, Double.parseDouble(price), Integer.parseInt(quantity),
				categoryName)) {
			reportFailure(
					"Product named   " + productName + " for category name " + categoryName + " not deleted from db.",
					true);
		}

		testPass();
	}
}
