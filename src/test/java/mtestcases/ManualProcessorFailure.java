package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.jdom2.JDOMException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import requestbuilder.ByPass;
import utilities.DateUtilities;
import xmlrequestbuilder.Close_Transaction;

public class ManualProcessorFailure extends BaseClass {

	@Test(invocationCount = 2)
	public void CREDIT_DEBIT_RefundOfSale() throws IOException, Exception {                  
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();

		if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {           
			performRefundTransaction(saleResult);
		}

	}

	@Test(invocationCount = 2)
	public void CREDIT_DEBIT_VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();

		if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {   
			performVoidTransaction(saleResult);
		}

	}
	
	@Test(invocationCount = 2)
	public void EBT_RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();         
		System.out.println(fileName);
   
		List<String> saleResult = performEBTSale();

		if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {   
			performRefundTransaction(saleResult);   
		}
	}

//	@Test(invocationCount = 2)
	public void EBT_VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();       
		System.out.println(fileName);

		List<String> saleResult = performEBTSale();

		if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {
			performVoidTransaction(saleResult);
		}

	}
	

//	@Test(invocationCount = 1)
	public void FSA_RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performFSASale();

		if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {      
			performRefundTransaction(saleResult);  
		}
	}

	@Test(invocationCount = 1)
	public void FSA_VoidOfSale() throws IOException, Exception {      
		fileName = new Exception().getStackTrace()[0].getMethodName();      
		System.out.println(fileName);

		List<String> saleResult = performFSASale();
   
		if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {        
			performVoidTransaction(saleResult);
		}

	}
	@AfterMethod      
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException {

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(DateUtilities.setFileName("ManualProcessorFailure"));

	}


}
