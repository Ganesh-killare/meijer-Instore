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

public class TC_eWIC extends BaseClass {
	@Test(invocationCount = 2)
	public void VoidOfSale() throws IOException, Exception {
		try {
			fileName = new Exception().getStackTrace()[0].getMethodName();
			System.out.println(fileName);

			List<String> saleResult = perform_eWICSale();

			if (saleResult.get(0).equalsIgnoreCase(ApprovedText)) {
				performVoidTransaction(saleResult);
			}

		} catch (Exception e) {
			System.out.println(e);   
		}

	}

	@AfterMethod
	public void saveXLFile() throws UnknownHostException, IOException, InterruptedException, JDOMException {  

		sendRequestToAESDK(ByPass.Option2());
		receiveResponseFromAESDK();
		sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
		receiveResponseFromAESDK();
		excelWriter.saveExcelFile(Utils.setFileName("eWIC"));

	}

}
