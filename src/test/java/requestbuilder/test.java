package requestbuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.util.Assert;

import base.BaseClass;
import utilities.GIft_Data;

public class test {
	@Test(dataProvider = "GIFT_DATA", dataProviderClass = GIft_Data.class, priority = 1)
	public void testRequests(String transactionType, String amount, String cardNumber, String entrySource,
			String transtype, String subtransType, String upsdata) {
		/*
		 * String saleRequest = CDPC_Sale_Request.CDPC_SALE_REQUEST("11","22","12");
		 * System.out.println(saleRequest); String refund =
		 * ReturnRequest.REFUND_REQUEST("1111111", "2222", "10.12");
		 * System.out.println(refund); String voidRequest =
		 * ReturnRequest.VOID_REQUEST("1111111", "2222", "10.12");
		 * System.out.println(voidRequest); String gcb = GCBRequest.GCB_REQUEST("Y");
		 * System.out.println(gcb); String closeReq = CloseRequest.CLOSE_REQUEST();
		 * System.out.println(closeReq); String ebt = EBTRequest.EBT_SALE_REQUEST("111",
		 * null, "12"); System.out.println(ebt); String be =
		 * BalanceEnquiry.EWIC_BALANCEINQUIRY_REQUEST("12", "265", "255");
		 * System.out.println(be); String ewic =
		 * EwicSaleRequest.EWIC_SALE_REQUEST("256", "25454", "6565");
		 * System.out.println(ewic); String fsa = FSARequest.FSA_SALE_REQUEST(null,
		 * null, null, 0.13); String fsa1 = FSARequest.FSA_SALE_REQUEST(null, null,
		 * null); System.out.println(fsa); System.out.println(fsa1); String gcb1 =
		 * GCBRequest.GCB_REQUEST("Y", "2000000000"); System.out.println(gcb1);
		 */

		String gf = GiftRequest.GIFT_REQUEST(amount, cardNumber, entrySource, subtransType, transtype, upsdata,
				"CardToken");

		System.out.println(gf);
	}

	private static String getHostIP() {
		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
		String ipAddress = localhost.getHostAddress();
		return ipAddress;
	}

//	@BeforeMethod

	@Test(invocationCount = 5)
	public void testRequest() throws IOException {
		List<String> Data = Arrays.asList("APPROVAL", "192242463559683905", "224246355968393323", "10.00", "VIC");
		List<String> tokens = Arrays.asList("CardToken", "CI", "CRM");
		/*
		 * System.out.println(SoloTronRequest.Request("611233055256456565",
		 * getHostIP()));
		 * 
		 * System.out.println(":".repeat(150));
		 * 
		 * System.out.println(SoloTronRequest.returnRequest(Data));
		 */

		/*
		 * System.out.println(IncommIQTransRequest.Request(getHostIP(), getHostIP()));
		 * System.out.println(":".repeat(150));
		 * System.out.println(SoloTronRequest.Request(getHostIP(), getHostIP()));
		 */

		System.out.println(FSARequest.FSA_RW_SALE_REQUEST(getHostIP(), getHostIP(), getHostIP()));
	}

//	@BeforeMethod
	public void testMethod() {
		org.testng.Assert.assertEquals("gg", "gr");
	}

	@Test(invocationCount = 1)
	public void tesrt() {
		System.out.println(Fleet.SaleRequest(null));
		;

	}

	@Test(invocationCount = 1)
	public void TestTheMethod() {

		int[] array = { 1, 2, 2, 3, 4, 4, 4, 5, 6, 1 };
		int[] counts = new int[array.length];

		for (int num : array) {
			counts[num]++;
		}

		for (int i = 1; i < counts.length; i++) {
			if (counts[i] > 1) {
				System.out.println("Number " + i + " is repeated " + counts[i] + " times.");
			}
		}

	}
}
