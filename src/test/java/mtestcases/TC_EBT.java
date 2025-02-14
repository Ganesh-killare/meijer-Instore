package mtestcases;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import requestbuilder.ByPass;
import utilities.Utils;

public class TC_EBT extends BaseClass {

	@Test(invocationCount = 2)
	public void RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performEBTSale();
		Utils.printResults(saleResult);

	

		List<String> returnResults = performRefundTransaction(saleResult);                                 
		Utils.printResults(returnResults);                                     
  
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
			throws Exception {

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("EBT"));

	}
}
