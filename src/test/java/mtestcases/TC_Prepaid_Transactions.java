package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.util.CellRangeAddress;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import base.BaseClass;
import responsevalidator.Response_Parameters;
import utilities.GIft_Data;
import utilities.PrepaidXL;
import xmlrequestbuilder.CloseRequest;
import xmlrequestbuilder.PrepaidGCB_Request;
import xmlrequestbuilder.Prepaid_Request_Modification;

public class TC_Prepaid_Transactions {

	static String RuntimeUPCData;

	int startRow = 1;
	List<CellRangeAddress> mergedRegions = new ArrayList<>();

	BaseClass cp = new BaseClass();
	Faker faker = new Faker();
	PrepaidXL exceldata = new PrepaidXL();

	String[] parameters = { "CardToken", "CardIdentifier", "CRMToken", "CardEntryMode", "TransactionTypeCode",
			"TransactionSequenceNumber", "CardType", "SubCardType", "TotalApprovedAmount", "ResponseText",
			"ResponseCode", "TransactionIdentifier", "AurusPayTicketNum", "ApprovalCode", "ProcessorMerchantId",
			"BlackHawkUpc", "ProcessorToken" };
	String Gift_PreAuthActivationreq;

	@Test(dataProvider = "Prepaid_Data", dataProviderClass = GIft_Data.class, priority = 1)
	public void test_Gift_Manual_CI(String transactionType, String amount, String cardNumber, String entrySource,
			String transtype, String subtransType, String upsdata)
			throws UnknownHostException, IOException, InterruptedException, Exception {

		if (upsdata.isEmpty()) {
			upsdata = RuntimeUPCData;
			System.out.println(upsdata);    
		}

		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = PrepaidGCB_Request.GCB_Request_Modified("N");
			cp.sendRequestToAESDK(req);

			String res = cp.receiveResponseFromAESDK();

			Response_Parameters GCBPrameter = new Response_Parameters(res);
			List<String> GCBXLData = GCBPrameter.print_Response("GCB", parameters);
			String result = GCBPrameter.getParameterValue("ResponseText");
			String CIToken = GCBPrameter.getParameterValue("CardIdentifier");
			String CRMTOken = GCBPrameter.getParameterValue("CRMToken");

			if (result.equalsIgnoreCase("Approved") && transactionType.contains("Pre-Auth activation")
					|| transactionType.contains("Pre-Auth Reload")) {

				String Gift_PreAuthActivationreq = Prepaid_Request_Modification.modified_Prepaid_Request(amount, null,
						null, subtransType, transtype, upsdata, GCBPrameter.getParameterValue("CardToken"));
				cp.sendRequestToAESDK(Gift_PreAuthActivationreq);
				// System.out.println(Gift_PreAuthActivationreq);
				String response_PreAuthActivation = cp.receiveResponseFromAESDK();
				Response_Parameters giftparameters = new Response_Parameters(response_PreAuthActivation);
				List<String> gift_Activation = giftparameters.print_Response(transactionType, parameters);
				gift_Activation.set(1, CIToken);
				gift_Activation.set(2, CRMTOken);
				exceldata.WriteActivationData(gift_Activation, transactionType);
				if (transactionType.contains("Pre-Auth Reload")) {
					RuntimeUPCData = giftparameters.getParameterValue("BlackHawkUpc");

				}   
			} else {
				String Gift_PreAuthActivationreq = Prepaid_Request_Modification.modified_Prepaid_Request2(amount, null,
						null, subtransType, transtype, upsdata, GCBPrameter.getParameterValue("CardIdentifier"));
				cp.sendRequestToAESDK(Gift_PreAuthActivationreq);
				// System.out.println(Gift_PreAuthActivationreq);
				String response_PreAuthActivation = cp.receiveResponseFromAESDK();
				Response_Parameters giftparameters = new Response_Parameters(response_PreAuthActivation);
				List<String> gift_Activation = giftparameters.print_Response(transactionType, parameters);
				gift_Activation.add(3, transactionType);
				exceldata.writeDataRefundOfSale(gift_Activation);
			}
			// System.out.println(Gift_PreAuthActivationreq);

		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			exceldata.saveExcelFile();

		}

	}

	@Test(dataProvider = "Prepaid_Data", dataProviderClass = GIft_Data.class, priority = 1)
	public void test_Gift_Manual_SWipe(String transactionType, String amount, String cardNumber, String entrySource,   
			String transtype, String subtransType, String upsdata)
			throws UnknownHostException, IOException, InterruptedException, Exception {

		if (upsdata.isEmpty()) {

			upsdata = RuntimeUPCData;
			System.out.println(upsdata);
		}

		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = PrepaidGCB_Request.GCB_Request_Modified("N");
			cp.sendRequestToAESDK(req);

			String res = cp.receiveResponseFromAESDK();

			Response_Parameters GCBPrameter = new Response_Parameters(res);
			List<String> GCBXLData = GCBPrameter.print_Response("GCB", parameters);
			String result = GCBPrameter.getParameterValue("ResponseText");
			String CIToken = GCBPrameter.getParameterValue("CardIdentifier");
			String CRMTOken = GCBPrameter.getParameterValue("CRMToken");

			if (result.equalsIgnoreCase("Approved") && transactionType.contains("Pre-Auth activation")
					|| transactionType.contains("Pre-Auth Reload")) {

				String Gift_PreAuthActivationreq = Prepaid_Request_Modification.modified_Prepaid_Request(amount, null,
						null, subtransType, transtype, upsdata, GCBPrameter.getParameterValue("CardToken"));
				cp.sendRequestToAESDK(Gift_PreAuthActivationreq);
				//System.out.println(Gift_PreAuthActivationreq);
				String response_PreAuthActivation = cp.receiveResponseFromAESDK();
				Response_Parameters giftparameters = new Response_Parameters(response_PreAuthActivation);
				List<String> gift_Activation = giftparameters.print_Response(transactionType, parameters);
				gift_Activation.set(1, CIToken);
				gift_Activation.set(2, CRMTOken);
				exceldata.WriteActivationData(gift_Activation, transactionType);
				if (transactionType.contains("Pre-Auth Reload")) {
					RuntimeUPCData = giftparameters.getParameterValue("BlackHawkUpc");

				}
			} else {
				String Gift_PreAuthActivationreq = Prepaid_Request_Modification.modified_Prepaid_Request2(amount, null,
						null, subtransType, transtype, upsdata, GCBPrameter.getParameterValue("CardIdentifier"));
				cp.sendRequestToAESDK(Gift_PreAuthActivationreq);
				// System.out.println(Gift_PreAuthActivationreq);
				String response_PreAuthActivation = cp.receiveResponseFromAESDK();
				Response_Parameters giftparameters = new Response_Parameters(response_PreAuthActivation);
				List<String> gift_Activation = giftparameters.print_Response(transactionType, parameters);
				gift_Activation.add(3, transactionType);
				exceldata.writeDataRefundOfSale(gift_Activation);
			}
			// System.out.println(Gift_PreAuthActivationreq);

		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			exceldata.saveExcelFile();

		}

	}
}
