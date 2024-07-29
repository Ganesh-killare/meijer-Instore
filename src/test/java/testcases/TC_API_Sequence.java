package testcases;

import java.io.IOException;

import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import apisequence.ByPassScreen;
import apisequence.CheckRequest;
import apisequence.GetUserInputRequest;
import apisequence.GiftActivation;
import apisequence.ShowScreenRequest;
import base.BaseClass;
import responsevalidator.Response_Parameters;
import xmlrequestbuilder.Close_Transaction;
import xmlrequestbuilder.Ewic_Sale_Request;
import xmlrequestbuilder.GCB_Modification;
import xmlrequestbuilder.Sale_Request_Modification;

public class TC_API_Sequence {

	BaseClass cp = new BaseClass();

	@Test(invocationCount = 1)
	public void test_GCB_ByPass_Activation() throws InterruptedException, Exception, IOException {
		try {

			BaseClass cp = new BaseClass();

			// GCB

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			System.out.println(req);
			System.out.println("=".repeat(50));

			// GCB_Response_Parameters GCBPrameter = new GCB_Response_Parameters(res);

			// ByPass Screen

			/*
			 * String ByPassScreenReq = ByPassScreen.ByPassScreenRequest("0");
			 * cp.sendRequestToAESDK(ByPassScreenReq); System.out.println(ByPassScreenReq);
			 * String ByPassResponse = cp.receiveResponseToAESDK();
			 * System.out.println(ByPassResponse); System.out.println("=".repeat(50));
			 * 
			 * // Activation Request
			 * 
			 * String ActivationRequest = GiftActivation.modified_Gift_Request();
			 * cp.sendRequestToAESDK(ActivationRequest);
			 * System.out.println(ActivationRequest); String ActivationRes =
			 * cp.receiveResponseToAESDK(); System.out.println(ActivationRes);
			 * System.out.println("=".repeat(50));
			 */

		} finally {
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			System.out.println("=".repeat(50));

			Thread.sleep(5000);

		}

	}

	@Test(invocationCount = 20)
	public void test_GCB_ByPass_Check() throws Exception, IOException {

		try {

			BaseClass cp = new BaseClass();

			// GCB

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			System.out.println(req);
			System.out.println("=".repeat(50));

			// GCB_Response_Parameters GCBPrameter = new GCB_Response_Parameters(res);

			// ByPass Screen

			String ByPassScreenReq = ByPassScreen.ByPassScreenRequest("0");
			cp.sendRequestToAESDK(ByPassScreenReq);
			System.out.println(ByPassScreenReq);
			String ByPassResponse = cp.receiveResponseFromAESDK();
			System.out.println(ByPassResponse);
			System.out.println("=".repeat(50));

			// Check

			String checkReq = CheckRequest.modified_Check_Request();
			cp.sendRequestToAESDK(checkReq);
			System.out.println(checkReq);
			String checkRes = cp.receiveResponseFromAESDK();
			System.out.println(checkRes);
			System.out.println("=".repeat(50));

		} finally {

			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			System.out.println("=".repeat(50));

			Thread.sleep(5000);

		}

	}

	@Test(invocationCount = 2)
	public void test_GCB_ByPass_GetInput_ByPass_Activation_ByPass() throws Exception, IOException {

		try {

			BaseClass cp = new BaseClass();

			// GCB

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			System.out.println(req);
			System.out.println("=".repeat(50));

			// GCB_Response_Parameters GCBPrameter = new GCB_Response_Parameters(res);

			// ByPass Screen

			String ByPassScreenReq = ByPassScreen.ByPassScreenRequest("0");
			cp.sendRequestToAESDK(ByPassScreenReq);
			System.out.println(ByPassScreenReq);
			String ByPassResponse = cp.receiveResponseFromAESDK();
			System.out.println(ByPassResponse);
			System.out.println("=".repeat(50));

			// Get User Input

			String getUserInputReq = GetUserInputRequest.getUserInputRequest();
			cp.sendRequestToAESDK(getUserInputReq);
			System.out.println(getUserInputReq);
			System.out.println("=".repeat(50));

			// ByPass Screen

			String ByPassScreenReq2 = ByPassScreen.ByPassScreenRequest("0");
			cp.sendRequestToAESDK(ByPassScreenReq2);
			System.out.println(ByPassScreenReq2);
			String ByPassResponse2 = cp.receiveResponseFromAESDK();
			System.out.println(ByPassResponse2);
			System.out.println("=".repeat(50));

			// Activation Request

			String ActivationRequest = GiftActivation.modified_Gift_Request();
			cp.sendRequestToAESDK(ActivationRequest);
			System.out.println(ActivationRequest);
			System.out.println("=".repeat(50));

			// ByPass Screen

			String ByPassScreenReq3 = ByPassScreen.ByPassScreenRequest("0");
			cp.sendRequestToAESDK(ByPassScreenReq3);
			System.out.println(ByPassScreenReq3);
			String ByPassResponse3 = cp.receiveResponseFromAESDK();
			System.out.println(ByPassResponse3);
			System.out.println("=".repeat(50));

		} finally {
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			System.out.println("=".repeat(50));
			Thread.sleep(5000);

		}

	}

