package mtestcases;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import utilities.Utils;

public class TC_Check extends BaseClass {

	@Test(invocationCount = 10)
	public void voidOfCheckTransaction() throws IOException, Exception {

		List<String> saleResult = performCheckTransaction();

			performVoidTransaction(saleResult);   

	}

	@AfterMethod
	public void saveXLFile() throws Exception {

		performByPassRequest(0);
		performCloseRequest();
		excelWriter.saveExcelFile(Utils.setFileName("CHECK"));


	}

}
