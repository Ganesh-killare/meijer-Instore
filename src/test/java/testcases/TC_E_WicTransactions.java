package testcases;

import java.util.List;

import org.testng.annotations.Test;

import base.BaseClass;
import responsevalidator.Response_Parameters;
import utilities.EXLUtility;
import utilities.TransactionXL;
import xmlrequestbuilder.CloseRequest;
import xmlrequestbuilder.Ewic_BalanceInquiry;
import xmlrequestbuilder.Ewic_Sale_Request;
import xmlrequestbuilder.Ewic_Void_Request;
import xmlrequestbuilder.GCB_Modification;

public class TC_E_WicTransactions {

	BaseClass cp = new BaseClass();
	TransactionXL excelWriter = new TransactionXL();

	String[] parameters = { "CardToken", "CardIdentifier", "CRMToken", "CardEntryMode", "TransactionTypeCode",
			"TransactionSequenceNumber", "CardType", "SubCardType", "TotalApprovedAmount", "ResponseText",
			"ResponseCode", "TransactionIdentifier", "AurusPayTicketNum", "ApprovalCode" , "ProcessorMerchantId"};
	EXLUtility xl = new EXLUtility();

	@Test(invocationCount = 2)
	public void test_Ewic_Transactions() throws Exception, Exception {
		try {

			BaseClass cp = new BaseClass();  

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();  
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseFromAESDK(); 
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);
			List<String> GCBXLData = GCBPrameter.print_Response("GCB" , parameters);
			xl.WriteGCBData(GCBXLData);
			String cardToken = GCBPrameter.getParameterValue("CardToken");
			String saleRequest = Ewic_Sale_Request.modified_Ewic_Sale_Request(cardToken);
			
			String BalanceInquiry = Ewic_BalanceInquiry.Ewic_Balance_Inquiry(cardToken);
			
			cp.sendRequestToAESDK(BalanceInquiry);
			String BalanceInRes = cp.receiveResponseFromAESDK();
			// System.out.println(BalanceInRes);
			Response_Parameters BalanceResponse = new Response_Parameters(BalanceInRes);
			List<String> BI_Data = BalanceResponse.print_Response(" Balance Inquiry  : ", parameters);
			BI_Data.add(3, "Balance Inquiry");
			xl.WriteData(BI_Data);

			String BIresponseText = BalanceResponse.getParameterValue("ResponseText");
			
			if (BIresponseText.equalsIgnoreCase("APPROVAL")) {
				cp.sendRequestToAESDK(saleRequest);
				String SaleResponse = cp.receiveResponseFromAESDK();
				// System.out.println(SaleResponse);
				Response_Parameters saleResponse = new Response_Parameters(SaleResponse);
				String saleresponseText = saleResponse.getParameterValue("ResponseText");
				String transactionID = saleResponse.getParameterValue("TransactionIdentifier");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
				saleData.add(3, "sale");
				xl.WriteData(saleData);
				if (saleresponseText.equalsIgnoreCase("APPROVAL")) {
					String voidRequest = Ewic_Void_Request.modified_Void_Request(AurusPayTicketNum, transactionID);
					cp.sendRequestToAESDK(voidRequest);
					String voidResponse = cp.receiveResponseFromAESDK();
					Response_Parameters voidRes = new Response_Parameters(voidResponse);

					List<String> voidData = voidRes.print_Response("Void", parameters);
					voidData.add(3, "VOID");
					xl.WriteData(voidData);

				}
			}

		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			xl.saveExcelFile();
			Thread.sleep(5000); 
		}

	}

}
