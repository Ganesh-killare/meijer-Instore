package mtestcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseClass;
import base.POS_APIs;
import requestbuilder.ByPass;
import requestbuilder.FSARequest;
import requestbuilder.Fleet;
import requestbuilder.GCBRequest;
import requestbuilder.GetUserInput;
import requestbuilder.IncommIQTransRequest;
import requestbuilder.ReturnRequest;
import requestbuilder.ShowScreen;
import requestbuilder.SoloTronRequest;
import responsevalidator.Response_Parameters;
import utilities.ExcelUtility;
import utilities.TransactionXL;
import utilities.Utils;
import xmlrequestbuilder.CloseRequest;
import xmlrequestbuilder.PLCC_Sale_Request_Modification;
import xmlrequestbuilder.Sale_Request_Modification;

public class TC_CIandCRM extends BaseClass {

	List<String> GCB_Parameters = parameters;

	TransactionXL excelWriter = new TransactionXL();

	@BeforeMethod
	public void POS_APIs() throws Exception, IOException, InterruptedException {
		sendRequestToAESDK(GetUserInput.MperkNumberRequest());
		Thread.sleep(800);
		sendRequestToAESDK(ByPass.pureRandom());
		receiveResponseFromAESDK();
		sendRequestToAESDK(ShowScreen.HighValuePromptRequest());
		Thread.sleep(800);
		sendRequestToAESDK(ByPass.pureRandom());
		receiveResponseFromAESDK();
		sendRequestToAESDK(GCBRequest.GCB_REQUEST());
		Thread.sleep(20);
		sendRequestToAESDK(ByPass.pureRandom());
		receiveResponseFromAESDK();

		POS_APIs apis = new POS_APIs();
		apis.performed();
		sendRequestToAESDK(ShowScreen.HighValuePromptRequest());
		performByPassRequest();

	}

