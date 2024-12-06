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

public class TC_EBT extends BaseClass {

	@Test(invocationCount = 20)
	public void RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performEBTSale();
		Utils.printResults(saleResult);
		/*
		 * Utils.printResults(saleResult);
		 * 
		 * List<String> returnResults = performRefundTransaction(saleResult);
		 * Utils.printResults(returnResults);
		 */
	}

	@Test(invocationCount = 2)
	public void VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performEBTSale();
		Utils.printResults(saleResult);

		List<String> returnResults = performVoidTransaction(saleResult);
		Utils.printResults(returnResults);
	}

	@Test(invocationCount = 2)
	public void VoidOfRefundWithoutsale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performEBT_RW_Sale();
		Utils.printResults(saleResult);
		List<String> returnResults = performVoidTransaction(saleResult);
		Utils.printResults(returnResults);
	}

	@AfterMethod
	public void saveXLFile()
			throws UnknownHostException, IOException, InterruptedException, JDOMException, ExecutionException {

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("EBT"));

	}
}
