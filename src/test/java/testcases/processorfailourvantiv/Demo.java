package testcases.processorfailourvantiv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseClass;
import reporting.AESDKData;
import responsevalidator.Response_Parameters;
import testcases.processorfailourchase.TC_ProcessorFailour13;
import utilities.Utils;
import utilities.ExcelUtility;
import utilities.P_XL_Utility;
import utilities.ProcessorFailourXL;
import xmlrequestbuilder.CloseRequest;
import xmlrequestbuilder.EBT_Sale_Request;
import xmlrequestbuilder.Ewic_BalanceInquiry;
import xmlrequestbuilder.Ewic_Sale_Request;
import xmlrequestbuilder.FSA_Sale_Request;
import xmlrequestbuilder.GCB_Modification;
import xmlrequestbuilder.Refund_Request_Modification;
import xmlrequestbuilder.Sale_Request_Modification;

public class Demo {
	
	String fileName = "C_ProcessorFailure_100.13";  
	String transType = "01"; // Change accordingly
    String transName = "sale"; // Change accordingly

	BaseClass cp = new BaseClass();
	ProcessorFailourXL xlWriter = new ProcessorFailourXL();    
	AESDKData ad = new AESDKData();

	String[] parameters = { "CardToken", "CardIdentifier", "CRMToken", "CardEntryMode", "TransactionTypeCode",
			"TransactionSequenceNumber", "CardType", "SubCardType", "TotalApprovedAmount", "ResponseText",
			"ResponseCode", "TransactionIdentifier", "AurusPayTicketNum", "ApprovalCode", "ProcessorMerchantId" };
	List<String> GCB_Parameters = new ArrayList<>(Arrays.asList(parameters));
	P_XL_Utility xl = new P_XL_Utility();

	
	List<String> headlines =  xlWriter.headlineSetter("91", "ISSUER OR SWITCH OPERATIVE", "CHASE");

	private static int invocationCount = 1;

	@BeforeClass   
	public void setHeadlines() throws Exception, Exception, InterruptedException {
		
		
		System.out.println("PLEASE USE CREDIT CARD");

		try {

			BaseClass cp = new BaseClass();   

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);

			String Sale_Request = Sale_Request_Modification.P_SALE_REQUEST(GCBPrameter.getParameterValue("CardToken"),     
					GCBPrameter.getParameterValue("CardIdentifier"), GCBPrameter.getParameterValue("CRMToken"), transType,
					"45.00");

			 GCBPrameter.print_Response("GCB", parameters);
			 

		
			String result = GCBPrameter.getParameterValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				// GCBPrameter.print_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseFromAESDK();
				// System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(transName + "  :  ", parameters);
				saleData.add(3, transName);
				
			
					xlWriter.writeHeadline(headlines.get(0));
					List<String> expectedList =  xlWriter.generateTransactionSteps("91", 0);
					saleData.add(0, expectedList.get(0));
					saleData.add(saleData.size()-1 , expectedList.get(5));
				
					xlWriter.writeTransactionData(saleData);
				ad.writeAESDKRequestAndResponse(Sale_Request, sale_Respose);

				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");

				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String PMI = saleResponse.getParameterValue("ProcessorMerchantId");
				List<String> data = Arrays.asList(transactionIdentifier, AurusPayTicketNum, Amount, "06", PMI);
				xl.writeDataForVoid(data);

			}
		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			xlWriter.writeHeadline(headlines.get(1));
			xlWriter.saveExcelFile(Utils.setFileName(fileName));
		
		}
	}
	// 2 Credit or Debit sale are there   
	      
	@Test(invocationCount = 2, priority = 1)   
	public void DebitSale() throws Exception, Exception {  
		System.out.println("PLEASE USE CREDIT OR DEBIT CARD");

		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);   

			String Sale_Request = Sale_Request_Modification.modified_Sale_Request(
					GCBPrameter.getParameterValue("CardToken"), GCBPrameter.getParameterValue("CardIdentifier"),   
					GCBPrameter.getParameterValue("CRMToken"), transType);

			 GCBPrameter.print_Response("GCB", parameters);

			 

		
			String result = GCBPrameter.getParameterValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				// GCBPrameter.print_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseFromAESDK();
				// System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);

				List<String> saleData = saleResponse.print_Response(transName + "  :  ", parameters);
				saleData.add(3, transName);
				

				
				List<String> expectedList =  xlWriter.generateTransactionSteps("91", 0);
				saleData.add(0, expectedList.get(0));
				saleData.add(saleData.size()-1 , expectedList.get(5));
			
				xlWriter.writeTransactionData(saleData);
				ad.writeAESDKRequestAndResponse(Sale_Request, sale_Respose);

				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");

				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String PMI = saleResponse.getParameterValue("ProcessorMerchantId");
				List<String> data = Arrays.asList(transactionIdentifier, AurusPayTicketNum, Amount, "06", PMI);  
				xl.writeDataForVoid(data);

			}
		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			

			xlWriter.saveExcelFile(Utils.setFileName(fileName));
			xl.saveExcelFile();
		}
	}


	// There are 1 FSA Sales (Normal Sales)
