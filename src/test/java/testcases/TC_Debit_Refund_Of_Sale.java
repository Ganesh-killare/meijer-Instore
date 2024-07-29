package testcases;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.JDOMException;
import org.testng.annotations.Test;

import base.ConnectionWithPort;
import request_response_modification.Close_Transaction;
import request_response_modification.GCB_Modification;
import request_response_modification.Refund_Request_Modification;
import request_response_modification.Sale_Request_Modification;
import responsemodification.GCB_Response_Parameters;
import responsemodification.Refund_Response_Parameters;
import responsemodification.Sale_Response_Parameters;
import utilities.RefundOfSaleXLWrite;

public class TC_Debit_Refund_Of_Sale extends ConnectionWithPort {

	ConnectionWithPort cp = new ConnectionWithPort();
	RefundOfSaleXLWrite excelWriter = new RefundOfSaleXLWrite();

	String[] parameters = { "CardToken", "CardIdentifier", "CRMToken", "CardEntryMode", "TransactionTypeCode",
			"TransactionSequenceNumber", "CardType", "SubCardType", "TotalApprovedAmount", "ResponseText",
			"ResponseCode", "TransactionIdentifier", "AurusPayTicketNum", "ApprovalCode" };

	@Test(invocationCount = 30)
	public void test_Debit_Refund_Of_Sale() throws Exception, IOException, InterruptedException, JDOMException {
		try {

			ConnectionWithPort cp = new ConnectionWithPort();

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseToAESDK();
			// System.out.println(res);
			GCB_Response_Parameters GCBPrameter = new GCB_Response_Parameters(res);

			String Sale_Request = Sale_Request_Modification
					.modified_Sale_Request(GCB_Response_Parameters.getElementValue("CardToken"), null, null, "01");
			// String Sale_Request = Sale_Request_Modification.modified_Sale_Request(
			// null,GCB_Response_Parameters.getElementValue("CardIdentifier"), null);
			// String Sale_Request =
			// Sale_Request_Modification.modified_Sale_Request(GCB_Response_Parameters.getElementValue("CardToken"),
			// null, null);

			List<String> GCBXLData = GCB_Response_Parameters.print_GCB_Response(parameters);
			excelWriter.WriteGCBData(GCBXLData);
			String result = GCB_Response_Parameters.getElementValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				// GCBPrameter.print_GCB_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseToAESDK();
				// System.out.println(sale_Respose);
				Sale_Response_Parameters saleResponse = new Sale_Response_Parameters(sale_Respose);
				List<String> saleData = Sale_Response_Parameters.print_Sale_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);

				String cardType = Sale_Response_Parameters.getElementValue("CardType");
				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = Sale_Response_Parameters.getElementValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = Sale_Response_Parameters.getElementValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = Sale_Response_Parameters.getElementValue("AurusPayTicketNum");
				String Amount = Sale_Response_Parameters.getElementValue("TransactionAmount");

				if (responseText.equalsIgnoreCase("APPROVAL")) {
					String Refund_Request = Refund_Request_Modification.modified_Refund_Request("02", Amount,
							AurusPayTicketNum, transactionIdentifier);
					cp.sendRequestToAESDK(Refund_Request);
					// System.out.println(Refund_Request);
					String RufundResponse = cp.receiveResponseToAESDK();
					// System.out.println(RufundResponse);
					Refund_Response_Parameters RefundResponse = new Refund_Response_Parameters(RufundResponse); // IMP
																												// for
					List<String> RefundData = Refund_Response_Parameters.print_Refund_Response(parameters, "Refund");
					RefundData.add(3, "Refund");
					excelWriter.writeDataRefundOfSale(RefundData);
					cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
				} else
					cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			}
		} finally {
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			Thread.sleep(5000);
			excelWriter.saveExcelFile();

		}
	}

	@Test(invocationCount = 30)
	public void test_Void_Of_Sale() throws Exception, IOException, InterruptedException, JDOMException {
		try {

		ConnectionWithPort cp = new ConnectionWithPort();

			// GCB Started

			String req = GCB_Modification.GCB_Request_Modified();
			cp.sendRequestToAESDK(req);
			// System.out.println(req);
			String res = cp.receiveResponseToAESDK();
			// System.out.println(res);
			GCB_Response_Parameters GCBPrameter = new GCB_Response_Parameters(res);

			 String Sale_Request =
		 Sale_Request_Modification.modified_Sale_Request(GCB_Response_Parameters.getElementValue("CardToken"),
			 null, null , "01");   
			 
			 /*		 String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null,
			 GCB_Response_Parameters.getElementValue("CardIdentifier"), null);
			String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, null,
					GCB_Response_Parameters.getElementValue("CRMToken"));								*/

			List<String> GCBXLData = GCB_Response_Parameters.print_GCB_Response(parameters);
			excelWriter.WriteGCBData(GCBXLData);
			String result = GCB_Response_Parameters.getElementValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				// GCBPrameter.print_GCB_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseToAESDK();
				// System.out.println(sale_Respose);
				Sale_Response_Parameters saleResponse = new Sale_Response_Parameters(sale_Respose);
				List<String> saleData = Sale_Response_Parameters.print_Sale_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);
				String cardType = Sale_Response_Parameters.getElementValue("CardType");
				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = Sale_Response_Parameters.getElementValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = Sale_Response_Parameters.getElementValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = Sale_Response_Parameters.getElementValue("AurusPayTicketNum");
				String Amount = Sale_Response_Parameters.getElementValue("TransactionAmount");

				if (responseText.equalsIgnoreCase("APPROVAL")) {
					String VoidRequest = Refund_Request_Modification.modified_Refund_Request("06", Amount,
							AurusPayTicketNum, transactionIdentifier);
					cp.sendRequestToAESDK(VoidRequest);
					// System.out.println(Refund_Request);
					String RufundResponse = cp.receiveResponseToAESDK();
					// System.out.println(RufundResponse);
					Refund_Response_Parameters RefundResponse = new Refund_Response_Parameters(RufundResponse); // IMP
					// for
					List<String> VoidData = Refund_Response_Parameters.print_Refund_Response(parameters, "VOID");
					VoidData.add(3, "Void");
					excelWriter.writeDataRefundOfSale(VoidData);
					cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
				} else
					cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());   
			}
		} finally {
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			Thread.sleep(5000);
			excelWriter.saveExcelFile();

		}
	}

	@Test
	public void test_Refund_Without_Sale() throws Exception {
		try {

			ConnectionWithPort cp = new ConnectionWithPort();

				// GCB Started

				String req = GCB_Modification.GCB_Request_Modified();
				cp.sendRequestToAESDK(req);
				// System.out.println(req);
				String res = cp.receiveResponseToAESDK();
				// System.out.println(res);
				GCB_Response_Parameters GCBPrameter = new GCB_Response_Parameters(res);

				 String Refund_Without_Sale_Request =
			 Sale_Request_Modification.modified_Sale_Request(GCB_Response_Parameters.getElementValue("CardToken"),
				 null, null, "02");   
				 
				 /*		 String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null,
				 GCB_Response_Parameters.getElementValue("CardIdentifier"), null);
				String Sale_Request = Sale_Request_Modification.modified_Sale_Request(null, null,
						GCB_Response_Parameters.getElementValue("CRMToken"));								*/

				List<String> GCBXLData = GCB_Response_Parameters.print_GCB_Response(parameters);
				excelWriter.WriteGCBData(GCBXLData);
				String result = GCB_Response_Parameters.getElementValue("ResponseText");

				if (result.equalsIgnoreCase("Approved")) {

					// GCBPrameter.print_GCB_Response(GCB_Response);

					// Sale Satrted

					cp.sendRequestToAESDK(Refund_Without_Sale_Request);
					// System.out.println(Sale_Request);
					String sale_Respose = cp.receiveResponseToAESDK();
					// System.out.println(sale_Respose);
					Sale_Response_Parameters saleResponse = new Sale_Response_Parameters(sale_Respose);
					List<String> saleData = Sale_Response_Parameters.print_Sale_Response(" Refund_Without_Sale  : ", parameters);
					saleData.add(3, "Refund");
					excelWriter.writeDataRefundOfSale(saleData);
					String cardType = Sale_Response_Parameters.getElementValue("CardType");
					// Assert.assertEquals(cardType, "MCD");
					String transactionIdentifier = Sale_Response_Parameters.getElementValue("TransactionIdentifier");
					// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
					String responseText = Sale_Response_Parameters.getElementValue("ResponseText");
					// Assert.assertEquals(responseText, "APPROVAL");
					String AurusPayTicketNum = Sale_Response_Parameters.getElementValue("AurusPayTicketNum");
					String Amount = Sale_Response_Parameters.getElementValue("TransactionAmount");

				if (responseText.equalsIgnoreCase("APPROVAL")) {
						String VoidRequest = Refund_Request_Modification.modified_Refund_Request("06", Amount,
								AurusPayTicketNum, transactionIdentifier);
						cp.sendRequestToAESDK(VoidRequest);
						// System.out.println(Refund_Request);
						String RufundResponse = cp.receiveResponseToAESDK();
						// System.out.println(RufundResponse);
						Refund_Response_Parameters RefundResponse = new Refund_Response_Parameters(RufundResponse); // IMP
						// for
						List<String> VoidData = Refund_Response_Parameters.print_Refund_Response(parameters, "VOID");
						VoidData.add(3, "Void");
						excelWriter.writeDataRefundOfSale(VoidData);
						cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
					} else
						cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());   
				}
			} finally {
				cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
				Thread.sleep(5000);
				excelWriter.saveExcelFile();

			}
		
		
		
		
	}
}
