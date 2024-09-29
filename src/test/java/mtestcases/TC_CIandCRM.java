package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseClass;
import requestbuilder.ByPass;
import requestbuilder.GCBRequest;
import requestbuilder.GetUserInput;
import requestbuilder.ShowScreen;
import responsevalidator.Response_Parameters;
import utilities.Utils;
import utilities.ExcelUtility;
import utilities.TransactionXL;
import xmlrequestbuilder.CloseRequest;
import xmlrequestbuilder.GCB_Modification;
import xmlrequestbuilder.PLCC_Sale_Request_Modification;
import xmlrequestbuilder.Refund_Request_Modification;
import xmlrequestbuilder.Sale_Request_Modification;

public class TC_CIandCRM extends BaseClass {

	List<String> GCB_Parameters = new ArrayList<>(Arrays.asList(parameters));

	TransactionXL excelWriter = new TransactionXL();

	@BeforeMethod
	public void POS_APIs() throws Exception, IOException, InterruptedException {
		sendRequestToAESDK(GetUserInput.MperkNumberRequest());
		Thread.sleep(800);
		sendRequestToAESDK(ByPass.Option1());
		receiveResponseFromAESDK();
		sendRequestToAESDK(ShowScreen.HighValuePromptRequest());
		Thread.sleep(800);
		sendRequestToAESDK(ByPass.Random());
		receiveResponseFromAESDK();
		sendRequestToAESDK(GCBRequest.GCB_REQUEST());
		Thread.sleep(20);
		sendRequestToAESDK(ByPass.Option0());   
		receiveResponseFromAESDK();
		
		
	}

