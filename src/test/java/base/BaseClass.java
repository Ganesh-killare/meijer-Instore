package base;

import java.io.BufferedOutputStream;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
import requestbuilder.TicketDisplayAurus;
import requestbuilder.WICRequestBuilder;
import requestbuilder.WorldPayEPP;
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
	public void incrementSessionId()
			throws UnknownHostException, IOException, InterruptedException, ExecutionException {
		SessionIdManager.incrementAndGetSessionId();
		// performTicketDisplay();

	}

	public void sendRequestToAESDK(String data) throws UnknownHostException, IOException, InterruptedException {
		socket = new Socket(serverAddress, serverPort);
		OutputStream outputStream = socket.getOutputStream();

		// Use OutputStream directly if PrintWriter isn't sending data properly
		BufferedOutputStream bufferedOut = new BufferedOutputStream(outputStream);

		LoggerUtil.logRequest(data);

		// Define the chunk size (adjust as needed)
		int chunkSize = 150000; // For example, send 1024 characters at a time
		int dataLength = data.length();
		int i = 0; // Initialize the index

		// Send the data in chunks using a while loop
		while (i < dataLength) {
			int end = Math.min(i + chunkSize, dataLength); // Get the end index for the current chunk
			String chunk = data.substring(i, end);

			// Write each chunk as bytes to avoid any issues with newline characters
			bufferedOut.write(chunk.getBytes());
			bufferedOut.flush(); // Ensure data is sent immediately

			i += chunkSize; // Move the index forward by the chunk size

		}

		// Optionally send an end signal, like a newline or EOF, if the server expects
		// that.
		// bufferedOut.write("\n".getBytes()); // For example, if you need to send a
		// newline to indicate the end of transmission

		bufferedOut.flush(); // Ensure the last chunk is sent

	}

	public String receiveResponseFromAESDK() throws InterruptedException, ExecutionException, IOException {
		// Create a thread pool with a single thread
		ExecutorService executor = Executors.newSingleThreadExecutor();

		// Define the task that reads the response
		Callable<String> task = () -> {
			InputStream inputStream = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			String response = in.readLine();
			LoggerUtil.logResponse(response);
			// System.out.println(response);
			return response;
		};

		// Set the timeout for 190 seconds
		try {
			// Execute the task with a 190-second timeout
			Future<String> future = executor.submit(task);
			return future.get(Utils.getPOSTimeOut(), TimeUnit.SECONDS); // Timeout after 190 seconds
		} catch (TimeoutException e) {
			// Handle timeout
			try {
				performByPassRequest();

			} catch (JDOMException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Timeout reached. No response received within " + Utils.getPOSTimeOut() + " seconds.");
			return null; // Or throw an exception, or handle as needed
		} finally {
			// Shutdown the executor service
			executor.shutdown();
			socket.close();
		}
	}

	// Perform CREDIT or DEBIT sale

	protected TransactionXL excelWriter = new TransactionXL();
	protected GiftDataXLwriting exceldata = new GiftDataXLwriting();
	public String gcbApprovedText = "Approved";
	public String ApprovedText = "APPROVAL";
	public String fileName = null;
	public static List<String> parameters;

	@BeforeClass
	public void baseSetUp() {

		if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
			parameters = Arrays.asList("CardToken", "CardIdentifier", "CRMToken", "CardEntryMode",
					"TransactionTypeCode", "TransactionSequenceNumber", "CardType", "SubCardType",
					"TotalApprovedAmount", "ResponseText", "ResponseCode", "TransactionIdentifier", "AurusPayTicketNum",
					"ApprovalCode", "ProcessorMerchantId", "CardExpiryDate", "STAN", "ProcessorResponseCode", "AID");
		} else {
			parameters = Arrays.asList("CardToken", "CardIdentifier", "CRMToken", "CardEntryMode",
					"TransactionTypeCode", "TransactionSequenceNumber", "CardType", "SubCardType",
					"TotalApprovedAmount", "ResponseText", "ResponseCode", "TransactionIdentifier", "AurusPayTicketNum",
					"ApprovalCode", "ProcessorMerchantId");
		}

	}

	public void performTicketDisplay()
			throws UnknownHostException, IOException, InterruptedException, ExecutionException {
		sendRequestToAESDK(TicketDisplayAurus.request());
		receiveResponseFromAESDK();
	}

	public List<String> performGetCardBin() throws Exception, IOException {

		List<String> gcbResults = new ArrayList<String>();
		List<String> gcbresult = null;
		POS_APIs pa = new POS_APIs();

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

			sendRequestToAESDK(ShowList.Request());

			// receiveResponseFromAESDK();

			Thread.sleep(800);
			performByPassRequest(2);

			/*
			 * sendRequestToAESDK(requestbuilder.Signature.Request());
			 * receiveResponseFromAESDK();
			 */

			// performByPassRequest(0);

			// Additional POS APIs

			/*
			 * pa.performed(); performByPassRequest();
			 */
		} else {
			performCloseRequest();
			Assert.fail("Get Card Bin Request denied!");
		}

		return gcbresult;

	}

	public List<String> performGetCardBinForSWIC() throws Exception, IOException {

		List<String> gcbResults = new ArrayList<String>();
		List<String> gcbresult = null;
		POS_APIs pa = new POS_APIs();

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
			boolean isProductionEnv = Utils.getEnvironment().toUpperCase().contains("P");

			switch (tokenType) {
			case "CardIdentifier":

				String cardIdentifier = isProductionEnv ? "2000000001581656" : "2000000000003154";
				System.out.println("Transaction Performed Using CI : " + cardIdentifier);
				return Arrays.asList("Approved", null, cardIdentifier, null);

			case "CRMToken":
				String crmToken = isProductionEnv ? "8920000010155342454" : "8920000010000048991";
				System.out.println("Transaction Performed Using CRM : " + crmToken);
				return Arrays.asList("Approved", null, null, crmToken);
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

	public void performByPassRequest() throws Exception, Exception, JDOMException {
		sendRequestToAESDK(ByPass.Random());
		receiveResponseFromAESDK();
	}

	public void performByPassRequest(int option) throws Exception, Exception, JDOMException {
		sendRequestToAESDK(ByPass.Option(option));
		receiveResponseFromAESDK();
	}

	public List<String> performCreditDebitSale() throws Exception, IOException, InterruptedException {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult;

		String tokenType = Utils.getTokenType();

		switch (tokenType) {
		case "CardIdentifier":
			String cardIdentifier = Utils.getCardIdentifier();
			System.out.println("Transaction Performed Using CI : " + cardIdentifier);
			gcbResult = Arrays.asList("Approved", null, cardIdentifier, null);
			break;

		case "CRMToken":

			String crmToken = Utils.getCRMToken();
			System.out.println("Transaction Performed Using CRM : " + crmToken);
			gcbResult = Arrays.asList("Approved", null, null, crmToken);
			break;
		default:
			gcbResult = performGetCardBin();
		}

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
				if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
					excelWriter.writeDataRefundOfSale(saleResult);
					saleResult.remove(3);
				}

			}
		} finally {

			// 1
			// performByPassRequest(1);
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

				if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
					saleResult.add(3, "RefundWithOutSale");
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

	public List<String> performRefundTransaction(List<String> saleResult)
			throws Exception, IOException, InterruptedException {
		// System.out.println( "In the Void Transaction, we used AurusPayTickNum and
		// Transaction ID with a total amount and tender amount of $"+
		// saleResult.get(3));

		Assert.assertNotEquals(saleResult.get(8), "0.00", "Amount is zero ; We are not processes Refund");
		List<String> RefundData = new ArrayList<String>();

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

			case "WPO":

				returnRequest = WorldPayEPP.returnRequest(saleResult);
				break;

			default:
				returnRequest = ReturnRequest.REFUND_REQUEST(transactionId, AurusPayTicketNum, amount);
				break;
			}
			// System.out.println(returnRequest);

			sendRequestToAESDK(returnRequest);
			String refundResponse = receiveResponseFromAESDK();

			Response_Parameters RefundResponse = new Response_Parameters(refundResponse); // IMP

			RefundData = RefundResponse.print_Response("Refund", parameters);
			RefundData.add(3, "Refund");
			excelWriter.writeDataRefundOfSale(RefundData);
			RefundData.remove(3);

			performCloseRequest();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("We are not able to performed refund transaction");
		}
		return RefundData;
	}

	public List<String> performVoidTransaction(List<String> saleResult)
			throws Exception, IOException, InterruptedException {

		// System.out.println("In the Void Transaction, we used AurusPayTickNum and
		// Transaction ID with a total amount and tender amount of $" +
		// SaleResult.get(3));

		Assert.assertNotEquals(saleResult.get(8), "0.00", "Amount is zero ; We are not processes Void");
		Assert.assertNotEquals(saleResult.get(8), null, "Amount is zero ; We are not processes Void");
		List<String> VoidData = new ArrayList<String>();
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

			VoidData = voidresponse.print_Response("Void", parameters);
			VoidData.add(3, "Void");
			excelWriter.writeDataRefundOfSale(VoidData);
			VoidData.remove(3);

		} catch (Exception e) {
			System.out.println("We are not able to performed void transaction");
		}
		return VoidData;
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
				if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
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

				if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
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

				if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
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
				if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
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
					if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
						excelWriter.writeDataRefundOfSale(saleResult);
						saleResult.remove(3);
					}

				}

			}
		} finally {
			performByPassRequest(1);
			performCloseRequest();
		}

		return saleResult;
	}

	public void performSWICTransaction() throws IOException, Exception {
		List<String> gcbResult = performGetCardBinForSWIC();

		System.out.println("I am In Swic ");

		// PIN ENTER
		sendRequestToAESDK(WICRequestBuilder.buildWICCardRequestForPin());
		String pinResponse = receiveResponseFromAESDK();
		Response_Parameters PINResponse = new Response_Parameters(pinResponse);
		List<String> PINData = PINResponse.print_Response("PIN Response", parameters);
		PINData.add(3, "PIN ENTER");
		excelWriter.writeDataRefundOfSale(PINData);

		// Balance Inquiry

		sendRequestToAESDK(WICRequestBuilder.buildWICCardRequestForBalanceInquiry());
		String inquiryResponse = receiveResponseFromAESDK();
		Response_Parameters InquiryResponse = new Response_Parameters(inquiryResponse);
		List<String> InquiryData = InquiryResponse.print_Response("Balance Inquiry ", parameters);
		InquiryData.add(3, "Balance Inquiry");
		excelWriter.writeDataRefundOfSale(InquiryData);

		// Sale

		sendRequestToAESDK(WICRequestBuilder.buildWICCardRequestForBalanceInquiry());
		String Sale = receiveResponseFromAESDK();
		Response_Parameters SaleResponse = new Response_Parameters(Sale);
		List<String> Saledata = SaleResponse.print_Response("Sale ", parameters);
		Saledata.add(3, "Sale");
		excelWriter.writeDataRefundOfSale(Saledata);

		// Void

		sendRequestToAESDK(WICRequestBuilder.buildWICCardRequestForBalanceInquiry());
		String Void = receiveResponseFromAESDK();
		Response_Parameters VoidResponse = new Response_Parameters(Void);
		List<String> VoidData = VoidResponse.print_Response("Void ", parameters);
		VoidData.add(3, "Void");
		excelWriter.writeDataRefundOfSale(VoidData);

	}

	public List<String> IncommTransaction() throws Exception, Exception {
		List<String> LS_DATA = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String IQulificationReq = IncommIQTransRequest.Request(gcbResult.get(1), "82"); // IQ
				IQulificationReq.trim();
				// Sale Satrted
				// System.out.println(balance_Request);
				sendRequestToAESDK(IQulificationReq);

				String IQulification = receiveResponseFromAESDK();
				// System.out.println(balance_Respose);

				Response_Parameters IQulificationRes = new Response_Parameters(IQulification);
				List<String> Data = IQulificationRes.print_Response(" Iteam Qulification : ", parameters);
				Data.add(3, "Iteam Qulification ");
				excelWriter.writeDataRefundOfSale(Data);

				Assert.assertEquals(IQulificationRes.getParameterValue("ResponseText"), ApprovedText);

			}
			// performCloseRequest();

			String Balance_LookUp = IncommIQTransRequest.Request(gcbResult.get(1), "12"); // Balance LookUp
			Balance_LookUp.trim();
			sendRequestToAESDK(Balance_LookUp);
			// System.out.println(Balance_LookUp);
			String BLookUp_Respose = receiveResponseFromAESDK();
			// System.out.println(BLookUp_Respose);
			Response_Parameters saleResponse = new Response_Parameters(BLookUp_Respose);
			List<String> saleResult = saleResponse.print_Response(" BLookUp_Response  : ", parameters);
			saleResult.add(3, "BLookUp_Respose");
			excelWriter.writeDataRefundOfSale(saleResult);

			Assert.assertEquals(saleResponse.getParameterValue("ResponseText"), ApprovedText);

			// performCloseRequest();

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
				Response_Parameters B_Response = new Response_Parameters(BLookUp_Respose);
				List<String> Result = B_Response.print_Response("Balance LookUp  : ", parameters);
				Result.add(3, "Balance LookUp");
				excelWriter.writeDataRefundOfSale(Result);

				Assert.assertEquals(B_Response.getParameterValue("ResponseText"), ApprovedText);

				// performCloseRequest();
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

	public List<String> performWorldPayEPPTransaction() throws Exception, Exception {
		List<String> Result = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {

				String SaleRequest = WorldPayEPP.Request(gcbResult.get(1), "01"); // Balance LookUp
				SaleRequest.trim();
				sendRequestToAESDK(SaleRequest);
				// System.out.println(Balance_LookUp);
				String SaleResponse = receiveResponseFromAESDK();
				// System.out.println(BLookUp_Respose);
				Response_Parameters S_Response = new Response_Parameters(SaleResponse);
				Result = S_Response.print_Response("Sale  : ", parameters);
				Result.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(Result);
				Result.remove(3);

			}

		} finally

		{
			performByPassRequest(1);
			performCloseRequest();

		}
		return Result;

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
		// System.out.println(gcbResult);
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {

				String Sale_Request = Fleet.SaleRequest(gcbResult);

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				System.out.println(Sale_Request);
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

	public List<String> PR_performCreditDebitSale(String Amount, String TransType)
			throws Exception, IOException, InterruptedException {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult;

		String tokenType = Utils.getTokenType();

		switch (tokenType) {
		case "CardIdentifier":
			String cardIdentifier = Utils.getCardIdentifier();
			System.out.println("Transaction Performed Using CI : " + cardIdentifier);
			gcbResult = Arrays.asList("Approved", null, cardIdentifier, null);
			break;

		case "CRMToken":

			String crmToken = Utils.getCRMToken();
			System.out.println("Transaction Performed Using CRM : " + crmToken);
			gcbResult = Arrays.asList("Approved", null, null, crmToken);
			break;
		default:
			gcbResult = performGetCardBin();
		}

		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {

				String Sale_Request = CD_Sale_Request.CreditDebitSaleRequest(gcbResult.get(1), gcbResult.get(2),
						gcbResult.get(3), Amount, TransType);

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				saleResult = saleResponse.print_Response(" Sale  : ", parameters);
				saleResult.add(3, "Sale");
				excelWriter.writeDataRefundOfSale(saleResult);

			}
		} finally {

			// 1
			performByPassRequest(1);
			performCloseRequest();

		}

		return saleResult;
	}

	public List<String> PF_performFSASale(String amount, String transType)
			throws Exception, IOException, InterruptedException {
		// GCB Started
		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBinAllowKeyed();

		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String Sale_Request = FSARequest.FSA_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2), gcbResult.get(3),
						Double.valueOf(amount.substring(3, 6)), transType);

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				saleResult = saleResponse.print_Response(" Sale  : ", parameters);
				saleResult.add(3, "Sale");
				if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
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

	public List<String> PR_performEBTSale(String AMT, String TransType)
			throws Exception, IOException, InterruptedException {

		List<String> saleResult = new ArrayList<String>();

		List<String> gcbResult = performGetCardBin();
		try {

			if (gcbResult.get(0).equalsIgnoreCase("Approved")) {
				String Sale_Request = EBTRequest.PF_EBT_SALE_REQUEST(gcbResult.get(1), gcbResult.get(2),
						gcbResult.get(3), AMT, TransType);

				// Sale Satrted

				sendRequestToAESDK(Sale_Request);
				// System.out.println(Sale_Request);
				String sale_Respose = receiveResponseFromAESDK();
				// System.out.println(sale_Respose);
				Response_Parameters saleResponse = new Response_Parameters(sale_Respose);
				saleResult = saleResponse.print_Response(" Sale  : ", parameters);
				saleResult.add(3, "Sale");

				if (Utils.getAutoDualProcessor().equalsIgnoreCase("N")) {
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
}
