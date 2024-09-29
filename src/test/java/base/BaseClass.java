package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import requestbuilder.AccountLookUp;
import requestbuilder.BalanceEnquiry;
import requestbuilder.ByPass;
import requestbuilder.CD_Sale_Request;
import requestbuilder.Check;
import requestbuilder.EBTRequest;
import requestbuilder.EBT_OTC;
import requestbuilder.EwicSaleRequest;
import requestbuilder.FSARequest;
import requestbuilder.Fleet;
import requestbuilder.GCBRequest;
import requestbuilder.IncommIQTransRequest;
import requestbuilder.PLC_Request;
import requestbuilder.PaymentOnAccount;
import requestbuilder.ReturnRequest;
import requestbuilder.ShowList;
import requestbuilder.Signature;
import requestbuilder.SoloTronRequest;
import responsevalidator.Response_Parameters;
import utilities.GiftDataXLwriting;
import utilities.LoggerUtil;
import utilities.TransactionXL;
import utilities.Utils;

public class BaseClass {

	PrintWriter out = null;
	Socket socket = null;

	private String serverAddress = Utils.getServerIp();
	private int serverPort = Utils.getServerPort();;

	@BeforeMethod
	public void incrementSessionId() {
		SessionIdManager.incrementAndGetSessionId();

	}

	public void sendRequestToAESDK(String data) throws UnknownHostException, IOException, InterruptedException { // System.out.println(data);

		socket = new Socket(serverAddress, serverPort);
		OutputStream outputStream = socket.getOutputStream();
		out = new PrintWriter(outputStream, true);
		LoggerUtil.logRequest(data);
		out.println(data);

		// System.out.println(data);

		// Thread.sleep(500);
	}

	public String receiveResponseFromAESDK() throws IOException, InterruptedException, JDOMException {
		InputStream inputStream = socket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

		String response = in.readLine();
		LoggerUtil.logResponse(response);
		// System.out.println(response);
		return response;

	}

	// Perform CREDIT or DEBIT sale

	protected TransactionXL excelWriter = new TransactionXL();
	protected GiftDataXLwriting exceldata = new GiftDataXLwriting();
	public String gcbApprovedText = "Approved";
	public String ApprovedText = "APPROVAL";
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
			gcbresult = Utils.selectToken(gcbResults, gcbResults.get(4));

			if (gcbResults.get(0).equalsIgnoreCase(gcbApprovedText)) {

				/*
				 * sendRequestToAESDK(ShowList.Request()); receiveResponseFromAESDK();
				 * performByPassRequest(2);
				 * 
				 * sendRequestToAESDK(requestbuilder.Signature.Request());
				 * receiveResponseFromAESDK();
				 */

				// performByPassRequest(0);

				// Additional POS APIs

			//	pa.performed();

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

		/*
		 * UAT CI 2000000000003154 CRM 8920000010000048991
		 * 
		 * PROD
		 * 
		 * CI 2000000001581656
		 * 
		 * CRM 8920000010155342454
		 */
		List<String> gcbResults = new ArrayList<>();

		try {
			String tokenType = Utils.getTokenType();

			switch (tokenType) {
			case "CardIdentifier":
				System.out.println("Transaction Performed Using CI : 2000000000003154");

				return Arrays.asList("Approved", null, "2000000000003154", null);

			case "CRMToken":
				System.out.println("Transaction Performed Using CRM : 8920000010000048991 ");

				return Arrays.asList("Approved", null, null, "8920000010000048991");

			default:
				String req = GCBRequest.GCB_REQUEST("Y").trim();
				sendRequestToAESDK(req);
				String res = receiveResponseFromAESDK();
				Response_Parameters GCBPrameter = new Response_Parameters(res);

				gcbResults.add(GCBPrameter.getParameterValue("ResponseText"));
				gcbResults.add(GCBPrameter.getParameterValue("CardToken"));
				gcbResults.add(GCBPrameter.getParameterValue("CardIdentifier"));
				gcbResults.add(GCBPrameter.getParameterValue("CRMToken"));
				gcbResults.add(GCBPrameter.getParameterValue("CardType"));

				List<String> GCBXLData = GCBPrameter.print_Response("GCB", parameters);
				excelWriter.WriteGCBData(GCBXLData, "GCB");

				// Ensure gcbResults is not empty before selecting tokens
				if (gcbResults.isEmpty()) {
					System.out.println("gcbResults is empty. Returning an empty list.");
					return new ArrayList<>();
				}

				return Utils.selectToken(gcbResults, gcbResults.get(4));
			}

		} catch (Exception e) {
			System.out.println("We were not able to perform Get Card Request successfully \n" + e);
		}

		// Return an empty list in case of an exception or no matching case
		return new ArrayList<>();
	}

	public void performCloseRequest() throws Exception, Exception, JDOMException {
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
	}

