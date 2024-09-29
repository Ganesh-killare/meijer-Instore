package mtestcases;

import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import utilities.Utils;

public class TC_CreditAndDebit extends BaseClass {

	@Test(invocationCount = 200)
	public void RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		// Utils.printResults(saleResult);

		 performRefundTransaction(saleResult);

	}

	@Test(invocationCount = 1000)
	public void VoidOfSale() throws IOException, Exception {   
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		// Utils.printResults(saleResult);

			 performVoidTransaction(saleResult);
		}


	@Test(invocationCount = 100)
	public void VoidOfRefundWithoutsale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
    
		System.out.println(fileName);

		List<String> saleResult = performCreditDebit_RW_Sale();
		// Utils.printResults(saleResult);

		performVoidTransaction(saleResult);

	}

	@AfterMethod
	public void saveXLFile() throws Exception {    

		performByPassRequest(0);
		performCloseRequest();
		excelWriter.saveExcelFile(Utils.setFileName(fileName));

	}

	@Test(invocationCount = 1)
	public void testGCB() throws Exception, Exception {
		performGetCardBin();

	}

}
