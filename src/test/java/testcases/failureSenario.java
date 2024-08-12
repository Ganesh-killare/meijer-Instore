
package testcases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BaseClass;
import reporting.AESDKData;
import responsevalidator.Response_Parameters;
import testcases.processorfailourvantiv.TC_ProcessorFailour17;
import utilities.Utils;
import utilities.P_XL_Utility;
import utilities.ProcessorFailourXL;
import utilities.TransactionXL;
import xmlrequestbuilder.Close_Transaction;
import xmlrequestbuilder.GCB_Modification;
import xmlrequestbuilder.Refund_Request_Modification;
import xmlrequestbuilder.Sale_Request_Modification;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
public class failureSenario {

	String fileName = "W_ProcessorFailure_100.17";  
	String transType = "01"; // Change accordingly
    String transName = "sale"; // Change accordingly
    String hardCodeAmount = "100.17";

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
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			xlWriter.writeHeadline(headlines.get(1));
			xlWriter.saveExcelFile(Utils.setFileName(fileName));
		
		}
	}
	
	// There are 3 CREDIT OR DEBIT Sales (Using Amount 100.17)
		@Test(invocationCount = 5, priority = 5)
		public void F_creditSale() throws Exception, Exception {    
		
			
			System.out.println("PLEASE USE CREDIT CARD  :: This sale is using amount 100.17");
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
						transType, "100.17");

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
					List<String> saleData = saleResponse.print_Response(transName + " :  ", parameters);
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
				cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
				cp.receiveResponseFromAESDK();
				xlWriter.saveExcelFile(Utils.setFileName(fileName));
				xl.saveExcelFile();
				invocationCount ++;
				System.out.println(invocationCount);
			}
		}
		
		// There are 3 CREDIT OR DEBIT Sales (After Failour transactions should be go through wordpay)
		@Test(invocationCount = 3, priority = 8)
		public void SECOUNDARY_creditAndDebitSale() throws Exception, Exception {
			System.out.println("PLEASE USE CREDIT OR DEBIT CARD");
			// excelWriter.writeHeadlineData(notes.get(2));

			try {

				BaseClass cp = new BaseClass();

				// GCB Started

				String req = GCB_Modification.GCB_Request_Modified();   
				cp.sendRequestToAESDK(req);
				// System.out.println(req);
				String res = cp.receiveResponseFromAESDK();
				// System.out.println(res);
				Response_Parameters GCBPrameter = new Response_Parameters(res);

				String Sale_Request = Sale_Request_Modification
						.modified_Sale_Request(GCBPrameter.getParameterValue("CardToken"), null, null, transType);
				// String Sale_Request = Sale_Request_Modification.modified_Sale_Request(
				// null,GCBResponse.getParameterValue("CardIdentifier"), null);
				// String Sale_Request =
				// Sale_Request_Modification.modified_Sale_Request(GCBResponse.getParameterValue("CardToken"),
				// null, null);

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
					List<String> saleData = saleResponse.print_Response(transName + " :  ", parameters);
					saleData.add(3, transName);
					
					List<String> expectedList =  xlWriter.generateTransactionSteps("91", invocationCount);
					
					saleData.add(0, expectedList.get(2));
					saleData.add(saleData.size()-1 , expectedList.get(7));
					
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
				cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
				cp.receiveResponseFromAESDK();
				xlWriter.saveExcelFile(Utils.setFileName(fileName));
				xl.saveExcelFile();

			}

		}
		@Test(priority = 11, dataProvider = "VoidData", dataProviderClass = TC_ProcessorFailour17.class) 

		public void VoidAllTransactions(String TransID, String AurusPayTicketNumber, String amount, String transType,
				String PMI) throws Exception, Exception, InterruptedException {

			try {
				if (!amount.equalsIgnoreCase("0.00") && !amount.isEmpty() && !TransID.substring(0, 1).equalsIgnoreCase("O")) {
					String VOid_Request = Refund_Request_Modification.modified_Refund_Request(transType, amount,
							AurusPayTicketNumber, TransID);

					cp.sendRequestToAESDK(VOid_Request);
					String voidResponse = cp.receiveResponseFromAESDK(); // //

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
				cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
				cp.receiveResponseFromAESDK();
				xlWriter.saveExcelFile(Utils.setFileName(fileName));
			}

		}
}