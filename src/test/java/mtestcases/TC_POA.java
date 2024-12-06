package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jdom2.JDOMException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import utilities.Utils;
import xmlrequestbuilder.CloseRequest;

public class TC_POA extends BaseClass {
	@Test(priority = 1)
	public void SSN_paymentOnAccountWithCheck() throws Exception, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);
		List<String> ALUData = performALUwithSSN();
		List<String> SaleData = performCheckTransaction();
		performPOA(SaleData.get(8), SaleData.get(12), ALUData.get(1), "CH");  

	}

	@Test(priority = 2)
	public void SSN_paymentOnAccountWithCash() throws IOException, InterruptedException, Exception {  
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);
		List<String> ALUData = performALUwithSSN();

		performPOA("10.00", null, ALUData.get(1), "CA");   
	}

	@Test(priority = 6)
	public void SSN_paymentOnAccountWithDebit() throws IOException, InterruptedException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);
		List<String> ALUData = performALUwithSSN();
		List<String> saleResult = performCreditDebitSale();
		performPOA(saleResult.get(8), saleResult.get(12), ALUData.get(1), "DB");
	}

	@Test(priority = 3)
	public void KEY_paymentOnAccountWithCheck() throws IOException, InterruptedException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> ALUData = performALUwithKey();
		List<String> SaleData = performCheckTransaction();
		performPOA(SaleData.get(8), SaleData.get(12), ALUData.get(1), "CH");
	}

	@Test(priority = 4)
	public void KEY_paymentOnAccountWithCash() throws IOException, InterruptedException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);
		List<String> ALUData = performALUwithKey();

		performPOA("10.00", null, ALUData.get(1), "CA");
	}

	@Test(priority = 5)
	public void KEY_paymentOnAccountWithDebit() throws IOException, InterruptedException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);
		List<String> ALUData = performALUwithKey();
		List<String> saleResult = performCreditDebitSale();
		performPOA(saleResult.get(8), saleResult.get(12), ALUData.get(1), "DB");

	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException, ExecutionException {

		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("POA"));

	}

}
