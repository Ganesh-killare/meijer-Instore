package mtestcases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.Signature;
import java.util.List;

import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseClass;
import requestbuilder.ByPass;
import requestbuilder.CD_Sale_Request;
import requestbuilder.GCBRequest;
import requestbuilder.GetUserInput;
import requestbuilder.ShowList;
import xmlrequestbuilder.CloseRequest;

public class IssueReproduce extends BaseClass {

	@Test(invocationCount = 1000)
	public void GCBFrezeIssue() throws UnknownHostException, IOException, InterruptedException, JDOMException {
		sendRequestToAESDK(GetUserInput.MperkNumberRequest());
		//Thread.sleep(10);
		sendRequestToAESDK(ByPass.Option2());

		System.out.println(receiveResponseFromAESDK());
		sendRequestToAESDK(GCBRequest.GCB_REQUEST());
		Thread.sleep(20);
		sendRequestToAESDK(ByPass.Option0());
		receiveResponseFromAESDK();

		sendRequestToAESDK(CloseRequest.Close_Transaction_Request());
		receiveResponseFromAESDK();

	}

	public void GCB_TokenFlushIssueWithShowList() throws Exception, Exception {
		List<String> GCBResult = performGetCardBin();
		Assert.assertEquals("Approved", GCBResult.get(0));
		sendRequestToAESDK(ShowList.Request());
		receiveResponseFromAESDK();
		sendRequestToAESDK(requestbuilder.Signature.Request());
		receiveResponseFromAESDK();

		String Sale_Request = CD_Sale_Request.CD_SALE_REQUEST(GCBResult.get(1), GCBResult.get(2), GCBResult.get(3));

		sendRequestToAESDK(Sale_Request);
		receiveResponseFromAESDK();

	}

	@Test(invocationCount = 1)
	public void doubleChargeIssue() throws Exception {
		System.out.println("Dont do the any activity on first GCB Request");
		sendRequestToAESDK(GCBRequest.GCB_REQUEST());
		receiveResponseFromAESDK();

		Thread.sleep(4000);

		System.out.println("Do the any activity on GCB Request");

		performCreditDebitSale();

		sendRequestToAESDK(CloseRequest.Close_Transaction_Request());

	}

	@Test(invocationCount = 100)
	public void ByPaasssss() throws Exception {
		performGetCardBin();
	

	}
}
