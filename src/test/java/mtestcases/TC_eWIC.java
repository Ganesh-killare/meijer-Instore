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

public class TC_eWIC extends BaseClass {
	@Test(invocationCount = 1)
	public void VoidOfSale() throws IOException, Exception {
		try {
			fileName = new Exception().getStackTrace()[0].getMethodName();
			System.out.println(fileName);

			List<String> saleResult = perform_eWICSale();
  
			performVoidTransaction(saleResult);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException, ExecutionException {

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(requestbuilder.CloseRequest.CLOSE_REQUEST());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("eWIC"));

	}

}