	@Test(dataProvider = "CI_Data", dataProviderClass = TC_CIandCRM.class, priority = 1)
	public void testCIRefundOfSale(String CI, String cardType) throws Exception, InterruptedException, JDOMException {

		Utils.checkEligibleTender(CI, cardType);

		try {
			System.out.println("Transaction performed using this CI :" + CI);
			if (cardType.equalsIgnoreCase("PLC")) {
				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, CI, null, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else if (cardType.equalsIgnoreCase("XXC")) {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, CI, null, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = FSARequest.FSA_SALE_REQUEST(null, CI, null);
				sendRequestToAESDK(Sale_Request);
			}

			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Sale ", parameters);
			saleData.add(3, "Sale");
			excelWriter.writeCICRMTransactionData(saleData);
			saleData.remove(3);
			Token_performRefundTransaction(saleData);

		} finally {

		}
	}

	@Test(dataProvider = "CI_Data", dataProviderClass = TC_CIandCRM.class, priority = 2)
	public void testCIVoidOfSale(String CI, String cardType) throws Exception, InterruptedException, JDOMException {

		Utils.checkEligibleTender(CI, cardType);
		try {
			System.out.println("Transaction performed using this CI :" + CI);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, CI, null, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else if (cardType.equalsIgnoreCase("XXC")) {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, CI, null, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = FSARequest.FSA_SALE_REQUEST(null, CI, null);
				sendRequestToAESDK(Sale_Request);
			}

			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Sale ", parameters);
			saleData.add(3, "Sale");
			excelWriter.writeCICRMTransactionData(saleData);
			saleData.remove(3);

			Token_performVoidTransaction(saleData);

		} finally {

		}
	}

	@Test(dataProvider = "CI_Data", dataProviderClass = TC_CIandCRM.class, priority = 3)
	public void testCIVoidOfRefundwithoutsale(String CI, String cardType)
			throws Exception, InterruptedException, JDOMException {

		Utils.checkEligibleTender(CI, cardType);
		try {
			System.out.println("Transaction performed using this CI :" + CI);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, CI, null, "02");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else if (cardType.equalsIgnoreCase("XXC")) {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, CI, null, "02");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = FSARequest.FSA_RW_SALE_REQUEST(null, CI, null);
				sendRequestToAESDK(Sale_Request);
			}

			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Refund Without sale", parameters);
			saleData.add(3, "Refund Without sale");
			excelWriter.writeCICRMTransactionData(saleData);
			saleData.remove(3);

			Token_performVoidTransaction(saleData);

		} finally {

		}
	}

	@Test(dataProvider = "CRM_Data", dataProviderClass = TC_CIandCRM.class, priority = 4)
	public void testCRMRefundOfSale(String CRM, String cardType) throws Exception, InterruptedException, JDOMException {

		Utils.checkEligibleTender(CRM, cardType);

		try {
			System.out.println("Transaction performed using this CRM :" + CRM);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, null, CRM, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else if (cardType.equalsIgnoreCase("XXC")) {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, null, CRM, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = FSARequest.FSA_SALE_REQUEST(null, null, CRM);
				sendRequestToAESDK(Sale_Request);
			}

			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Sale ", parameters);
			saleData.add(3, "Sale");
			excelWriter.writeCICRMTransactionData(saleData);
			saleData.remove(3);

			Token_performRefundTransaction(saleData);

		} finally {

		}
	}

	@Test(dataProvider = "CRM_Data", dataProviderClass = TC_CIandCRM.class, priority = 5)
	public void testCRMVoidOfSale(String CRM, String cardType) throws Exception, InterruptedException, JDOMException {

		Utils.checkEligibleTender(CRM, cardType);
		try {
			System.out.println("Transaction performed using this CRM :" + CRM);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, null, CRM, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else if (cardType.equalsIgnoreCase("XXC")) {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, null, CRM, "01");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = FSARequest.FSA_SALE_REQUEST(null, null, CRM);
				sendRequestToAESDK(Sale_Request);
			}
			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Sale ", parameters);
			saleData.add(3, "Sale");
			excelWriter.writeCICRMTransactionData(saleData);
			saleData.remove(3);

			Token_performVoidTransaction(saleData);

		} finally {

		}
	}

	@Test(dataProvider = "CRM_Data", dataProviderClass = TC_CIandCRM.class, priority = 6)
	public void testCRMVoidOfRefundwithoutsale(String CRM, String cardType)
			throws Exception, InterruptedException, JDOMException {
		Utils.checkEligibleTender(CRM, cardType);
		try {
			System.out.println("Transaction performed using this CRM :" + CRM);

			if (cardType.equalsIgnoreCase("PLC")) {

				String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, null, CRM, "02");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else if (cardType.equalsIgnoreCase("XXC")) {
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, null, CRM, "02");
				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);

			} else {
				String Sale_Request = FSARequest.FSA_RW_SALE_REQUEST(null, null, CRM);
				sendRequestToAESDK(Sale_Request);
			}
			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" Refund Without sale", parameters);
			saleData.add(3, "Refund Without sale");
			excelWriter.writeCICRMTransactionData(saleData);
			saleData.remove(3);

			Token_performVoidTransaction(saleData);

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

	public List<String> Token_performRefundTransaction(List<String> saleResult)
			throws Exception, IOException, InterruptedException {
		// System.out.println( "In the Void Transaction, we used AurusPayTickNum and
		// Transaction ID with a total amount and tender amount of $"+
		// saleResult.get(3));

		Assert.assertNotEquals(saleResult.get(8), "0.00", "Amount is zero ; We are not processes Refund");

		List<String> RefundData = new ArrayList<String>();

		try {

			String transactionId = saleResult.get(11);
			String AurusPayTicketNum = saleResult.get(12);
			String amount = saleResult.get(8);
			String transactionType = saleResult.get(6).toUpperCase();

			if (transactionType.equalsIgnoreCase("EPP")) {
				transactionType = saleResult.get(7).toUpperCase();
			}

			String returnRequest = null;

			switch (transactionType) {
			case "EBF":
			case "EBC":
				System.out.println("EBT Refund");
				returnRequest = ReturnRequest.EBT_REFUND_REQUEST(transactionId, AurusPayTicketNum, amount);
				break;

			case "VIF":
			case "MCF":
			case "VGF":
			case "WXF":
				returnRequest = Fleet.RefundRequest(saleResult);
				break;

			case "STO":
				returnRequest = SoloTronRequest.returnRequest(saleResult);
				break;

			case "EPP":
				returnRequest = IncommIQTransRequest.returnRequest(saleResult);
				break;

			default:
				returnRequest = ReturnRequest.REFUND_REQUEST(transactionId, AurusPayTicketNum, amount);
				break;
			}
			// System.out.println(returnRequest);

			sendRequestToAESDK(returnRequest);
			String refundResponse = receiveResponseFromAESDK();

			Response_Parameters RefundResponse = new Response_Parameters(refundResponse); // IMP

			RefundData = RefundResponse.print_Response("Refund", parameters);
			RefundData.add(3, "Refund");
			excelWriter.writeCICRMTransactionData(RefundData);
		} catch (Exception e) {
			System.out.println("We are not able to performed refund transaction");
		}
		return RefundData;
	}

	public List<String> Token_performVoidTransaction(List<String> saleResult)
			throws Exception, IOException, InterruptedException {

		// System.out.println("In the Void Transaction, we used AurusPayTickNum and
		// Transaction ID with a total amount and tender amount of $" +
		// SaleResult.get(3));
		List<String> VoidData = new ArrayList<String>();

		Assert.assertNotEquals(saleResult.get(8), "0.00", "Amount is zero ; We are not processes Void");
		String returnRequest;

		String transactionId = saleResult.get(11);
		String AurusPayTicketNum = saleResult.get(12);
		String amount = saleResult.get(8);
		String transactionType = saleResult.get(6).toUpperCase();

		try {

			switch (transactionType) {
			case "MCS":
			case "VIS":
			case "NVS":
			case "AXS":
				returnRequest = FSARequest.FSA_VOID_REQUEST(transactionId, AurusPayTicketNum, amount);
				break;

			default:
				returnRequest = ReturnRequest.VOID_REQUEST(transactionId, AurusPayTicketNum, amount);
				break;
			}

			// System.out.println(returnRequest);
			sendRequestToAESDK(returnRequest);
			String VoidResponse = receiveResponseFromAESDK();

			Response_Parameters voidresponse = new Response_Parameters(VoidResponse); // IMP

			VoidData = voidresponse.print_Response("Void", parameters);
			VoidData.add(3, "Void");
			excelWriter.writeCICRMTransactionData(VoidData);
		} catch (Exception e) {
			System.out.println("We are not able to performed void transaction");
		}
		return VoidData;

	}
	// @AfterClass

	@AfterMethod
	public void afterMethod(ITestResult result) throws Exception, InterruptedException, JDOMException {

		// Check if the test method failed
		if (result.getStatus() == ITestResult.FAILURE) {
			// System.out.println("Test failed. Skipping after method execution.");
			/*
			 * sendRequestToAESDK(ByPass.Option1()); receiveResponseFromAESDK();
			 * sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			 * receiveResponseFromAESDK();
			 */
			return; // Exit the method if the test failed
		}

		sendRequestToAESDK(ByPass.Option1());
		receiveResponseFromAESDK();
		sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
		receiveResponseFromAESDK();
		// Proceed with the original operations if the test passed

		excelWriter.saveExcelFile(Utils.setFileName("CI&CRM_Transaction"));
	}

	@AfterClass
	public void tearDown() throws Exception {
		excelWriter.saveExcelFile(Utils.setFileName("CI&CRM_Transaction"));
		sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
		receiveResponseFromAESDK();
	}

}