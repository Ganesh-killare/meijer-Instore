package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.testng.Assert;

import requestbuilder.AccountLookUp;
import requestbuilder.BalanceEnquiry;
import requestbuilder.ByPass;
import requestbuilder.CD_Sale_Request;
import requestbuilder.Check;
import requestbuilder.EBTRequest;
import requestbuilder.EwicSaleRequest;
import requestbuilder.FSARequest;
import requestbuilder.GCBRequest;
import requestbuilder.IncommIQTransRequest;
import requestbuilder.PLC_Request;
import requestbuilder.PaymentOnAccount;
import requestbuilder.ReturnRequest;
import requestbuilder.Signature;
import requestbuilder.SoloTronRequest;
import responsevalidator.Response_Parameters;
import utilities.DateUtilities;
import utilities.GiftDataXLwriting;
import utilities.LoggerUtil;
import utilities.TransactionXL;
import xmlrequestbuilder.Close_Transaction;

public class BaseClass {

	private String serverAddress = getHostIP();
	// private String serverAddress = "10.190.10.169";

	private int serverPort = 8060;
	// private int serverPort = 15583;
	PrintWriter out = null;
	Socket socket = null;

	private static String getHostIP() {
		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
		String ipAddress = localhost.getHostAddress();
		return ipAddress;
	}

	public void sendRequestToAESDK(String data) throws UnknownHostException, IOException, InterruptedException {
		// System.out.println(data);

		socket = new Socket(serverAddress, serverPort);
		OutputStream outputStream = socket.getOutputStream();
		out = new PrintWriter(outputStream, true);

		out.println(data);
		LoggerUtil.logRequest(data);

		// Thread.sleep(500);
	}

	public String receiveResponseFromAESDK() throws IOException, InterruptedException, JDOMException {
		InputStream inputStream = socket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

		String response = in.readLine();
		LoggerUtil.logResponse(response);

		// inputStream.close();

		try {

			// Parse the XML string
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(new StringReader(response));

			// Create a custom format for pretty printing
			Format format = Format.getPrettyFormat();

			// Create an XMLOutputter with the custom format
			XMLOutputter xmlOutput = new XMLOutputter(format);

			// Convert the document to a string with pretty formatting
			StringWriter stringWriter = new StringWriter();
			xmlOutput.output(document, stringWriter);

			return stringWriter.toString();
		} catch (Exception e) {
			System.err.println("Error parsing XML: " + e.getMessage());
			return response;
		}

	}

	// Perform CREDIT or DEBIT sale

	protected TransactionXL excelWriter = new TransactionXL();
	protected GiftDataXLwriting exceldata = new GiftDataXLwriting();
	public String gcbApprovedText = "Approved";
	public String ApprovedText = "Approval";
	public String fileName = null;

	public static String[] parameters = { "CardToken", "CardIdentifier", "CRMToken", "CardEntryMode",
			"TransactionTypeCode", "TransactionSequenceNumber", "CardType", "SubCardType", "TotalApprovedAmount",
			"ResponseText", "ResponseCode", "TransactionIdentifier", "AurusPayTicketNum", "ApprovalCode",
			"ProcessorMerchantId", "CardExpiryDate", "STAN", "ProcessorResponseCode" };

	public static List<String> ResponseParameter = new ArrayList<>(Arrays.asList(parameters));

