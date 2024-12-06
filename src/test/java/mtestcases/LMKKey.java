package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jdom2.JDOMException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import requestbuilder.ByPass;
import utilities.Utils;

public class LMKKey extends BaseClass {

	@Test(invocationCount = 4, priority = 1 )
	public void Test_RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		Utils.printResults(saleResult);

		 List<String> returnResults = performRefundTransaction(saleResult);
		 Utils.printResults(returnResults);

	}

	 @Test(invocationCount = 4, priority = 2)
	public void Test_VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		Utils.printResults(saleResult);

		List<String> returnResults = performVoidTransaction(saleResult);
		Utils.printResults(returnResults);

	}

	@Test(invocationCount = 4, priority = 3)
	public void Test_RefundWithoutSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebit_RW_Sale();
		Utils.printResults(saleResult);
		List<String> returnResults = performVoidTransaction(saleResult);
		Utils.printResults(returnResults);

	}

	@Test(invocationCount = 2, priority = 4)
	public void EBT_RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performEBTSale();

		Utils.printResults(saleResult);

		 List<String> returnResults = performRefundTransaction(saleResult);
		 Utils.printResults(returnResults);
	}
	//@Test(invocationCount = 2, priority = 4)
	public void EBT_RefundWithoutSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);
		
		List<String> saleResult = performEBT_RW_Sale();
		
		Utils.printResults(saleResult);
		
		/*
		 * List<String> returnResults = performVoidTransaction(saleResult);
		 * Utils.printResults(returnResults);
		 */
	}

	@Test(invocationCount = 1,priority = 5)   
	public void E_WICSale() throws IOException, Exception {
		try {
			fileName = new Exception().getStackTrace()[0].getMethodName();
			System.out.println(fileName);

			List<String> saleResult = perform_eWICSale();

			 performVoidTransaction(saleResult);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@AfterMethod
	public void saveXLFile()
			throws UnknownHostException, IOException, InterruptedException, JDOMException, ExecutionException {    

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
	//	excelWriter.saveExcelFile(Utils.setFileName("UAT_WAR"));

	}

}
