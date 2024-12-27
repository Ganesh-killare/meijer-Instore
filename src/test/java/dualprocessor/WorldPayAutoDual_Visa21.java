package dualprocessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BaseClass;
import reporting.AESDKData;
import responsevalidator.Response_Parameters;
import testcases.processorfailourchase.TC_ProcessorFailour13;
import utilities.P_XL_Utility;
import utilities.ProcessorFailourXL;
import utilities.Utils;
import xmlrequestbuilder.CloseRequest;
import xmlrequestbuilder.Refund_Request_Modification;

public class WorldPayAutoDual_Visa21 extends BaseClass {

	String fileName = "C_ProcessorFailure";
	String transType = "01"; // Change accordingly
	String transName = "sale"; // Change accordingly
	String Amount = "100.21";
	String ResponseCode = "56";

	BaseClass cp = new BaseClass();
	ProcessorFailourXL xlWriter = new ProcessorFailourXL();
	AESDKData ad = new AESDKData();

	P_XL_Utility xl = new P_XL_Utility();

	List<String> headlines = xlWriter.headlineSetter(ResponseCode, "", "WorldPay");

	private static int invocationCount = 1;

	@BeforeClass
	public void setUp() throws Exception, InterruptedException, Exception {
		xlWriter.writeHeadline(headlines.get(0)); // HeadLine set
		System.out.println("Call Indrajeet for the rest the counter.....");

		Scanner sc = new Scanner(System.in);
		String msg = sc.nextLine();
		System.out.println(msg);
		sc.close();

		// Modified File name
		fileName = fileName + Amount;

		System.out.println("Normal Credit / Debit Sale ");

		List<String> saleData = performCreditDebitSale();

		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(5));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
		xlWriter.writeHeadline(headlines.get(1));

	}

	@Test(invocationCount = 2, priority = 1)
	public void performNoramal_CD_Sale() throws IOException, InterruptedException, Exception {
		System.out.println("Normal Credit / Debit Sale ");

		List<String> saleData = performCreditDebitSale();

		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(5));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 1, priority = 2)
	public void performNormalFSA_TXN() throws IOException, InterruptedException, Exception {
		System.out.println("Normal FSA Sale ");

		List<String> saleData = performFSASale();
		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(5));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 1, priority = 3)
	public void performWIC_TXN() throws IOException, InterruptedException, Exception {

		System.out.println("Normal E-WIC Sale ");

		List<String> saleData = perform_eWICSale();
		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(5));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 2, priority = 4)
	public void performEBT_TXN() throws IOException, InterruptedException, Exception {
		System.out.println("Normal EBT Sale ");

		List<String> saleData = performEBTSale();
		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(5));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 5, priority = 5)
	public void PF_performCreditDebit() throws IOException, InterruptedException, Exception {

		System.out.println("Hardcode Amount 100.21 Use only Credit VISA Sale ");

		try {
			List<String> saleData = PR_performCreditDebitSale(Amount, transType);

			List<String> expectedList = xlWriter.generateTransactionSteps("91", invocationCount);
			saleData.add(0, expectedList.get(1));
			saleData.add(saleData.size() - 1, expectedList.get(6));
			saleData.add(expectedList.get(expectedList.size() - 1));
			xlWriter.writeTransactionData(saleData);
			xl.writeDataForVoid(Utils.getVoidData(saleData));
		} finally {
			invocationCount++;
			if (invocationCount == 6) {
				xlWriter.writeHeadline(headlines.get(2));
			}
		}

	}

