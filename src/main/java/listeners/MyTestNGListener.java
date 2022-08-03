package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class MyTestNGListener implements ITestListener {

	public void onTestFailure(ITestResult result) {
	 ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("test");
	 test.log(Status.INFO, " in test falure");

		test.log(Status.FAIL, "Test failed - " + result.getName() +" and the reason is "+result.getThrowable());
	}

	public void onTestSuccess(ITestResult result) {

		ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("test");
		test.log(Status.INFO, " in test pass");
		test.log(Status.PASS, "Test Passed - " + result.getName());
	}

	public void onTestSkipped(ITestResult result) {
		
		System.out.println("In skip------------>");

		ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("test");
		
		String skipReason =  (String) result.getTestContext().getAttribute("skipReason");

		test.log(Status.INFO, " in test skip");
		System.out.println("In skip------------>");
		
		if(!(skipReason == null || skipReason.length() == 0))
		test.log(Status.SKIP, "Test  " + result.getName() + " skipped due to reason "+skipReason);
       else
         test.log(Status.SKIP, "Test  " + result.getName() + " skipped due to unknown reason ");
      
			
	

	}

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub

	}

}