//	@Test(priority = 2)
	public void P_FSA() throws Exception, Exception {  
		System.out.println("PLEASE USE FSA CARD");
		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = GCB_Modification.fsa_GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);   


			String Sale_Request = FSA_Sale_Request.modified_FSA_SALE_Request(
					GCBPrameter.getParameterValue("CardToken"), GCBPrameter.getParameterValue("CardIdentifier"),
					GCBPrameter.getParameterValue("CRMToken"), transType);
			// System.out.println(Sale_Request);
			 GCBPrameter.print_Response("GCB", parameters);
			 
			String result = GCBPrameter.getParameterValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				// GCBPrameter.print_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseFromAESDK();
				// System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(transName + "  :  ", parameters);
				saleData.add(3, transName);
			 
				List<String> expectedList =  xlWriter.generateTransactionSteps("91", 0);
				saleData.add(0, expectedList.get(0));
				saleData.add(saleData.size()-1 , expectedList.get(5));
			
				xlWriter.writeTransactionData(saleData);

				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");  
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");

				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String PMI = saleResponse.getParameterValue("ProcessorMerchantId");
				List<String> data = Arrays.asList(transactionIdentifier, AurusPayTicketNum, Amount, "06", PMI);
				xl.writeDataForVoid(data);
			}
		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
		
			xlWriter.saveExcelFile(Utils.setFileName(fileName));
			xl.saveExcelFile();
		}
	}

	// There are 1 E-wic sale

