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

public class ManualProcessorFailure extends BaseClass {

	@Test(invocationCount = 1)
	public void CREDIT_DEBIT_RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();      

		performRefundTransaction(saleResult);

	}


	//@Test(invocationCount = 2)
	public void CREDIT_DEBIT_VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();

		performVoidTransaction(saleResult);

	}

	@Test(invocationCount = 1)
	public void EBT_RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performEBTSale();

		performRefundTransaction(saleResult);
	}

//	@Test(invocationCount = 2)
	public void EBT_VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performEBTSale();

		performVoidTransaction(saleResult);

	}

	// @Test(invocationCount = 1)
	public void FSA_RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performFSASale();

		performRefundTransaction(saleResult);
	}

	//@Test(invocationCount = 1)
	public void FSA_VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performFSASale();

		performVoidTransaction(saleResult);
	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException, ExecutionException {

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();    
		excelWriter.saveExcelFile(Utils.setFileName("ManualProcessorFailure"));   

	}

}
