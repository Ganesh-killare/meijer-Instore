package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.jdom2.JDOMException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import utilities.DateUtilities;
import xmlrequestbuilder.Close_Transaction;

public class TC_POA extends BaseClass {
	@Test(priority = 1)
	public void SSN_paymentOnAccountWithCheck() throws Exception, Exception {
		List<String> ALUData = performALUwithSSN();
		List<String> SaleData = performCheckTransaction();
		performPOA(SaleData.get(3), SaleData.get(2), ALUData.get(2), "CH");

	}
    
	@Test(priority = 2)
	public void SSN_paymentOnAccountWithCash() throws IOException, InterruptedException, Exception {   
		List<String> ALUData = performALUwithSSN();

		performPOA("10.00", null, ALUData.get(2), "CA");
	}

	@Test(priority = 6)
	public void SSN_paymentOnAccountWithDebit() throws IOException, InterruptedException, Exception {
		List<String> ALUData = performALUwithSSN();
		List<String> saleResult = performCreditDebitSale();
		performPOA(saleResult.get(3), saleResult.get(2), ALUData.get(2), "DB");
	}

	@Test(priority = 3)
	public void KEY_paymentOnAccountWithCheck() throws IOException, InterruptedException, Exception {    
		List<String> ALUData = performALUwithKey();
		List<String> SaleData = performCheckTransaction();
		performPOA(SaleData.get(3), SaleData.get(2), ALUData.get(2), "CH");
	}

	@Test(priority = 4)
	public void KEY_paymentOnAccountWithCash() throws IOException, InterruptedException, Exception {
		List<String> ALUData = performALUwithKey();

		performPOA("10.00", null, ALUData.get(2), "CA");
	}

	@Test(priority = 5)
	public void KEY_paymentOnAccountWithDebit() throws IOException, InterruptedException, Exception {
		List<String> ALUData = performALUwithKey();
		List<String> saleResult = performCreditDebitSale();
		performPOA(saleResult.get(3), saleResult.get(2), ALUData.get(2), "DB");
	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException {

		sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(DateUtilities.setFileName("POA_TRANSACTIONS"));

	}

}