	@Test(dataProvider = "CI_Data", dataProviderClass = TC_CIandCRM.class, priority = 1)    
	public void testCIRefundOfSale(String CI, String cardType) throws Exception, InterruptedException, JDOMException {

		Utils.checkEligibleTender(cardType);

		try {
			System.out.println("Transaction performed using this CI :" + CI);
			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, CI, null, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, CI, null, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
			}

			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Sale ", parameters);
			saleData.add(3, "Sale");
			excelWriter.writeCICRMTransactionData(saleData);
			// Assert.assertEquals(cardType, "MCD");
			String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
			// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
			String responseText = saleResponse.getParameterValue("ResponseText");
			// Assert.assertEquals(responseText, "APPROVAL");
			String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
			String Amount = saleResponse.getParameterValue("TransactionAmount");
			if (responseText.equalsIgnoreCase("APPROVAL")) {
				String RefundRequest = Refund_Request_Modification.modified_Refund_Request("02", Amount,
						AurusPayTicketNum, transactionIdentifier);
				sendRequestToAESDK(RefundRequest);

				String refund = receiveResponseFromAESDK();

				Response_Parameters VoidResponse = new Response_Parameters(refund); // IMP

				List<String> VoidData = VoidResponse.print_Response("Refund", parameters);
				VoidData.add(3, "Refund");
				excelWriter.writeCICRMTransactionData(VoidData);
			}

		} finally {

		}
	}

	@Test(dataProvider = "CI_Data", dataProviderClass = TC_CIandCRM.class, priority = 2)
	public void testCIVoidOfSale(String CI, String cardType) throws Exception, InterruptedException, JDOMException {
		
		
		Utils.checkEligibleTender(cardType);
		try {
			System.out.println("Transaction performed using this CI :" + CI);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, CI, null, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, CI, null, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
			}

			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Sale ", parameters);
			saleData.add(3, "Sale");
			excelWriter.writeCICRMTransactionData(saleData);

			String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
			// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
			String responseText = saleResponse.getParameterValue("ResponseText");
			// Assert.assertEquals(responseText, "APPROVAL");
			String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
			String Amount = saleResponse.getParameterValue("TransactionAmount");
			if (responseText.equalsIgnoreCase("APPROVAL")) {
				String RefundRequest = Refund_Request_Modification.modified_Refund_Request("06", Amount,
						AurusPayTicketNum, transactionIdentifier);
				sendRequestToAESDK(RefundRequest);

				String refund = receiveResponseFromAESDK();

				Response_Parameters VoidResponse = new Response_Parameters(refund); // IMP

				List<String> VoidData = VoidResponse.print_Response(" Void                 ", parameters);
				VoidData.add(3, "Void");
				excelWriter.writeCICRMTransactionData(VoidData);
			}

		} finally {

		}
	}

	@Test(dataProvider = "CI_Data", dataProviderClass = TC_CIandCRM.class, priority = 3)
	public void testCIVoidOfRefundwithoutsale(String CI, String cardType)
			throws Exception, InterruptedException, JDOMException {
		
		Utils.checkEligibleTender(cardType);
		try {
			System.out.println("Transaction performed using this CI :" + CI);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, CI, null, "02");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, CI, null, "02");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
			}
			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Refund Without sale", parameters);
			saleData.add(3, "Refund Without sale");
			excelWriter.writeCICRMTransactionData(saleData);

			String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
			// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
			String responseText = saleResponse.getParameterValue("ResponseText");
			// Assert.assertEquals(responseText, "APPROVAL");
			String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
			String Amount = saleResponse.getParameterValue("TransactionAmount");
			if (responseText.equalsIgnoreCase("APPROVAL")) {
				String RefundRequest = Refund_Request_Modification.modified_Refund_Request("06", Amount,
						AurusPayTicketNum, transactionIdentifier);
				sendRequestToAESDK(RefundRequest);

				String refund = receiveResponseFromAESDK();

				Response_Parameters VoidResponse = new Response_Parameters(refund); // IMP

				List<String> VoidData = VoidResponse.print_Response("Void                                ", parameters);
				VoidData.add(3, "Void");
				excelWriter.writeCICRMTransactionData(VoidData);
			}

		} finally {

		}
	}

	@Test(dataProvider = "CRM_Data", dataProviderClass = TC_CIandCRM.class, priority = 4)
	public void testCRMRefundOfSale(String CRM, String cardType) throws Exception, InterruptedException, JDOMException {

		Utils.checkEligibleTender(cardType);

		try {
			System.out.println("Transaction performed using this CRM :" + CRM);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, null, CRM, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, null, CRM, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
			}
			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Sale ", parameters);
			saleData.add(3, "Sale");
			excelWriter.writeCICRMTransactionData(saleData);

			String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
			// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
			String responseText = saleResponse.getParameterValue("ResponseText");
			// Assert.assertEquals(responseText, "APPROVAL");
			String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
			String Amount = saleResponse.getParameterValue("TransactionAmount");
			if (responseText.equalsIgnoreCase("APPROVAL")) {
				String RefundRequest = Refund_Request_Modification.modified_Refund_Request("02", Amount,
						AurusPayTicketNum, transactionIdentifier);
				sendRequestToAESDK(RefundRequest);

				String refund = receiveResponseFromAESDK();

				Response_Parameters VoidResponse = new Response_Parameters(refund); // IMP

				List<String> VoidData = VoidResponse.print_Response("Refund", parameters);
				VoidData.add(3, "Refund");
				excelWriter.writeCICRMTransactionData(VoidData);
			}

		} finally {

		}
	}

	@Test(dataProvider = "CRM_Data", dataProviderClass = TC_CIandCRM.class, priority = 5)
	public void testCRMVoidOfSale(String CRM, String cardType) throws Exception, InterruptedException, JDOMException {
		
		Utils.checkEligibleTender(cardType);
		try {
			System.out.println("Transaction performed using this CRM :" + CRM);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, null, CRM, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, null, CRM, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
			}
			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Sale ", parameters);
			saleData.add(3, "Sale");
			excelWriter.writeCICRMTransactionData(saleData);

			String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
			// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
			String responseText = saleResponse.getParameterValue("ResponseText");
			// Assert.assertEquals(responseText, "APPROVAL");
			String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
			String Amount = saleResponse.getParameterValue("TransactionAmount");
			if (responseText.equalsIgnoreCase("APPROVAL")) {
				String RefundRequest = Refund_Request_Modification.modified_Refund_Request("06", Amount,
						AurusPayTicketNum, transactionIdentifier);
				sendRequestToAESDK(RefundRequest);

				String refund = receiveResponseFromAESDK();

				Response_Parameters VoidResponse = new Response_Parameters(refund); // IMP

				List<String> VoidData = VoidResponse.print_Response(" Void                 ", parameters);
				VoidData.add(3, "Void");
				excelWriter.writeCICRMTransactionData(VoidData);
			}

		} finally {

		}
	}

	@Test(dataProvider = "CRM_Data", dataProviderClass = TC_CIandCRM.class, priority = 6)
	public void testCRMVoidOfRefundwithoutsale(String CRM, String cardType)
			throws Exception, InterruptedException, JDOMException {
		Utils.checkEligibleTender(cardType);
		try {
			System.out.println("Transaction performed using this CRM :" + CRM);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, null, CRM, "02");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, null, CRM, "02");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
			}
			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Refund Without sale", parameters);
			saleData.add(3, "Refund Without sale");
			excelWriter.writeCICRMTransactionData(saleData);

			String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
			// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
			String responseText = saleResponse.getParameterValue("ResponseText");
			// Assert.assertEquals(responseText, "APPROVAL");
			String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
			String Amount = saleResponse.getParameterValue("TransactionAmount");
			if (responseText.equalsIgnoreCase("APPROVAL")) {
				String RefundRequest = Refund_Request_Modification.modified_Refund_Request("06", Amount,
						AurusPayTicketNum, transactionIdentifier);
				sendRequestToAESDK(RefundRequest);

				String refund = receiveResponseFromAESDK();

				Response_Parameters VoidResponse = new Response_Parameters(refund); // IMP

				List<String> VoidData = VoidResponse.print_Response("Void                                ", parameters);
				VoidData.add(3, "Void");
				excelWriter.writeCICRMTransactionData(VoidData);
			}

		} finally {

		}
	}

	@DataProvider(name = "CI_Data")
	public String[][] getData() throws IOException {

		String path;

		if (Utils.getEnvironment().toUpperCase().contains("P")) {
			path = ".\\test-Data\\LiveCIandCRM.xlsx";
		} else {
			path = ".\\test-Data\\CIandCRMs.xlsx";
		}

		ExcelUtility xlutil = new ExcelUtility(path);// creating an object for XLUtility

		int totalrows = xlutil.getRowCount("CI");
		int totalcols = xlutil.getCellCount("CI", 1);

		String logindata[][] = new String[totalrows][totalcols];// created for two dimension array which can store the
		// data user and password

		for (int i = 1; i <= totalrows; i++) // 1 //read the data from xl storing in two deminsional array
		{
			for (int j = 0; j < totalcols; j++) // 0 i is rows j is col
			{
				logindata[i - 1][j] = xlutil.getCellData("CI", i, j); // 1,0
			}
		}
		return logindata;// returning two dimension array

	}

	@DataProvider(name = "CRM_Data")
	public String[][] getCRMData() throws IOException {
		String path;

		if (Utils.getEnvironment().toUpperCase().contains("P")) {
			path = ".\\test-Data\\LiveCIandCRM.xlsx";
		} else {
			path = ".\\test-Data\\CIandCRMs.xlsx";
		}

		ExcelUtility xlutil = new ExcelUtility(path);// creating an object for XLUtility

		int totalrows = xlutil.getRowCount("CRM");
		int totalcols = xlutil.getCellCount("CRM", 1);

		String logindata[][] = new String[totalrows][totalcols];// created for two dimension array which can store the
		// data user and password

		for (int i = 1; i <= totalrows; i++) // 1 //read the data from xl storing in two deminsional array
		{
			for (int j = 0; j < totalcols; j++) // 0 i is rows j is col
			{
				logindata[i - 1][j] = xlutil.getCellData("CRM", i, j); // 1,0
			}
		}
		return logindata;// returning two dimension array

	}

	// @AfterClass

	@AfterMethod
	public void afterMethod(ITestResult result) throws Exception, InterruptedException, JDOMException {   
	    // Check if the test method failed
	    if (result.getStatus() == ITestResult.FAILURE) {
	      //  System.out.println("Test failed. Skipping after method execution.");      
	        return; // Exit the method if the test failed
	    }
	    
	    // Proceed with the original operations if the test passed
	    sendRequestToAESDK(ByPass.Option1());
	    receiveResponseFromAESDK();
	    sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
	    receiveResponseFromAESDK();
	    excelWriter.saveExcelFile(Utils.setFileName("CI&CRM_Transaction"));
	}

}