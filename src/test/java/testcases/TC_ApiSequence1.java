package testcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import apisequence.ByPassScreen;
import apisequence.GetUserInputRequest;
import apisequence.ShowScreenRequest;
import base.BaseClass;
import responsevalidator.Response_Parameters;
import xmlrequestbuilder.Close_Transaction;
import xmlrequestbuilder.GCB_Modification;
import xmlrequestbuilder.Sale_Request_Modification;

public class TC_ApiSequence1 {

String[] parameters = { "CardToken", "CardIdentifier", "CRMToken", "CardEntryMode", "TransactionTypeCode",
"TransactionSequenceNumber", "CardType", "SubCardType", "TotalApprovedAmount", "ResponseText",
"ResponseCode", "TransactionIdentifier", "AurusPayTicketNum", "ApprovalCode", "ProcessorMerchantId" };
List<String> GCB_Parameters = new ArrayList<>(Arrays.asList(parameters));
BaseClass cp = new BaseClass();

@Test(invocationCount = 1)

public void test_GCB_ByPass_Activation() throws InterruptedException, Exception, IOException {

try {

/*
* // ByPass Screen 0
* 
* String ByPassScreenReq = ByPassScreen.ByPassScreenRequest("2");
* cp.sendRequestToAESDK(ByPassScreenReq); System.out.println(ByPassScreenReq);
* String ByPassResponse = cp.receiveResponseToAESDK();
* System.out.println(ByPassResponse); System.out.println("=".repeat(50)); //
* ByPass Screen 1 String ByPassScreenReq1 =
* ByPassScreen.ByPassScreenRequest("2");
* cp.sendRequestToAESDK(ByPassScreenReq1);
* System.out.println(ByPassScreenReq1); String ByPassResponse1 =
* cp.receiveResponseToAESDK(); System.out.println(ByPassResponse1);
* System.out.println("=".repeat(50));
*/
// Get User Input

String getUserInputReq = GetUserInputRequest.getUserInputRequest();
cp.sendRequestToAESDK(getUserInputReq);
System.out.println(getUserInputReq);
String getUserInput0 = cp.receiveResponseFromAESDK();
System.out.println(getUserInput0);
System.out.println("=".repeat(50));

// ByPass Screen  3
String ByPassScreenReq3 = ByPassScreen.ByPassScreenRequest("0");
cp.sendRequestToAESDK(ByPassScreenReq3);
System.out.println(ByPassScreenReq3);
String ByPassResponse3 = cp.receiveResponseFromAESDK();
System.out.println(ByPassResponse3);
System.out.println("=".repeat(50));

/*
* // ByPass Screen 4 String ByPassScreenReq4 =
* ByPassScreen.ByPassScreenRequest("2");
* cp.sendRequestToAESDK(ByPassScreenReq4);
* System.out.println(ByPassScreenReq4); String ByPassResponse4 =
* cp.receiveResponseToAESDK(); System.out.println(ByPassResponse4);
* System.out.println("=".repeat(50)); // ByPass Screen 4 String
* ByPassScreenReq5 = ByPassScreen.ByPassScreenRequest("0");
* cp.sendRequestToAESDK(ByPassScreenReq5);
* System.out.println(ByPassScreenReq5); String ByPassResponse5 =
* cp.receiveResponseToAESDK(); System.out.println(ByPassResponse5);
* System.out.println("=".repeat(50)); // Show screen 65740m
*/
String showScreenReq = ShowScreenRequest.showScreenRequest();
cp.sendRequestToAESDK(showScreenReq);
System.out.println(showScreenReq);
String showScreen = cp.receiveResponseFromAESDK();
System.out.println(showScreen);
System.out.println("=".repeat(50));

// GCB Started

String req = GCB_Modification.GCB_Request_Modified();
cp.sendRequestToAESDK(req);
System.out.println(req);
String res = cp.receiveResponseFromAESDK();
System.out.println(res);
Response_Parameters GCBPrameter = new Response_Parameters(res);
String result = GCBPrameter.getParameterValue("ResponseText");

String Sale_Request = Sale_Request_Modification
.modified_Sale_Request(GCBPrameter.getParameterValue("CardToken"), null, null, "01");
List<String> GCBXLData = GCBPrameter.print_Response("GCB", parameters);

// ByPass Screen  4
String ByPassScreenReq6 = ByPassScreen.ByPassScreenRequest("2");
cp.sendRequestToAESDK(ByPassScreenReq6);
System.out.println(ByPassScreenReq6);
String ByPassResponse6 = cp.receiveResponseFromAESDK();
System.out.println(ByPassResponse6);
System.out.println("=".repeat(50));
//Transrequest
if (result.equalsIgnoreCase("Approved")) {

// Sale Satrted

cp.sendRequestToAESDK(Sale_Request);
System.out.println(Sale_Request);
String sale_Respose = cp.receiveResponseFromAESDK();
System.out.println(sale_Respose);
}
// Close 
String close = Close_Transaction.Close_Transaction_Request();
cp.sendRequestToAESDK(close);
System.out.println(close);
String closeresponse = cp.receiveResponseFromAESDK();
System.out.println(closeresponse);
/*
* System.out.println(closeresponse); // ByPass Screen String ByPassScreenReq7 =
* ByPassScreen.ByPassScreenRequest("2");
* cp.sendRequestToAESDK(ByPassScreenReq7);
* System.out.println(ByPassScreenReq7); String ByPassResponse7 =
* cp.receiveResponseToAESDK(); System.out.println(ByPassResponse7);
* System.out.println("=".repeat(50));
*/
/*
* // ByPass Screen String ByPassScreenReq8 =
* ByPassScreen.ByPassScreenRequest("2");
* cp.sendRequestToAESDK(ByPassScreenReq8);
* System.out.println(ByPassScreenReq8); String ByPassResponse8 =
* cp.receiveResponseToAESDK(); System.out.println(ByPassResponse8);
* System.out.println("=".repeat(50));
*/
} finally {
// cp.sendRequestToAESDK(Close_Transaction.Close_Transaction_Request());
System.out.println("=".repeat(50));

Thread.sleep(5000);

}

}
}