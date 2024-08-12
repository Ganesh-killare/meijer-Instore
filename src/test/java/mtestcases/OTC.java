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
import xmlrequestbuilder.Close_Transaction;

public class OTC extends BaseClass {

	@Test(invocationCount = 1)
	public void InComm_VoidOfSale() throws Exception {      
		fileName = new Exception().getStackTrace()[0].getMethodName();  
		System.out.println(fileName);
		List<String> SaleResult = IncommTransaction();
		System.out.println(SaleResult);
		if (SaleResult.get(0).equalsIgnoreCase(ApprovedText)) {          
			performVoidTransaction(SaleResult);
		}   
	}

	@Test(invocationCount = 1)
	public void SolotronVoidOfSale() throws Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();  
		System.out.println(fileName);   
		List<String> SaleResult = SlutronTransactions();
		System.out.println(SaleResult);
		if (SaleResult.get(0).equalsIgnoreCase(ApprovedText)) {          
			performVoidTransaction(SaleResult);
		} 
	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException {

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();  
		sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());   
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("OTC"));

	}

}