//	@Test(invocationCount = 2, priority = 6)
	public void PF_performEBT_TXN() throws IOException, InterruptedException, Exception {
		System.out.println("Hardcode Amount EBT Sale ");
		try {
			List<String> saleData = PR_performEBTSale(Amount, transType);
			List<String> expectedList = xlWriter.generateTransactionSteps("91", invocationCount);
			saleData.add(0, expectedList.get(1));
			saleData.add(saleData.size() - 1, expectedList.get(6));
			saleData.add(expectedList.get(expectedList.size() - 1));
			xlWriter.writeTransactionData(saleData);
			xl.writeDataForVoid(Utils.getVoidData(saleData));
		} finally {
			invocationCount++;
			if (invocationCount == 6) {
				xlWriter.writeHeadline(headlines.get(2));
			}
		}

	}

	// @Test(invocationCount = 1, priority = 7)
	public void PF_performFSA_TXN() throws IOException, InterruptedException, Exception {

		System.out.println("Hardcode Amount FSA Sale ");

		try {
			List<String> saleData = PF_performFSASale(Amount, transType);
			List<String> expectedList = xlWriter.generateTransactionSteps("91", invocationCount);
			saleData.add(0, expectedList.get(1));
			saleData.add(saleData.size() - 1, expectedList.get(6));
			saleData.add(expectedList.get(expectedList.size() - 1));
			xlWriter.writeTransactionData(saleData);
			xl.writeDataForVoid(Utils.getVoidData(saleData));
		} finally {
			invocationCount++;
			xlWriter.writeHeadline(headlines.get(2));
		}
	}

	@Test(invocationCount = 2, priority = 8)
	public void AF_performNoramal_CD_Sale() throws IOException, InterruptedException, Exception {
		System.out.println("Normal Credit / Debit Sale ");

		List<String> saleData = performCreditDebitSale();

		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(2));
		saleData.add(saleData.size() - 1, expectedList.get(7));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 1, priority = 9)
	public void AF_performNormalFSA_TXN() throws IOException, InterruptedException, Exception {

		System.out.println("Normal FSA Sale ");

		List<String> saleData = performFSASale();
		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(2));
		saleData.add(saleData.size() - 1, expectedList.get(7));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 1, priority = 10)
	public void AF_performWIC_TXN() throws IOException, InterruptedException, Exception {

		System.out.println("Normal e-WIC Sale ");

		List<String> saleData = perform_eWICSale();
		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(2));
		saleData.add(saleData.size() - 1, expectedList.get(7));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 2, priority = 11)
	public void AF_performEBT_TXN() throws IOException, InterruptedException, Exception {
		System.out.println("Normal EBT Sale ");

		List<String> saleData = performEBTSale();
		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(2));
		saleData.add(saleData.size() - 1, expectedList.get(7));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(priority = 12, dataProvider = "VoidData", dataProviderClass = TC_ProcessorFailour13.class)

	public void VoidAllTransactions(String TransID, String AurusPayTicketNumber, String amount, String transType,
			String PMI) throws Exception, Exception, InterruptedException {

		try {
			if (!amount.equalsIgnoreCase("0.00") && !amount.isEmpty()
					&& !TransID.substring(0, 1).equalsIgnoreCase("O")) {
				String VOid_Request = Refund_Request_Modification.modified_Refund_Request(transType, amount,
						AurusPayTicketNumber, TransID);

				cp.sendRequestToAESDK(VOid_Request);
				String voidResponse = cp.receiveResponseFromAESDK();

				Response_Parameters VoidResponse = new Response_Parameters(voidResponse); // IMP

				List<String> VoidData = VoidResponse.print_Response("VOID", parameters);
				VoidData.add(3, "Void");

				List<String> expectedList = xlWriter.generateTransactionSteps("91", invocationCount);

				VoidData.add(0, expectedList.get(0));
				VoidData.add(VoidData.size() - 1, expectedList.get(8));
				VoidData.add("Parent transId is :- " + TransID + " ,  AurusPayTicketNumber :- " + AurusPayTicketNumber
						+ " And sale Processor MID is :- " + PMI);

				xlWriter.writeTransactionData(VoidData);

				String ProMerID = VoidResponse.getParameterValue("ProcessorMerchantId");
				ad.writeAESDKRequestAndResponse(VOid_Request, voidResponse);
				Assert.assertEquals(ProMerID, PMI, "Processor ID MissMatch.......");
			}

		} finally {
			cp.sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
			cp.receiveResponseFromAESDK();
			// xlWriter.saveExcelFile(Utils.setFileName(fileName));
		}

	}

	@Test(invocationCount = 1, priority = 13)
	public void resetCounter() throws IOException, InterruptedException, Exception {
		xlWriter.writeHeadline(headlines.get(0)); // HeadLine set
		System.out.println("Call Indrajeet for the rest the counter.....");

		// Set Copunter 1
		invocationCount = 1;

		Scanner sc = new Scanner(System.in);
		String msg = "";
		if (sc.hasNext()) {
			msg = sc.next();
		} else {
			System.out.println("We will Wait 2 mins ");
			Thread.sleep(200000);
		}
		System.out.println(msg);
		sc.close();

		System.out.println("Normal Credit / Debit Sale ");

		List<String> saleData = performCreditDebitSale();

		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(5));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
		xlWriter.writeHeadline(headlines.get(3));

	}

	@Test(invocationCount = 2, priority = 14)
	public void _performNoramal_CD_Sale() throws IOException, InterruptedException, Exception {
		System.out.println("Normal Credit / Debit Sale ");

		List<String> saleData = performCreditDebitSale();

		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(9));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 1, priority = 15)
	public void _performNormalFSA_TXN() throws IOException, InterruptedException, Exception {
		System.out.println("Normal FSA Sale ");

		List<String> saleData = performFSASale();
		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(9));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 1, priority = 16)
	public void _performWIC_TXN() throws IOException, InterruptedException, Exception {

		System.out.println("Normal E-WIC Sale ");

		List<String> saleData = perform_eWICSale();
		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(9));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 2, priority = 17)
	public void _performEBT_TXN() throws IOException, InterruptedException, Exception {
		System.out.println("Normal EBT Sale ");

		List<String> saleData = performEBTSale();
		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(9));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@Test(invocationCount = 4, priority = 18)
	public void PF__performCreditDebit() throws IOException, InterruptedException, Exception {

		System.out.println("Hardcode Amount only Credit VISA  Sale ");

		try {
			List<String> saleData = PR_performCreditDebitSale(Amount, transType);

			List<String> expectedList = xlWriter.generateTransactionSteps("91", invocationCount);
			saleData.add(0, expectedList.get(1));
			saleData.add(saleData.size() - 1, expectedList.get(6));
			saleData.add(expectedList.get(expectedList.size() - 1));
			xlWriter.writeTransactionData(saleData);
			xl.writeDataForVoid(Utils.getVoidData(saleData));
		} finally {
			invocationCount++;
		}

	}

	@Test(invocationCount = 1, priority = 19)
	public void __performNoramal_CD_Sale() throws IOException, InterruptedException, Exception {
		System.out.println("Normal Credit / Debit Sale ");

		List<String> saleData = performCreditDebitSale();

		List<String> expectedList = xlWriter.generateTransactionSteps("91", 0);
		saleData.add(0, expectedList.get(0));
		saleData.add(saleData.size() - 1, expectedList.get(10));

		xlWriter.writeTransactionData(saleData);
		xl.writeDataForVoid(Utils.getVoidData(saleData));
	}

	@AfterMethod
	public void afterTXN() {
		xlWriter.saveExcelFile(Utils.setFileName(fileName));
		xl.saveExcelFile();
	}

}
