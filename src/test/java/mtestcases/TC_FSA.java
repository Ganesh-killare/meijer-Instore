package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.jdom2.JDOMException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BaseClass;
import requestbuilder.ByPass;
import utilities.Utils;
import xmlrequestbuilder.CloseRequest;

public class TC_FSA extends BaseClass {

	@Test(invocationCount = 5)
	public void RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performFSASale();
		Utils.printResults(saleResult);

		performRefundTransaction(saleResult);
	}

	@Test(invocationCount = 5)
	public void VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();   
		System.out.println(fileName);

		List<String> saleResult = performFSASale();
		Utils.printResults(saleResult);

		performVoidTransaction(saleResult);

	}

	@Test(invocationCount = 5)
	public void VoidOfRefundWithoutsale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);   

		List<String> saleResult = performFSA_RW_Sale();
		Utils.printResults(saleResult);

		performVoidTransaction(saleResult);

	}

	// @Test
	public void performGCB() throws IOException, Exception {
		performGetCardBinAllowKeyed();
	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException {

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("FSA"));

	}

}