	@Test(invocationCount = 1)
	public void test_GCB_ByPass_ShowScreen_ByPass_Ewic_Bypass() throws Exception {
		try {

			// GCB

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			System.out.println(req);
			System.out.println("=".repeat(50));

			// ByPass Screen

			String ByPassScreenReq = ByPassScreen.ByPassScreenRequest("0");
			cp.sendRequestToAESDK(ByPassScreenReq);
			System.out.println(ByPassScreenReq);
			String ByPassResponse = cp.receiveResponseFromAESDK();
			System.out.println(ByPassResponse);
			System.out.println("=".repeat(50));

			// Show Screen

			String showScreenReq = ShowScreenRequest.showScreenRequest();
			cp.sendRequestToAESDK(showScreenReq);
			System.out.println(showScreenReq);
			System.out.println("=".repeat(50));

			// ByPass Screen

			String ByPassScreenReq2 = ByPassScreen.ByPassScreenRequest("0");
			cp.sendRequestToAESDK(ByPassScreenReq2);
			System.out.println(ByPassScreenReq2);
			String ByPassResponse2 = cp.receiveResponseFromAESDK();
			System.out.println(ByPassResponse2);
			System.out.println("=".repeat(50));

			// E-wic
			String EwicRequest = Ewic_Sale_Request.modified_Ewic_Sale_Request(null);
			cp.sendRequestToAESDK(EwicRequest);
			System.out.println(EwicRequest);
			System.out.println("=".repeat(50));

			// ByPass Screen

			String ByPassScreenReq3 = ByPassScreen.ByPassScreenRequest("0");
			cp.sendRequestToAESDK(ByPassScreenReq3);
			System.out.println(ByPassScreenReq3);
			String ByPassResponse3 = cp.receiveResponseFromAESDK();
			System.out.println(ByPassResponse3);
			System.out.println("=".repeat(50));

		} finally {
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			System.out.println("=".repeat(50));
			Thread.sleep(5000);

		}

	}

	private static boolean stopTestExecution = false;

	@Test(invocationCount = 1)

	public void testCCTAPI() throws Exception, IOException {
		// Get User Input

		String getUserInputReq = GetUserInputRequest.getUserInputRequest();
		cp.sendRequestToAESDK(getUserInputReq);
		System.out.println(getUserInputReq);
		Thread.sleep(1000);
		System.out.println("=".repeat(50));

		// ByPass Screen

		String ByPassScreenReq = ByPassScreen.ByPassScreenRequest("0");
		cp.sendRequestToAESDK(ByPassScreenReq);
		System.out.println(ByPassScreenReq);
		String ByPassResponse = cp.receiveResponseFromAESDK();
		System.out.println(ByPassResponse);
		System.out.println("=".repeat(50));

		// Sale Request

		String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, null, null, "01");
		cp.sendRequestToAESDK(Sale_Request);
		System.out.println(Sale_Request);

		// ByPass Screen

		String ByPassScreenReq10 = ByPassScreen.ByPassScreenRequest("2");
		cp.sendRequestToAESDK(ByPassScreenReq10);
		System.out.println(ByPassScreenReq10);
		String ByPassResponse10 = cp.receiveResponseFromAESDK();
		System.out.println(ByPassResponse10);
		System.out.println("=".repeat(50));

