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
import xmlrequestbuilder.CloseRequest;

public class TC_OTC extends BaseClass {

	@Test(invocationCount = 3)
	public void InComm_VoidOfSale() throws Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);
		List<String> SaleResult = IncommTransaction();   
		performVoidTransaction(SaleResult);
	}

    @Test(invocationCount = 3)
	public void InComm_RefundOfSale() throws Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();       
		System.out.println(fileName);            
		List<String> SaleResult = IncommTransaction();
		// System.out.println(SaleResult);
		performRefundTransaction(SaleResult);   
	}   

	@Test(invocationCount = 3)
	public void SolotronVoidOfSale() throws Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);
		List<String> SaleResult = SlutronTransactions();
		// System.out.println(SaleResult);
		performVoidTransaction(SaleResult);
	}

	@Test(invocationCount = 3)
	public void SolotronRefundOfSale() throws Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);
		List<String> SaleResult = SlutronTransactions();
		// System.out.println(SaleResult);
		performRefundTransaction(SaleResult);
	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException, ExecutionException {

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("OTC"));

	}

}