//	@Test(invocationCount = 1, priority = 3)
	public void Ewic_Transactions() throws Exception, Exception {   
		System.out.println("PLEASE USE E-WIC CARDS");
		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);
			 GCBPrameter.print_Response("GCB", parameters);
			 
			String cardToken = GCBPrameter.getParameterValue("CardToken");
			String saleRequest = Ewic_Sale_Request.modified_Ewic_Sale_Request(cardToken);

			String BalanceInquiry = Ewic_BalanceInquiry.Ewic_Balance_Inquiry(cardToken);

			cp.sendRequestToAESDK(BalanceInquiry);
			String BalanceInRes = cp.receiveResponseFromAESDK();
			// System.out.println(BalanceInRes);
			Response_Parameters BalanceResponse = new Response_Parameters(BalanceInRes);
			List<String> BI_Data = BalanceResponse.print_Response(" Balance Inquiry  : ", parameters);
			BI_Data.add(3, "Balance Inquiry");
			

			String BIresponseText = BalanceResponse.getParameterValue("ResponseText");

			if (BIresponseText.equalsIgnoreCase("APPROVAL")) {
				cp.sendRequestToAESDK(saleRequest);
				String SaleResponse = cp.receiveResponseFromAESDK();
				// System.out.println(SaleResponse);
				Response_Parameters saleResponse = new Response_Parameters(SaleResponse);
				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TotalApprovedAmount");
				String PMI = saleResponse.getParameterValue("ProcessorMerchantId");
				List<String> saleData = saleResponse.print_Response(transName + "  :  ", parameters);
				saleData.add(3, transName);
				
				List<String> expectedList =  xlWriter.generateTransactionSteps("91", 0);
				saleData.add(0, expectedList.get(0));
				saleData.add(saleData.size()-1 , expectedList.get(5));
			
				xlWriter.writeTransactionData(saleData);
				
				List<String> data = Arrays.asList(transactionIdentifier, AurusPayTicketNum, Amount, "06", PMI);
				xl.writeDataForVoid(data);

			}

		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			xlWriter.saveExcelFile(Utils.setFileName(fileName));
			xl.saveExcelFile();
			cp.receiveResponseFromAESDK();
		
		}

	}

	// There are 2 EBT Sales (Normal Sales)

	// @Test(invocationCount = 2, priority = 4)

	public void P_EBT() throws Exception {
		System.out.println("PLEASE USE EBT CARD");

		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);

			String Sale_Request = EBT_Sale_Request.modified_Sale_Request(GCBPrameter.getParameterValue("CardToken"), transType);

			 GCBPrameter.print_Response("GCB", parameters);
			 
			String result = GCBPrameter.getParameterValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				// GCBPrameter.print_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseFromAESDK();
				// System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(transName + "  :  ", parameters);
				saleData.add(3, transName);
			

				List<String> expectedList =  xlWriter.generateTransactionSteps("91", 0);
				saleData.add(0, expectedList.get(0));
				saleData.add(saleData.size()-1 , expectedList.get(5));
			
				xlWriter.writeTransactionData(saleData);
				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");

				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String PMI = saleResponse.getParameterValue("ProcessorMerchantId");
				List<String> data = Arrays.asList(transactionIdentifier, AurusPayTicketNum, Amount, "06", PMI);
				xl.writeDataForVoid(data);
			}
		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			xlWriter.saveExcelFile(Utils.setFileName(fileName));
			xl.saveExcelFile();
		}
	}

	// There are 3 CREDIT OR DEBIT Sales (Using Amount 100.13)
	@Test(invocationCount = 3, priority = 5)
	public void F_creditSale() throws Exception, Exception {    
	
		
		System.out.println("PLEASE USE CREDIT CARD  :: This sale is using amount 100.13");
		// excelWriter.writeHeadlineData(notes.get(1));

		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);     

			String Sale_Request = Sale_Request_Modification.P_SALE_REQUEST(GCBPrameter.getParameterValue("CardToken"),
					(GCBPrameter.getParameterValue("CardIdentifier")), (GCBPrameter.getParameterValue("CRMToken")),
					transType, "100.13");

			 GCBPrameter.print_Response("GCB", parameters);

			String result = GCBPrameter.getParameterValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				// GCBPrameter.print_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseFromAESDK();
				// System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(transName + "  :  ", parameters);
				saleData.add(3, transName);
				
				
				List<String> expectedList =  xlWriter.generateTransactionSteps("91", invocationCount);
				saleData.add(0, expectedList.get(1));
				saleData.add(saleData.size()-1 , expectedList.get(6));
				saleData.add(expectedList.get(expectedList.size()-1));
				
			
				xlWriter.writeTransactionData(saleData);
				ad.writeAESDKRequestAndResponse(Sale_Request, sale_Respose);

				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");

				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String PMI = saleResponse.getParameterValue("ProcessorMerchantId");
				List<String> data = Arrays.asList(transactionIdentifier, AurusPayTicketNum, Amount, "06", PMI);
				xl.writeDataForVoid(data);

			}
		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			xlWriter.saveExcelFile(Utils.setFileName(fileName));
			xl.saveExcelFile();
			invocationCount ++;
			System.out.println(invocationCount);
		}
	}
	
	
	
	@Test(invocationCount = 2, priority = 6)   
	public void afterFailureCreditDebitSale() throws Exception, Exception {  
		System.out.println("PLEASE USE CREDIT OR DEBIT CARD");

		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);   

			String Sale_Request = Sale_Request_Modification.modified_Sale_Request(
					GCBPrameter.getParameterValue("CardToken"), GCBPrameter.getParameterValue("CardIdentifier"),   
					GCBPrameter.getParameterValue("CRMToken"), transType);

			 GCBPrameter.print_Response("GCB", parameters);

			 

		
			String result = GCBPrameter.getParameterValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				// GCBPrameter.print_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseFromAESDK();
				// System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);

				List<String> saleData = saleResponse.print_Response(transName + "  :  ", parameters);
				saleData.add(3, transName);
				

				
				List<String> expectedList =  xlWriter.generateTransactionSteps("91", 0);
				saleData.add(0, expectedList.get(0));
				saleData.add(saleData.size()-1 , expectedList.get(5));
			
				xlWriter.writeTransactionData(saleData);
				ad.writeAESDKRequestAndResponse(Sale_Request, sale_Respose);

				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");

				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String PMI = saleResponse.getParameterValue("ProcessorMerchantId");
				List<String> data = Arrays.asList(transactionIdentifier, AurusPayTicketNum, Amount, "06", PMI);  
				xl.writeDataForVoid(data);

			}
		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			

			xlWriter.saveExcelFile(Utils.setFileName(fileName));
			xl.saveExcelFile();
		}
	}


	@Test(priority = 11, dataProvider = "VoidData", dataProviderClass = TC_ProcessorFailour13.class)
	public void VoidAllTransactions(String TransID, String AurusPayTicketNumber, String amount, String transType,
			String PMI) throws Exception, Exception, InterruptedException {

		try {
			if (!amount.equalsIgnoreCase("0.00") && !amount.isEmpty() && !TransID.substring(0, 1).equalsIgnoreCase("O")) {
				String VOid_Request = Refund_Request_Modification.modified_Refund_Request(transType, amount,
						AurusPayTicketNumber, TransID);

				cp.sendRequestToAESDK(VOid_Request);
				String voidResponse = cp.receiveResponseFromAESDK(); 

				Response_Parameters VoidResponse = new Response_Parameters(voidResponse); // IMP

				List<String> VoidData = VoidResponse.print_Response("VOID", parameters);
				VoidData.add(3, "Void");
				
				

				List<String> expectedList =  xlWriter.generateTransactionSteps("91", invocationCount);  
				
				VoidData.add(0, expectedList.get(0));
				VoidData.add(VoidData.size()-1 , expectedList.get(8));
				VoidData.add("Parent transId is :- " + TransID + " ,  AurusPayTicketNumber :- " + AurusPayTicketNumber
						+ " And sale Processor MID is :- " + PMI);
				
				xlWriter.writeTransactionData(VoidData);
				
			
				String ProMerID = VoidResponse.getParameterValue("ProcessorMerchantId");
				ad.writeAESDKRequestAndResponse(VOid_Request, voidResponse);
				Assert.assertEquals(ProMerID, PMI, "Processor ID MissMatch.......");
			}

		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			xlWriter.saveExcelFile(Utils.setFileName(fileName));
		}

	}

	// This is the data provider method for the "Void Transactions"  

	@DataProvider(name = "VoidData")
	public String[][] allTransactionsVoid() throws IOException {
		String path = "./test-Data\\VoidTransactions.xlsx";// taking xl file from testData

		ExcelUtility xlutil = new ExcelUtility(path);// creating an object for XLUtility

		int totalrows = xlutil.getRowCount("Transactions");
		int totalcols = xlutil.getCellCount("Transactions", 1);

		String logindata[][] = new String[totalrows][totalcols];// created for two dimension array which can store the
																// data user and password

		for (int i = 1; i <= totalrows; i++) // 1 //read the data from xl storing in two deminsional array
		{
			for (int j = 0; j < totalcols; j++) // 0 i is rows j is col
			{
				logindata[i - 1][j] = xlutil.getCellData("Transactions", i, j); // 1,0
			}
		}
		return logindata;// returning two dimension array
	}



}