	public List<String> performGetCardBin() throws Exception, IOException {

		List<String> gcbResults = new ArrayList<String>();
		List<String> gcbresult = null;
		POS_APIs pa = new POS_APIs();

		try {

			 pa.beforeGetCardBinAPIs();

			String req = GCBRequest.GCB_REQUEST().trim();
			sendRequestToAESDK(req);
			// System.out.println(req);
			String res = receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);

			gcbResults.add(GCBPrameter.getParameterValue("ResponseText"));
			gcbResults.add(GCBPrameter.getParameterValue("CardToken"));
			gcbResults.add(GCBPrameter.getParameterValue("CardIdentifier"));
			gcbResults.add(GCBPrameter.getParameterValue("CRMToken"));
			gcbResults.add(GCBPrameter.getParameterValue("CardType"));

			List<String> GCBXLData = GCBPrameter.print_Response("GCB", parameters);
			excelWriter.WriteGCBData(GCBXLData, "GCB");
			gcbresult = DateUtilities.selectToken(gcbResults);

			if (gcbResults.get(0).equalsIgnoreCase(gcbApprovedText)) {
				Thread.sleep(1000);

				 pa.performed();

			} else {
				System.out.println("Get Card Bin Request denied!");
				Thread.sleep(5000);
			}

		} catch (Exception e) {
			System.out.println("We are not able to perform Get Card Request successfully  \n" + e);
		}

