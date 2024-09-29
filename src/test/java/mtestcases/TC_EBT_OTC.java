package mtestcases;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import utilities.Utils;

public class TC_EBT_OTC extends BaseClass {
	@Test(invocationCount = 2)
	public void RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();   
		System.out.println(fileName);

		List<String> saleResult = performEBT_OTC_Transaction();

			performRefundTransaction(saleResult);

	}

	@Test(invocationCount = 2)
	public void voidOfRefundWithoutSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performebtOtcRefundWithoutSale();

			performVoidTransaction(saleResult);

	}

	@Test(invocationCount = 2)
	public void VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performEBT_OTC_Transaction();

			performVoidTransaction(saleResult);
	}

	@AfterMethod
	public void saveXLFile() throws Exception {

		performByPassRequest(0);
		performCloseRequest();
		excelWriter.saveExcelFile(Utils.setFileName("EBT_OTC"));

	}

//	@Test(invocationCount = 1)
	public void testGCB() throws Exception, Exception {    
		performGetCardBin();

	}

}
