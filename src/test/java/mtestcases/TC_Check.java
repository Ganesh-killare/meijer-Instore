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

public class TC_Check extends BaseClass {

	@Test(invocationCount = 1)
	public void voidOfCheckTransaction() throws IOException, Exception {
	

		List<String> saleResult = performCheckTransaction();

		if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {   
			performVoidTransaction(saleResult);
		}

	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException {

		sendRequestToAESDK(ByPass.Option0());
		receiveResponseFromAESDK();
		sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("CHECK"));

	}

}
