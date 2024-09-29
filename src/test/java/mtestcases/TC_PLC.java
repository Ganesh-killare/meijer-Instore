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
import xmlrequestbuilder.CloseRequest;

public class TC_PLC extends BaseClass {

	@Test(invocationCount = 600)
	public void PLC_RefundOfSale() throws IOException, Exception {       
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performPLCSale();
    // Utils.printResults(saleResult);

			performRefundTransaction(saleResult);      
	}   

	@Test(invocationCount = 600)
	public void PLC_VoidOfSale() throws IOException, Exception {   
		fileName = new Exception().getStackTrace()[0].getMethodName();                
		System.out.println(fileName);    

		List<String> saleResult = performPLCSale();
	//	Utils.printResults(saleResult);

			 performVoidTransaction(saleResult);   

	}

	@Test(invocationCount = 60)
	public void PLC_VoidOfRefundWithoutsale() throws IOException, Exception {       
		fileName = new Exception().getStackTrace()[0].getMethodName();     
		System.out.println(fileName);

		List<String> saleResult = performPLC_RWSale();     

			performVoidTransaction(saleResult);
     
	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException {   

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();  
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("PLC"));    

	}

}   
