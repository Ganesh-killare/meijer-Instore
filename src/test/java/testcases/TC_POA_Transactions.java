package testcases;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.JDOMException;
import org.testng.annotations.Test;

import base.BaseClass;
import responsevalidator.Response_Parameters;
import utilities.Utils;
import utilities.TransactionXL;
import xmlrequestbuilder.A_L_Key_Request_Modification;
import xmlrequestbuilder.CheckRequestModification;
import xmlrequestbuilder.Close_Transaction;
import xmlrequestbuilder.PLCC_Sale_Request_Modification;
import xmlrequestbuilder.POA_RequestModification;
import xmlrequestbuilder.Sale_Request_Modification;
import xmlrequestbuilder.Signature_Request_Modification;

public class TC_POA_Transactions {

	BaseClass cp = new BaseClass();
	TransactionXL excelWriter = new TransactionXL();

	String[] parameters = { "CardToken", "CardIdentifier", "CRMToken", "CardEntryMode", "TransactionTypeCode",
			"TransactionSequenceNumber", "CardType", "SubCardType", "TotalApprovedAmount", "ResponseText",
			"ResponseCode", "TransactionIdentifier", "AurusPayTicketNum", "ApprovalCode" };
	List<String> GCB_Parameters = new LinkedList<String>(Arrays.asList(parameters));

	@Test(invocationCount = 1, priority = 1)
	public void test_ALUwith_Key_POA() throws Exception, IOException, InterruptedException, JDOMException {
		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = A_L_Key_Request_Modification.modified_AL_Key_Request("69");
			cp.sendRequestToAESDK(req);
			 System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			 System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);
			String CITOken = GCBPrameter.getParameterValue("CardIdentifier");

			String Sale_Request = PLCC_Sale_Request_Modification.modified_PLCCSale_Request(null, CITOken ,null, "01");

			List<String> GCBXLData = GCBPrameter.print_Response("Account LookUp Key ", parameters);
			excelWriter.WriteGCBData( GCBXLData, "ALU key");
			String result = GCBPrameter.getParameterValue("ResponseText");
			
			// String CRMTOken = GCB_Response_Parameters.getParameterValue("ResponseText");

			if (result.equalsIgnoreCase("APPROVAL")) {

				// GCBPrameter.print_GCB_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseFromAESDK();
				// System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);

				String cardType = saleResponse.getParameterValue("CardType");
				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");

				if (responseText.equalsIgnoreCase("APPROVAL")) {
					String POA_Request = POA_RequestModification.modified_POA_Request("53", AurusPayTicketNum, CITOken,
							"DB");
					cp.sendRequestToAESDK(POA_Request);
					// System.out.println(POA_Request);
					String RufundResponse = cp.receiveResponseFromAESDK();
					// System.out.println(RufundResponse);
					Response_Parameters POA_Response = new Response_Parameters(RufundResponse); // IMP
																								// for
					List<String> POAData = POA_Response.print_Response("POA", parameters);
					POAData.add(3, "POA");
					excelWriter.writeDataRefundOfSale(POAData);
					cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
				}
			}
		} finally {
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			Thread.sleep(5000);
			excelWriter.saveExcelFile(Utils.setFileName("PLCC Transactions"));

		}
	}

	@Test(invocationCount = 1, priority = 2)
	public void test_ALUwith_Key_POA_Cash() throws Exception, IOException, InterruptedException, JDOMException {
		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = A_L_Key_Request_Modification.modified_AL_Key_Request("69");
			cp.sendRequestToAESDK(req);
			 System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);

			List<String> GCBXLData = GCBPrameter.print_Response("Account LookUp Key ", parameters);   
			GCB_Parameters.add(3, "TransType");
			excelWriter.WriteGCBData( GCBXLData, "ALU key");
			String result = GCBPrameter.getParameterValue("ResponseText");
			String CITOken = GCBPrameter.getParameterValue("CardIdentifier");
			// String CRMTOken = GCB_Response_Parameters.getParameterValue("ResponseText");

			if (result.equalsIgnoreCase("APPROVAL")) {
				String POA_Request = POA_RequestModification.modified_POA_Request("53", null, CITOken, "CA");
				cp.sendRequestToAESDK(POA_Request);
				// System.out.println(POA_Request);
				String POAResponse = cp.receiveResponseFromAESDK();
				// System.out.println(RufundResponse);
				Response_Parameters POA_Response = new Response_Parameters(POAResponse); // IMP
				// for
				List<String> POAData = POA_Response.print_Response("POA", parameters);
				POAData.add(3, "POA");
				excelWriter.writeDataRefundOfSale(POAData);
				cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			}
		} finally {
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			Thread.sleep(5000);
			excelWriter.saveExcelFile(Utils.setFileName("PLCC Transactions"));

		}
	}

	@Test(invocationCount = 1, priority = 1)
	public void test_ALUwith_Key_POA_Check() throws Exception, IOException, InterruptedException, JDOMException {
		try {

			BaseClass cp = new BaseClass();

			// GCB Started

			String req = A_L_Key_Request_Modification.modified_AL_Key_Request("69");
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);

			String Check_Sale_Request = CheckRequestModification.modified_Check_Request();

			List<String> GCBXLData = GCBPrameter.print_Response("Account LookUp Key ", parameters);
			GCB_Parameters.add(3, "TransType");
			excelWriter.WriteGCBData( GCBXLData, "ALU key");
			String result = GCBPrameter.getParameterValue("ResponseText");
			String CITOken = GCBPrameter.getParameterValue("CardIdentifier");
			// String CRMTOken = GCB_Response_Parameters.getParameterValue("ResponseText");

			if (!result.equalsIgnoreCase("APPROVAL")) {

				// GCBPrameter.print_GCB_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Check_Sale_Request);
				System.out.println(Check_Sale_Request);
				String sale_Respose = cp.receiveResponseFromAESDK();
				 System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				// System.out.println(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" Check Sale  : ", parameters);   

				saleData.add(3, "Check Sale");

				excelWriter.writeDataRefundOfSale(saleData);

				String cardType = saleResponse.getParameterValue("CardType");
				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String CheckClearingStatus = saleResponse.getParameterValue("CheckClearingStatus");
				if (CheckClearingStatus.equalsIgnoreCase("1")) {
					String signRequest = Signature_Request_Modification.modified_Sign_Request(transactionIdentifier);

					cp.sendRequestToAESDK(signRequest);
					 System.out.println(signRequest);
					String SignResponse = cp.receiveResponseFromAESDK();
					Response_Parameters signRes = new Response_Parameters(SignResponse);
					String signResText = signRes.getParameterValue("ResponseText");
					if (signResText.equalsIgnoreCase("APPROVED")) {
						String POA_Request = POA_RequestModification.modified_POA_Request("53", AurusPayTicketNum,
								CITOken, "CH");
						cp.sendRequestToAESDK(POA_Request);
						 System.out.println(POA_Request);
						String POAResponse = cp.receiveResponseFromAESDK();
						// System.out.println(RufundResponse);
						Response_Parameters POA_Response = new Response_Parameters(POAResponse); // IMP
																									// for
						List<String> POAData = POA_Response.print_Response("POA", parameters);
						POAData.add(3, "POA");
						excelWriter.writeDataRefundOfSale(POAData);
						cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
					} else
						cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
				}

			}

		} finally {
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			Thread.sleep(5000);
			excelWriter.saveExcelFile(Utils.setFileName("PLCC Transactions"));   

		}
	}

}
