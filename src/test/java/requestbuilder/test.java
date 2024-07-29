package requestbuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

import org.testng.annotations.Test;

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

	@Test(invocationCount = 1)
	public void testRequest() throws IOException {

		System.out.println(AccountLookUp.withSSNandPhoneNumber());
		System.out.println(AccountLookUp.withProcessorToken());

	}

}
