package testcases;

import java.io.IOException;

import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import base.ConnectionWithPort;
import request_response_modification.Close_Transaction;
import request_response_modification.GCB_Modification;
import request_response_modification.Sale_Request_Modification;
import request_response_modification.Void_Request_Modification;
import responsemodification.GCB_Response_Parameters;
import responsemodification.Sale_Response_Parameters;
import responsemodification.Void_Response_Parameters;

public class Void_Of_Sale {

	ConnectionWithPort cp = new ConnectionWithPort();
	Faker faker = new Faker();

	@Test(invocationCount = 10)
	public void testVoidOfSale() throws Exception, IOException {
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
					.modified_Sale_Request(GCB_Response_Parameters.getElementValue("CardToken"), null, null,"01");

			String[] parameters = { "CardToken", "CardIdentifier", "CRMToken", "CardEntryMode", "TransactionTypeCode",
					"TransactionSequenceNumber", "CardType", "SubCardType", "TotalApprovedAmount", "ResponseText",
					"ResponseCode", "TransactionIdentifier", "AurusPayTicketNum", "ApprovalCode" };
			GCB_Response_Parameters.print_GCB_Response(parameters);
			String result = GCB_Response_Parameters.getElementValue("ResponseText");

			if (result.equalsIgnoreCase("Approved")) {

				// GCBPrameter.print_GCB_Response(GCB_Response);

				// Sale Satrted

				cp.sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = cp.receiveResponseToAESDK();
				// System.out.println(sale_Respose);
				Sale_Response_Parameters saleResponse = new Sale_Response_Parameters(sale_Respose);
				Sale_Response_Parameters.print_Sale_Response("Sale  :", parameters);
				String cardType = Sale_Response_Parameters.getElementValue("CardType");
				// Assert.assertEquals(cardType, "MCD");
				String transactionIdentifier = Sale_Response_Parameters.getElementValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = Sale_Response_Parameters.getElementValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = Sale_Response_Parameters.getElementValue("AurusPayTicketNum");
				String Amount = Sale_Response_Parameters.getElementValue("TransactionAmount");

				if (responseText.equalsIgnoreCase("APPROVAL")) {
					String Void_Request = Void_Request_Modification.modified_Void_Request( "06" ,Amount, AurusPayTicketNum,
							transactionIdentifier);
					cp.sendRequestToAESDK(Void_Request);

					String VoidResponse = cp.receiveResponseToAESDK();

					Void_Response_Parameters voidParameters = new Void_Response_Parameters(VoidResponse); // IMP for
																											// print
																											// response

					Void_Response_Parameters.print_Void_Response(parameters);
					cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
				} else
					cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			}
		} finally {
			cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			Thread.sleep(5000);
		}
	}

}