		/*
		 * String sale_Respose = cp.receiveResponseToAESDK();
		 * System.out.println(sale_Respose); Response_Parameters saleResponseq =
		 * new Response_Parameters(sale_Respose); String responseCode1 =
		 * Response_Parameters.getValue("ResponseCode"); if
		 * ("61008".equals(responseCode1)) {
		 * System.out.println("Response code is as expected. Stopping the test.");
		 * stopTestExecution(); }
		 * 
		 * Assert.assertEquals(responseCode1, "61008");
		 * System.out.println("=".repeat(50));
		 * 
		 * if (stopTestExecution) { throw new
		 * SkipException("Stopping test execution based on a condition"); }
		 */

		// Get User Input

		String getUserInputReq2 = GetUserInputRequest.getUserInputRequest();
		cp.sendRequestToAESDK(getUserInputReq2);
		System.out.println(getUserInputReq2);
		Thread.sleep(1000);
		System.out.println("=".repeat(50));

		// ByPass Screen

		String ByPassScreenReq1 = ByPassScreen.ByPassScreenRequest("2");
		cp.sendRequestToAESDK(ByPassScreenReq1);
		System.out.println(ByPassScreenReq1);
		String ByPassResponse1 = cp.receiveResponseFromAESDK();
		System.out.println(ByPassResponse1);
		System.out.println("=".repeat(50));

		// ByPass Screen

		String ByPassScreenReq2 = ByPassScreen.ByPassScreenRequest("0");
		cp.sendRequestToAESDK(ByPassScreenReq2);
		System.out.println(ByPassScreenReq2);
		String ByPassResponse2 = cp.receiveResponseFromAESDK();
		System.out.println(ByPassResponse2);
		System.out.println("=".repeat(50));

		// Show Screen

		String showScreenReq = ShowScreenRequest.showScreenRequest();
		cp.sendRequestToAESDK(showScreenReq);
		System.out.println(showScreenReq);
		String showScreenRes = cp.receiveResponseFromAESDK();
		System.out.println(showScreenRes);
		System.out.println("=".repeat(50));

		// ByPass Screen

		String ByPassScreenReq3 = ByPassScreen.ByPassScreenRequest("2");
		cp.sendRequestToAESDK(ByPassScreenReq3);
		System.out.println(ByPassScreenReq3);
		String ByPassResponse3 = cp.receiveResponseFromAESDK();
		System.out.println(ByPassResponse3);
		System.out.println("=".repeat(50));

		// GCB

		String req = GCB_Modification.GCB_Request_Modified();
		cp.sendRequestToAESDK(req);
		System.out.println(req);
		String GCBres = cp.receiveResponseFromAESDK();
		System.out.println(GCBres);
		Response_Parameters GCBPrameter = new Response_Parameters(GCBres);
		String cardToken = GCBPrameter.getParameterValue("CardToken");

		// ByPass Screen

		String ByPassScreenReq4 = ByPassScreen.ByPassScreenRequest("0");
		cp.sendRequestToAESDK(ByPassScreenReq4);
		System.out.println(ByPassScreenReq4);
		String ByPassResponse4 = cp.receiveResponseFromAESDK();
		System.out.println(ByPassResponse4);
		System.out.println("=".repeat(50));

		// Sale Request
        System.out.println("This is a card token "+cardToken);
		String Sale_Request2 = Sale_Request_Modification.modified_Sale_Request(cardToken, null, null, "01");
		cp.sendRequestToAESDK(Sale_Request2);
		System.out.println(Sale_Request2);
		String sale_Respose2 = cp.receiveResponseFromAESDK();
		System.out.println(sale_Respose2);
		System.out.println("=".repeat(50));
		Response_Parameters saleResponseQ = new Response_Parameters(sale_Respose2);
		String responseCode = saleResponseQ.getParameterValue("ResponseCode");

		if ("61008".equals(responseCode)) {
			System.out.println("Response code is as expected. Stopping the test.");
			stopTestExecution();
		}

		Assert.assertEquals(responseCode, "61008");
		System.out.println("=".repeat(50));

		if (stopTestExecution) {
			throw new SkipException("Stopping test execution based on a condition");
		}
	}

	// Method to set the stopTestExecution variable
	public static void stopTestExecution() {
		stopTestExecution = true;
	}
}
