package mtestcases;

import java.io.IOException;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import utilities.Utils;

public class TC_CreditAndDebit extends BaseClass {

	@Test(invocationCount = 10)
	public void RefundOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		Utils.printResults(saleResult);

		List<String> RefundResults = performRefundTransaction(saleResult);
		Utils.printResults(RefundResults);

	}

	@Test(invocationCount = 7)
	public void VoidOfSale() throws IOException, Exception {
		fileName = new Exception().getStackTrace()[0].getMethodName();
		System.out.println(fileName);

		List<String> saleResult = performCreditDebitSale();
		Utils.printResults(saleResult);

		List<String> voidResults = performVoidTransaction(saleResult);

		Utils.printResults(voidResults);
	}

	@Test(invocationCount = 7)
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

	@Test(invocationCount = 2)
	public void testGCB() throws Exception, Exception {
		performGetCardBin();

	}

}