		return gcbresult;

	}

	public List<String> performGetCardBinAllowKeyed() throws Exception, IOException {

		List<String> gcbResults = new ArrayList<String>();
		List<String> gcbresult = null;
		POS_APIs pa = new POS_APIs();

		try {

		//	pa.beforeGetCardBinAPIs();

			String req = GCBRequest.GCB_REQUEST("Y").trim();
			sendRequestToAESDK(req);
			// System.out.println(req);
			String res = receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters GCBPrameter = new Response_Parameters(res);

			gcbResults.add(GCBPrameter.getParameterValue("ResponseText"));
			gcbResults.add(GCBPrameter.getParameterValue("CardToken"));
			gcbResults.add(GCBPrameter.getParameterValue("CardIdentifier"));
			gcbResults.add(GCBPrameter.getParameterValue("CRMToken"));
			gcbResults.add(GCBPrameter.getParameterValue("CardType"));

			List<String> GCBXLData = GCBPrameter.print_Response("GCB", parameters);
			excelWriter.WriteGCBData(GCBXLData, "GCB");
			gcbresult = DateUtilities.selectToken(gcbResults);

			if (gcbResults.get(0).equalsIgnoreCase(gcbApprovedText)) {

				// pa.performed();

			} else {
				System.out.println("Get Card Bin Request denied!");
				Thread.sleep(5000);
			}

		} catch (Exception e) {
			System.out.println("We are not able to perform Get Card Request successfully  \n" + e);
		}

		return gcbresult;

	}

	public List<String> performCreditDebitSale() throws Exception, IOException, InterruptedException {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {

				String Sale_Request = CD_Sale_Request.CD_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2),
						gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);

				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String CardType = saleResponse.getParameterValue("CardType");

				saleResult.add(responseText);
				saleResult.add(transactionIdentifier);
				saleResult.add(AurusPayTicketNum);
				saleResult.add(Amount);
				saleResult.add(CardType);

			}
		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

	// perform Refund without sale

	public List<String> performCreditDebit_RW_Sale() throws Exception, IOException, InterruptedException {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String Sale_Request = CD_Sale_Request.CD_RW_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2),
						gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" RefundWithOutSale  : ", parameters);
				saleData.add(3, "RefundWithOutSale");
				excelWriter.writeDataRefundOfSale(saleData);

				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String CardType = saleResponse.getParameterValue("CardType");

				saleResult.add(responseText);
				saleResult.add(transactionIdentifier);
				saleResult.add(AurusPayTicketNum);
				saleResult.add(Amount);
				saleResult.add(CardType);

			}
		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

	public void performRefundTransaction(List<String> SaleResult) throws Exception, IOException, InterruptedException {

		String returnRequest = null;
		try {
			if (SaleResult.get(4).contains("EB")) {
				System.out.println("EBT Refund");
				returnRequest = ReturnRequest.EBT_REFUND_REQUEST(SaleResult.get(1), SaleResult.get(2),
						SaleResult.get(3));

			} else {
				returnRequest = ReturnRequest.REFUND_REQUEST(SaleResult.get(1), SaleResult.get(2), SaleResult.get(3));

			}

			// System.out.println(returnRequest);

			sendRequestToAESDK(returnRequest);
			String refundResponse = receiveResponseFromAESDK();

			Response_Parameters RefundResponse = new Response_Parameters(refundResponse); // IMP

			List<String> RefundData = RefundResponse.print_Response("Refund", parameters);
			RefundData.add(3, "Refund");
			excelWriter.writeDataRefundOfSale(RefundData);
		} catch (Exception e) {
			System.out.println("We are not able to performed refund transaction");
		}
	}

	public void performVoidTransaction(List<String> SaleResult) throws Exception, IOException, InterruptedException {

		try {

			String returnRequest = ReturnRequest.VOID_REQUEST(SaleResult.get(1), SaleResult.get(2), SaleResult.get(3));
			sendRequestToAESDK(returnRequest);
			String VoidResponse = receiveResponseFromAESDK();

			Response_Parameters voidresponse = new Response_Parameters(VoidResponse); // IMP

			List<String> VoidData = voidresponse.print_Response("Void", parameters);
			VoidData.add(3, "Void");
			excelWriter.writeDataRefundOfSale(VoidData);
		} catch (Exception e) {
			System.out.println("We are not able to performed void transaction");
		}

	}

	// PLC sale transaction

	public List<String> performPLCSale() throws Exception, IOException, InterruptedException {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String Sale_Request = PLC_Request.PLC_SaleRequest(gcbResult.get(1), gcbResult.get(2), gcbResult.get(3));
				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);

				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String CardType = saleResponse.getParameterValue("CardType");

				saleResult.add(responseText);
				saleResult.add(transactionIdentifier);
				saleResult.add(AurusPayTicketNum);
				saleResult.add(Amount);
				saleResult.add(CardType);

			}
		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

	// PLC Refund without sale transaction

	public List<String> performPLC_RWSale() throws Exception, IOException, InterruptedException {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String Sale_Request = PLC_Request.PLC_RW_SaleRequest(gcbResult.get(1), gcbResult.get(2),
						gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);

				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String CardType = saleResponse.getParameterValue("CardType");

				saleResult.add(responseText);
				saleResult.add(transactionIdentifier);
				saleResult.add(AurusPayTicketNum);
				saleResult.add(Amount);
				saleResult.add(CardType);

			}
		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

	// FSA Sale transaction

	public List<String> performFSASale() throws Exception, IOException, InterruptedException {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBinAllowKeyed();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String Sale_Request = FSARequest.FSA_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2), gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);

				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String CardType = saleResponse.getParameterValue("CardType");

				saleResult.add(responseText);
				saleResult.add(transactionIdentifier);
				saleResult.add(AurusPayTicketNum);
				saleResult.add(Amount);
				saleResult.add(CardType);

			}
		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

	// FSA Refund without Sale transaction

	public List<String> performFSA_RW_Sale() throws Exception, IOException, InterruptedException {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBinAllowKeyed();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String Sale_Request = FSARequest.FSA_RW_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2),
						gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);

				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");

				String CardType = saleResponse.getParameterValue("CardType");

				saleResult.add(responseText);
				saleResult.add(transactionIdentifier);
				saleResult.add(AurusPayTicketNum);
				saleResult.add(Amount);
				saleResult.add(CardType);

			}
		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

	// EBT sale transaction

	public List<String> performEBTSale() throws Exception, IOException, InterruptedException {

		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String Sale_Request = EBTRequest.EBT_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2), gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				// System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);

				String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String CardType = saleResponse.getParameterValue("CardType");

				saleResult.add(responseText);
				saleResult.add(transactionIdentifier);
				saleResult.add(AurusPayTicketNum);
				saleResult.add(Amount);
				saleResult.add(CardType);

			}
		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

	// EBT sale transaction

	public List<String> performEBT_RW_Sale() throws Exception, IOException, InterruptedException {

		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String Sale_Request = EBTRequest.EBT_RW_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2),
						gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
				saleData.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleData);

				// String transactionIdentifier =
				// saleResponse.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String responseText = saleResponse.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
				String Amount = saleResponse.getParameterValue("TransactionAmount");
				String CardType = saleResponse.getParameterValue("CardType");

				saleResult.add(responseText);
				saleResult.add(null);
				saleResult.add(AurusPayTicketNum);
				saleResult.add(Amount);
				saleResult.add(CardType);

			}
		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

	// E-wic sale

	public List<String> perform_eWICSale() throws Exception, IOException, InterruptedException {

		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String balance_Request = BalanceEnquiry.EWIC_BALANCEINQUIRY_REQUEST(gcbResult.get(1), gcbResult.get(2),
						gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(balance_Request);
				// System.out.println(Sale_Request);
				String balance_Respose = receiveResponseFromAESDK();

				Response_Parameters BResponse = new Response_Parameters(balance_Respose);
				List<String> BalData = BResponse.print_Response(" Balance Response : ", parameters);
				BalData.add(3, "Balance Response ");
				excelWriter.writeDataRefundOfSale(BalData);
				String responseText = BResponse.getParameterValue("ResponseText");

				if (responseText.equalsIgnoreCase(ApprovedText)) {

					String Sale_Request = EwicSaleRequest.EWIC_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2),
							gcbResult.get(1));

					sendRequestToAESDK(Sale_Request);
					// System.out.println(Sale_Request);
					String sale_Respose = receiveResponseFromAESDK();
					Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
					List<String> saleData = saleResponse.print_Response(" Sale  : ", parameters);
					saleData.add(3, "Sale");
					excelWriter.writeDataRefundOfSale(saleData);

					String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
					// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
					String SresponseText = saleResponse.getParameterValue("ResponseText");
					// Assert.assertEquals(responseText, "APPROVAL");
					String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
					String Amount = saleResponse.getParameterValue("TransactionAmount");
					String CardType = saleResponse.getParameterValue("CardType");

					saleResult.add(SresponseText);
					saleResult.add(transactionIdentifier);
					saleResult.add(AurusPayTicketNum);
					saleResult.add(Amount);
					saleResult.add(CardType);

				}

			}
		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

	public List<String> IncommTransaction() throws Exception, Exception {
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String balance_Request = IncommIQTransRequest.Request(gcbResult.get(1), "82"); // IQ
				balance_Request.trim();
				// Sale Satrted
				// System.out.println(balance_Request);
				sendRequestToAESDK(balance_Request);

				String balance_Respose = receiveResponseFromAESDK();
				// System.out.println(balance_Respose);

				Response_Parameters BResponse = new Response_Parameters(balance_Respose);
				List<String> BalData = BResponse.print_Response(" Iteam Qulification : ", parameters);
				BalData.add(3, "Iteam Qulification ");
				excelWriter.writeDataRefundOfSale(BalData);

			}

			List<String> gcbResults = performGetCardBin();

			String Balance_LookUp = IncommIQTransRequest.Request(gcbResults.get(1), "12"); // Balance LookUp
			Balance_LookUp.trim();
			sendRequestToAESDK(Balance_LookUp);
			// System.out.println(Balance_LookUp);
			String BLookUp_Respose = receiveResponseFromAESDK();
			// System.out.println(BLookUp_Respose);
			Response_Parameters saleResponse = new Response_Parameters(BLookUp_Respose);
			List<String> saleData = saleResponse.print_Response(" BLookUp_Respose  : ", parameters);
			saleData.add(3, "BLookUp_Respose");
			excelWriter.writeDataRefundOfSale(saleData);

			// String SresponseText = saleResponse.getParameterValue("ResponseText");

			List<String> gcbResult2 = performGetCardBin();
			String LimitedSpend = IncommIQTransRequest.Request(gcbResult2.get(1), "01"); // Limited Spend
			LimitedSpend.trim();
			sendRequestToAESDK(LimitedSpend);
			// System.out.println(Sale_Request);
			String LS_Respose = receiveResponseFromAESDK();
			Response_Parameters LS = new Response_Parameters(LS_Respose);
			List<String> LS_DATA = LS.print_Response(" Limited Spend  : ", parameters);
			LS_DATA.add(3, "Limited Spend");
			excelWriter.writeDataRefundOfSale(LS_DATA);

			String lstransactionIdentifier = LS.getParameterValue("TransactionIdentifier");
			// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
			String lsSresponseText = LS.getParameterValue("ResponseText");
			// Assert.assertEquals(responseText, "APPROVAL");
			String lsAurusPayTicketNum = LS.getParameterValue("AurusPayTicketNum");
			String lsAmount = LS.getParameterValue("TransactionAmount");

			saleResult.add(lsSresponseText);
			saleResult.add(lstransactionIdentifier);
			saleResult.add(lsAurusPayTicketNum);
			saleResult.add(lsAmount);

		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}
		return saleResult;

	}

	public List<String> SlutronTransactions() throws Exception, Exception {
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {

				String Balance_LookUp = SoloTronRequest.Request(gcbResult.get(1), "12"); // Balance LookUp
				Balance_LookUp.trim();
				sendRequestToAESDK(Balance_LookUp);
				// System.out.println(Balance_LookUp);
				String BLookUp_Respose = receiveResponseFromAESDK();
				// System.out.println(BLookUp_Respose);
				Response_Parameters saleResponse = new Response_Parameters(BLookUp_Respose);
				List<String> saleData = saleResponse.print_Response("Balance LookUp  : ", parameters);
				saleData.add(3, "Balance LookUp");
				excelWriter.writeDataRefundOfSale(saleData);

				List<String> gcbResult1 = performGetCardBin();
				String LimitedSpend = SoloTronRequest.Request(gcbResult1.get(1), "01"); // Sale
				LimitedSpend.trim();
				// System.out.println(LimitedSpend);
				sendRequestToAESDK(LimitedSpend);
				// System.out.println(Sale_Request);
				String LS_Respose = receiveResponseFromAESDK();
				Response_Parameters LS = new Response_Parameters(LS_Respose);
				List<String> LS_DATA = LS.print_Response(" Sale : ", parameters);
				LS_DATA.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(LS_DATA);

				String lstransactionIdentifier = LS.getParameterValue("TransactionIdentifier");
				// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
				String lsSresponseText = LS.getParameterValue("ResponseText");
				// Assert.assertEquals(responseText, "APPROVAL");
				String lsAurusPayTicketNum = LS.getParameterValue("AurusPayTicketNum");
				String lsAmount = LS.getParameterValue("TransactionAmount");

				saleResult.add(lsSresponseText);
				saleResult.add(lstransactionIdentifier);
				saleResult.add(lsAurusPayTicketNum);
				saleResult.add(lsAmount);

			}

		} finally

		{
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}
		return saleResult;

	}

	public List<String> performALUwithSSN() {
		List<String> ALUResult = new ArrayList<String>();
		POS_APIs pa = new POS_APIs();

		try {

			// pa.beforeGetCardBinAPIs();

			String req = AccountLookUp.withSSNandPhoneNumber();
			sendRequestToAESDK(req);
			// System.out.println(req);
			String res = receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters ALUParameter = new Response_Parameters(res);

			ALUResult.add(ALUParameter.getParameterValue("ResponseText"));
			ALUResult.add(null);
			ALUResult.add(ALUParameter.getParameterValue("CardIdentifier"));
			ALUResult.add(null);
			ALUResult.add(ALUParameter.getParameterValue("CardType"));

			List<String> GCBXLData = ALUParameter.print_Response("ALU", parameters);
			excelWriter.WriteGCBData(GCBXLData, "ALU");

			if (ALUResult.get(0).equalsIgnoreCase(gcbApprovedText)) {

				// pa.performed();

			} else {
				System.out.println("Account LookUp Request denied!");
				// Assert.fail();
			}

		} catch (Exception e) {
			System.out.println("We are not able to perform Account LookUp Request successfully  \n" + e);
		}

		return ALUResult;

	}

	public List<String> performALUwithKey() {
		List<String> ALUResult = new ArrayList<String>();
		POS_APIs pa = new POS_APIs();

		try {

			// pa.beforeGetCardBinAPIs();

			String req = AccountLookUp.withProcessorToken();
			sendRequestToAESDK(req);
			// System.out.println(req);
			String res = receiveResponseFromAESDK();
			// System.out.println(res);
			Response_Parameters ALUParameter = new Response_Parameters(res);

			List<String> GCBXLData = ALUParameter.print_Response("ALU", parameters);
			excelWriter.WriteGCBData(GCBXLData, "ALU");

			//

			ALUResult.add(ALUParameter.getParameterValue("ResponseText"));
			ALUResult.add(null);
			ALUResult.add(ALUParameter.getParameterValue("CardIdentifier"));
			ALUResult.add(null);
			ALUResult.add(ALUParameter.getParameterValue("CardType"));

			if (ALUResult.get(0).equalsIgnoreCase(gcbApprovedText)) {
				int i = 0 ;

				// pa.performed();

			} else {
				System.out.println("Account LookUp Request denied!");
				// Assert.fail();
			}

		} catch (Exception e) {
			System.out.println("We are not able to perform Account LookUp Request successfully  \n" + e);
		}

		return ALUResult;

	}

	public void performPOA( String AMT, String AuruspayTicketNumber, String cardIdentifier, String paymentMethod)
			throws Exception, IOException, InterruptedException {

		try {

			String POARequest = PaymentOnAccount.Request(AMT, AuruspayTicketNumber, cardIdentifier, paymentMethod);

			sendRequestToAESDK(POARequest);
		//	System.out.println(POARequest);
			String POARes = receiveResponseFromAESDK();
			// System.out.println(sale_Respose);
			Response_Parameters saleResponse = new Response_Parameters(POARes);
			List<String> saleData = saleResponse.print_Response(" POA  : ", parameters);
			saleData.add(3, "POA");
			excelWriter.writeDataRefundOfSale(saleData);

			String responseText = saleResponse.getParameterValue("ResponseText");
			Assert.assertEquals( responseText, "APPROVAL");

		} finally {
			sendRequestToAESDK(ByPass.Option1());   
			receiveResponseFromAESDK();
		

		}

	}

	public List<String> performCheckTransaction() throws IOException, Exception {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		try {

			String Sale_Request = Check.Request();

			// Sale Satrted

			sendRequestToAESDK(Sale_Request);
			// System.out.println(Sale_Request);
			String sale_Respose = receiveResponseFromAESDK();
			Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
			List<String> saleData = saleResponse.print_Response(" CHECK Sale  : ", parameters);
			saleData.add(3, "CHECK Sale");
			excelWriter.writeDataRefundOfSale(saleData);

			String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
			// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");
			String responseText = saleResponse.getParameterValue("ResponseText");
			// Assert.assertEquals(responseText, "APPROVAL");
			String AurusPayTicketNum = saleResponse.getParameterValue("AurusPayTicketNum");
			String Amount = saleResponse.getParameterValue("TransactionAmount");
			String CardType = saleResponse.getParameterValue("CardType");

			String CheckStatus = saleResponse.getParameterValue("CheckClearingStatus");
			if (CheckStatus.equalsIgnoreCase("1")) {

				String signRequest = Signature.Request(transactionIdentifier);
				sendRequestToAESDK(signRequest);
				// System.out.println(signRequest);
				String SignResponse = receiveResponseFromAESDK();
				Response_Parameters signRes = new Response_Parameters(SignResponse);
				String signResText = signRes.getParameterValue("ResponseText");
				Assert.assertEquals("APPROVED", signResText);

			}

			saleResult.add(responseText);
			saleResult.add(transactionIdentifier);
			saleResult.add(AurusPayTicketNum);
			saleResult.add(Amount);
			saleResult.add(CardType);

		} finally {
			sendRequestToAESDK(ByPass.Option1());
			receiveResponseFromAESDK();
			sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
			receiveResponseFromAESDK();

		}

		return saleResult;
	}

}
