package mtestcases;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import utilities.Utils;

public class TC_Fleet extends BaseClass {

	@Test(invocationCount = 1)
	public void fleet_RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performFleetSale();

		performRefundTransaction(saleResult);

	}

	@Test(invocationCount = 100)
	public void fleet_VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();     
		System.out.println(fileName);

		List<String> saleResult = performFleetSale();

		performVoidTransaction(saleResult);
   
	}

	@Test(invocationCount = 100)
	public void fleet_VoidOfRefundWithoutsale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();

		System.out.println(fileName);

		List<String> saleResult = performFleetRefundWithoutSale();

		performVoidTransaction(saleResult);

	}

	@AfterMethod
	public void saveXLFile() throws Exception {
   
		performByPassRequest(0);
		performCloseRequest();
		excelWriter.saveExcelFile(Utils.setFileName(fileName));

	}
}