	public void performByPassRequest(int option) throws Exception, Exception, JDOMException {
		sendRequestToAESDK(ByPass.Option(option));
		receiveResponseFromAESDK();
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
				saleResult = saleResponse.print_Response(" Sale  : ", parameters);
				saleResult.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleResult);
				saleResult.remove(3);

			}
		} finally {

			// 1
			performByPassRequest(1);
			performCloseRequest();

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
				saleResult = saleResponse.print_Response(" RefundWithOutSale  : ", parameters);
				saleResult.add(3, "RefundWithOutSale");
				excelWriter.writeDataRefundOfSale(saleResult);
				saleResult.remove(3);

			}
		} finally {
			performByPassRequest(1);
			performCloseRequest();

		}

		return saleResult;
	}

	public void performRefundTransaction(List<String> saleResult) throws Exception, IOException, InterruptedException {
		// System.out.println( "In the Void Transaction, we used AurusPayTickNum and
		// Transaction ID with a total amount and tender amount of $"+
		// saleResult.get(3));

		Assert.assertNotEquals(saleResult.get(8), "0.00", "Amount is zero ; We are not processes Refund");

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

			List<String> RefundData = RefundResponse.print_Response("Refund", parameters);
			RefundData.add(3, "Refund");
			excelWriter.writeDataRefundOfSale(RefundData);
		} catch (Exception e) {
			System.out.println("We are not able to performed refund transaction");
		}
	}

	public void performVoidTransaction(List<String> saleResult) throws Exception, IOException, InterruptedException {

		// System.out.println("In the Void Transaction, we used AurusPayTickNum and
		// Transaction ID with a total amount and tender amount of $" +
		// SaleResult.get(3));

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
				saleResult = saleResponse.print_Response(" Sale  : ", parameters);
				saleResult.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleResult);

				saleResult.remove(3);
			}
		} finally {
			performByPassRequest(1);
			performCloseRequest();

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
				saleResult = saleResponse.print_Response(" Refund Without Sale  : ", parameters);
				saleResult.add(3, "Refund Without Sale");
				excelWriter.writeDataRefundOfSale(saleResult);

				saleResult.remove(3);

			}
		} finally {
			performByPassRequest(1);
			performCloseRequest();

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
				saleResult = saleResponse.print_Response(" Sale  : ", parameters);
				saleResult.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleResult);
				saleResult.remove(3);
			}
		} finally {
			performByPassRequest(1);
			performCloseRequest();

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
				saleResult = saleResponse.print_Response("Refund without Sale  : ", parameters);
				saleResult.add(3, "Refund without Sale");
				excelWriter.writeDataRefundOfSale(saleResult);
				saleResult.remove(3);
			}
		} finally {
			performByPassRequest(1);
			performCloseRequest();

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
				saleResult = saleResponse.print_Response(" Sale  : ", parameters);
				saleResult.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleResult);
				saleResult.remove(3);

			}
		} finally {
			performByPassRequest(1);
			performCloseRequest();

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
				saleResult = saleResponse.print_Response("Refund Without Sale  : ", parameters);
				saleResult.add(3, "Refund Without Sale");
				excelWriter.writeDataRefundOfSale(saleResult);

				saleResult.remove(3);
			}
		} finally {
			performByPassRequest(1);
			performCloseRequest();

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
					saleResult = saleResponse.print_Response(" Sale  : ", parameters);
					saleResult.add(3, "Sale");
					excelWriter.writeDataRefundOfSale(saleResult);
					saleResult.remove(3);

				}

			}
		} finally {
			performByPassRequest(1);
			performCloseRequest();
		}

		return saleResult;
	}

	public List<String> IncommTransaction() throws Exception, Exception {
		List<String> LS_DATA = new ArrayList<String>();

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
			performCloseRequest();

			List<String> gcbResults = performGetCardBin();

			String Balance_LookUp = IncommIQTransRequest.Request(gcbResults.get(1), "12"); // Balance LookUp
			Balance_LookUp.trim();
			sendRequestToAESDK(Balance_LookUp);
			// System.out.println(Balance_LookUp);
			String BLookUp_Respose = receiveResponseFromAESDK();
			// System.out.println(BLookUp_Respose);
			Response_Parameters saleResponse = new Response_Parameters(BLookUp_Respose);
			List<String> saleResult = saleResponse.print_Response(" BLookUp_Respose  : ", parameters);
			saleResult.add(3, "BLookUp_Respose");
			excelWriter.writeDataRefundOfSale(saleResult);

			// String SresponseText = saleResponse.getParameterValue("ResponseText");

			performCloseRequest();

			List<String> gcbResult2 = performGetCardBin();
			String LimitedSpend = IncommIQTransRequest.Request(gcbResult2.get(1), "01"); // Limited Spend
			LimitedSpend.trim();
			sendRequestToAESDK(LimitedSpend);
			// System.out.println(Sale_Request);
			String LS_Respose = receiveResponseFromAESDK();
			Response_Parameters LS = new Response_Parameters(LS_Respose);
			LS_DATA = LS.print_Response(" Limited Spend  : ", parameters);
			LS_DATA.add(3, "Limited Spend");
			excelWriter.writeDataRefundOfSale(LS_DATA);
			LS_DATA.remove(3);

		} finally {
			performByPassRequest(1);
			performCloseRequest();

		}
		return LS_DATA;

	}

	public List<String> SlutronTransactions() throws Exception, Exception {
		List<String> LS_DATA = new ArrayList<String>();

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
				List<String> saleResult = saleResponse.print_Response("Balance LookUp  : ", parameters);
				saleResult.add(3, "Balance LookUp");
				excelWriter.writeDataRefundOfSale(saleResult);

				performCloseRequest();

				List<String> gcbResult1 = performGetCardBin();
				String LimitedSpend = SoloTronRequest.Request(gcbResult1.get(1), "01"); // Sale
				LimitedSpend.trim();
				// System.out.println(LimitedSpend);
				sendRequestToAESDK(LimitedSpend);
				// System.out.println(Sale_Request);
				String LS_Respose = receiveResponseFromAESDK();
				Response_Parameters LS = new Response_Parameters(LS_Respose);
				LS_DATA = LS.print_Response(" Sale : ", parameters);
				LS_DATA.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(LS_DATA);

				LS_DATA.remove(3);

			}

		} finally

		{
			performByPassRequest(1);
			performCloseRequest();

		}
		return LS_DATA;

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

			ALUResult = ALUParameter.print_Response("ALU", parameters);
			excelWriter.WriteGCBData(ALUResult, "ALU");

			//

			if (ALUResult.get(0).equalsIgnoreCase("APPROVAL")) {

				// pa.performed();

			}

		} catch (Exception e) {
			System.out.println("We are not able to perform Account LookUp Request successfully  \n" + e);
		}

		return ALUResult;

	}

	public void performPOA(String AMT, String AuruspayTicketNumber, String cardIdentifier, String paymentMethod)
			throws Exception, IOException, InterruptedException {

		try {

			String POARequest = PaymentOnAccount.Request(AMT, AuruspayTicketNumber, cardIdentifier, paymentMethod);

			sendRequestToAESDK(POARequest);
			// System.out.println(POARequest);
			String POARes = receiveResponseFromAESDK();
			// System.out.println(sale_Respose);
			Response_Parameters saleResponse = new Response_Parameters(POARes);
			List<String> saleResult = saleResponse.print_Response(" POA  : ", parameters);
			saleResult.add(3, "POA");
			excelWriter.writeDataRefundOfSale(saleResult);

			String responseText = saleResponse.getParameterValue("ResponseText");
			Assert.assertEquals(responseText, "APPROVAL");

		} finally {
			performByPassRequest(1);

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
			saleResult = saleResponse.print_Response(" CHECK Sale  : ", parameters);
			saleResult.add(3, "CHECK Sale");
			excelWriter.writeDataRefundOfSale(saleResult);
			saleResult.remove(3);

			String transactionIdentifier = saleResponse.getParameterValue("TransactionIdentifier");
			// Assert.assertEquals(transactionIdentifier.substring(0, 1), "1");

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

		} finally {
			performByPassRequest(1);
			performCloseRequest();
		}

		return saleResult;
	}

	// EBT OTC

	public List<String> performEBT_OTC_Transaction() throws IOException, Exception {

		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {

				String Sale_Request = EBT_OTC.SALE_REQUEST(gcbResult.get(1), gcbResult.get(2), gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				saleResult = saleResponse.print_Response(" Sale  : ", parameters);
				saleResult.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleResult);
				saleResult.remove(3);

			}
		} finally {

			// 1
			performByPassRequest(1);
			performCloseRequest();

		}
		return saleResult;
	}

	public List<String> performebtOtcRefundWithoutSale() throws IOException, Exception {

		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {

				String Sale_Request = EBT_OTC.RW_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2), gcbResult.get(3));

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				saleResult = saleResponse.print_Response(" Refund Without Sale  : ", parameters);
				saleResult.add(3, "Refund Without Sale ");
				excelWriter.writeDataRefundOfSale(saleResult);

				saleResult.remove(3);
			}
		} finally {

			// 1
			performByPassRequest(1);
			performCloseRequest();

		}
		return saleResult;
	}

	public List<String> performFleetSale() throws Exception, Exception {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		System.out.println(gcbResult);
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {

				String Sale_Request = Fleet.SaleRequest(gcbResult);

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				saleResult = saleResponse.print_Response(" Sale  : ", parameters);
				saleResult.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleResult);

				saleResult.remove(3);

			}
		} finally {

			// 1
			performByPassRequest(1);
			performCloseRequest();

		}
		return saleResult;

	}

	public List<String> performFleetRefundWithoutSale() throws IOException, Exception {

		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {

				String Sale_Request = Fleet.RW_Sale(gcbResult);

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				saleResult = saleResponse.print_Response(" Refund Without Sale  : ", parameters);
				saleResult.add(3, "Refund Without Sale ");
				excelWriter.writeDataRefundOfSale(saleResult);
				saleResult.remove(3);

			}
		} finally {

			// 1
			performByPassRequest(1);
			performCloseRequest();

		}
		return saleResult;
	}
}
