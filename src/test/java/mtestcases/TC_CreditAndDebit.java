package mtestcases;

import java.io.IOException;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import utilities.Utils;

public class TC_CreditAndDebit extends BaseClass {

	@Test(invocationCount = 1)
	public void saleRefundVoid() throws IOException, Exception {
		performCloseRequest();
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		 Utils.printResults(saleResult);

		
		  List<String> RefundResults = performRefundTransaction(saleResult); //
		 Utils.printResults(RefundResults); 
		  performVoidTransaction(RefundResults);
		 
	}
  
	@Test(invocationCount = 700)
	public void VoidOfSale() throws IOException, Exception {      
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);   

		List<String> saleResult = performCreditDebitSale();
		Utils.printResults(saleResult);

		List<String> voidResults = performVoidTransaction(saleResult);  

		Utils.printResults(voidResults);
	}

	@Test(invocationCount = 70)
	public void VoidOfRefundWithoutsale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();

		System.out.println(fileName);

		List<String> saleResult = performCreditDebit_RW_Sale();
		Utils.printResults(saleResult);

		List<String> voidResults = performVoidTransaction(saleResult);

		Utils.printResults(voidResults);

	}

	@AfterMethod
	public void saveXLFile(ITestResult Result) throws Exception {

		fileName = "CREDIT&DEBIT";

		if (Result.getStatus() == ITestResult.FAILURE) {
			excelWriter.saveExcelFile(Utils.setFileName(fileName));

			return;

		}

		performByPassRequest(0);
		performCloseRequest();
		excelWriter.saveExcelFile(Utils.setFileName(fileName));

	}

	@Test(invocationCount = 1)
	public void testGCB() throws Exception, Exception {
		performGetCardBin();

	}
	@Test(invocationCount = 1)
	public void testClose() throws Exception, Exception {
		performCloseRequest();
		
	}

}
