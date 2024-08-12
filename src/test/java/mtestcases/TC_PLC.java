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

public class TC_PLC extends BaseClass {   

	@Test(invocationCount = 40)   
	public void PLC_RefundOfSale() throws IOException, Exception {       
		fileName = new Exception().getStackTrace()[0].getMethodName();       
		System.out.println(fileName);

		List<String> saleResult = performPLCSale();

		if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {        
			performRefundTransaction(saleResult);
		}
	}

	@Test(invocationCount = 40)   
	public void PLC_VoidOfSale() throws IOException, Exception {    
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performPLCSale();

		if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {         
			performVoidTransaction(saleResult);
		}

	}

	@Test(invocationCount = 4)
	public void PLC_VoidOfRefundWithoutsale() throws IOException, Exception {         
		fileName = new Exception().getStackTrace()[0].getMethodName();    
		System.out.println(fileName);

		List<String> saleResult = performPLC_RWSale();
    
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
		excelWriter.saveExcelFile(Utils.setFileName(fileName));   

	}

}
