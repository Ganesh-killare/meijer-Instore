package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import base.BaseClass;
import requestbuilder.GiftRequest;
import responsevalidator.Response_Parameters;
import utilities.Utils;
import utilities.GIft_Data;
import utilities.GiftDataXLwriting;
import xmlrequestbuilder.CloseRequest;
import xmlrequestbuilder.GCB_Modification;
import xmlrequestbuilder.Gift_Request_Modification;

public class TC_Gift_Activation extends BaseClass {

	int startRow = 1;
	List<CellRangeAddress> mergedRegions = new ArrayList<>();

	Faker faker = new Faker();
	GiftDataXLwriting exceldata = new GiftDataXLwriting();

	String[] parameters = { "CardToken", "CardNumber", "ProcessorToken", "CardEntryMode", "TransactionTypeCode",
			"SubTransType", "TransactionSequenceNumber", "CardType", "SubCardType", "TotalApprovedAmount",
			"ResponseText", "ResponseCode", "TransactionIdentifier", "AurusPayTicketNum", "ApprovalCode",
			"ProcessorMerchantId", "BlackHawkUpc", "ProcessorToken", "CardExpiryDate" };

	List<String> Parameters = new ArrayList<>(Arrays.asList(parameters));
	String Gift_PreAuthActivationreq;

//	@Test(dataProvider = "Gift_PreAuth_ActivationData", dataProviderClass = GIft_Data.class, priority = 1)
	/*
	 * public void test_Gift_Reload(String transactionType, String amount, String
	 * cardNumber, String entrySource, String transtype, String subtransType, String
	 * upsdata) throws UnknownHostException, IOException, InterruptedException,
	 * Exception {
	 * 
	 * try {
	 * 
	 * String Gift_PreAuthActivationreq =
	 * Gift_Request_Modification.modified_Gift_Request(amount, cardNumber,
	 * entrySource, subtransType, transtype, upsdata, null); //
	 * System.out.println(Gift_PreAuthActivationreq);
	 * 
	 * cp.sendRequestToAESDK(Gift_PreAuthActivationreq); String
	 * response_PreAuthActivation = receiveResponseFromAESDK();
	 * Gift_Response_Parameters giftparameters = new
	 * Gift_Response_Parameters(response_PreAuthActivation); List<String>
	 * gift_Activation =
	 * Gift_Response_Parameters.print_Gift_Response("Gift Activation", parameters);
	 * 
	 * if (transactionType.equalsIgnoreCase("Pre-Auth activation")) {
	 * exceldata.WriteActivationData(gift_Activation); } else {
	 * gift_Activation.add(3, transactionType);
	 * exceldata.writeDataRefundOfSale(gift_Activation); }
	 * 
	 * } finally {
	 * cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
	 * Thread.sleep(5000); exceldata.saveExcelFile();
	 * 
	 * }
	 * 
	 * }
	 */

	@Test(dataProvider = "GIFT_DATA", dataProviderClass = GIft_Data.class, priority = 1)   
	public void test_Gift(String transactionType, String amount, String cardNumber, String entrySource,
			String transtype, String subtransType, String upsdata)
			throws UnknownHostException, IOException, InterruptedException, Exception {    

		try {

			String giftRequest = GiftRequest.GIFT_REQUEST(amount, cardNumber, entrySource, subtransType, transtype,
					upsdata, null);
			
			// System.out.println(giftRequest);

			sendRequestToAESDK(giftRequest);
			String giftResponse = receiveResponseFromAESDK();
			Response_Parameters giftparameters = new Response_Parameters(giftResponse);
			List<String> gift_Activation = giftparameters.print_Response(transactionType, parameters);      

			if (transactionType.contains("Pre-Auth activation")) {
				exceldata.WriteActivationData(gift_Activation, Parameters); 
			} else {
				gift_Activation.add(3, transactionType);
				exceldata.writeDataRefundOfSale(gift_Activation);
			}

		} finally {
			sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			receiveResponseFromAESDK();
			exceldata.saveExcelFile(Utils.setFileName("gift_Transactions"));

		}

	}

	
	// Use this method for GCB Swipe 
	
	@Test(dataProvider = "GIFT_DATA_S_M", dataProviderClass = GIft_Data.class, priority = 1)
	public void testGiftTransactionsUsingGCBFlow(String transactionType, String amount, String cardNumber,
			String entrySource, String transtype, String subtransType, String upsdata)
			throws UnknownHostException, IOException, InterruptedException, Exception {        

		try {

			// GCB        

			String req = GCB_Modification.GCB_Request_Modified();      
			sendRequestToAESDK(req); // System.out.println(req);
			String res = receiveResponseFromAESDK(); // System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);
			String CardToken = GCBPrameter.getParameterValue("CardToken");   
			upsdata = GiftRequest.getUPCdata(CardToken);

			GCBPrameter.print_Response("GCB", parameters);    

			String result = GCBPrameter.getParameterValue("ResponseText");
			if (result.equalsIgnoreCase("Approved")) {

				String giftRequest = GiftRequest.GIFT_REQUEST(amount, cardNumber, entrySource, subtransType, transtype,
						upsdata, CardToken); //
			//	System.out.println(giftRequest);

				sendRequestToAESDK(giftRequest);
				String giftResponse = receiveResponseFromAESDK();
				Response_Parameters giftparameters = new Response_Parameters(giftResponse);
				List<String> gift_Activation = giftparameters.print_Response(transactionType, parameters);

				if (transactionType.contains("Pre-Auth activation")) {
					exceldata.WriteActivationData(gift_Activation, Parameters);
				} else {
					gift_Activation.add(3, transactionType);
					exceldata.writeDataRefundOfSale(gift_Activation);
				}
			}
		} finally {
			sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			receiveResponseFromAESDK();
			exceldata.saveExcelFile(Utils.setFileName("gift_Transactions"));

		}

	}

	@Test(dataProvider = "GIFT_DATA_S_M", dataProviderClass = GIft_Data.class, priority = 1)
	public void test_Gift_Manual_SWipe(String transactionType, String amount, String cardNumber, String entrySource,
			String transtype, String subtransType, String upsdata)
			throws UnknownHostException, IOException, InterruptedException, Exception {

		try {

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();
			sendRequestToAESDK(req);
			// System.out.println(req);
			String res = receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);
			String result = GCBPrameter.getParameterValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				String Gift_PreAuthActivationreq = Gift_Request_Modification.modified_Gift_Request(amount, null, null,
						subtransType, transtype, upsdata, GCBPrameter.getParameterValue("CardToken"));
				// System.out.println(Gift_PreAuthActivationreq);

				sendRequestToAESDK(Gift_PreAuthActivationreq);
				String response_PreAuthActivation = receiveResponseFromAESDK();
				Response_Parameters giftparameters = new Response_Parameters(response_PreAuthActivation);
				List<String> gift_Activation = giftparameters.print_Response(transactionType, parameters);

				if (transactionType.contains("Pre-Auth activation")) {
					exceldata.WriteActivationData(gift_Activation, Parameters);
				} else {
					gift_Activation.add(3, transactionType);
					exceldata.writeDataRefundOfSale(gift_Activation);
				}
			}

		} finally {
			sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			Thread.sleep(5000);
			exceldata.saveExcelFile(Utils.setFileName("gift_Transactions"));

		}

	}

}
