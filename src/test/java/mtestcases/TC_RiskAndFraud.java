package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.jdom2.JDOMException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import requestbuilder.ByPass;
import utilities.Utils;

public class TC_RiskAndFraud extends BaseClass {

	@Test(invocationCount = 20, priority = 1)
	public void Test_USA_Card() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		Utils.printResults(saleResult);
		

			// performRefundTransaction(saleResult);

	}

	@Test(invocationCount = 2, priority = 2)
	public void Test_UK_Card() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		Utils.printResults(saleResult);
			// performVoidTransaction(saleResult);
		}

	@Test(invocationCount = 2, priority = 2)
	public void Test_Canada_Card() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		Utils.printResults(saleResult);
		// 	performVoidTransaction(saleResult);
		}

	@Test(invocationCount = 2, priority = 2)
	public void Test_EUR_Card() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		Utils.printResults(saleResult);
			// performVoidTransaction(saleResult);

	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException {

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("Risk&Fraud"));

	}

}
