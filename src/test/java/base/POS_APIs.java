package base;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.jdom2.JDOMException;
import org.testng.Assert;

import requestbuilder.ByPass;
import requestbuilder.GetUserInput;
import requestbuilder.ShowScreen;
import responsevalidator.Response_Parameters;
import utilities.Utils;
import xmlrequestbuilder.CloseRequest;

public class POS_APIs extends BaseClass {

	public void beforeGetCardBinAPIs() throws Exception, IOException, InterruptedException {

		sendRequestToAESDK(GetUserInput.MperkNumberRequest());

		Thread.sleep(1000);

		sendRequestToAESDK(ByPass.Random());
		receiveResponseFromAESDK();

	}

	private void performTransamountConfirmation(String amount)
			throws UnknownHostException, IOException, InterruptedException, JDOMException, ExecutionException {
		sendRequestToAESDK(ShowScreen.showScreenRequest(amount));
		String confirmResponse = receiveResponseFromAESDK();
		try {
			Response_Parameters confirmresponse = new Response_Parameters(confirmResponse);

			Assert.assertEquals("01", confirmresponse.getParameterValue("ButtonReturn"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String generateTransactionAmount()
			throws UnknownHostException, IOException, InterruptedException, JDOMException, ExecutionException {
		String roundedAmountString;

		POS_APIs pa = new POS_APIs();

		if (Utils.getEnvironment().toUpperCase().contains("P")) {
			roundedAmountString = "0.01";
		} else {
			roundedAmountString = String.valueOf(new Random().nextInt(100) + 1);
			roundedAmountString = roundedAmountString + ".00";

			// roundedAmountString = "05" + ".08";
			// roundedAmountString = "100.11";
			 roundedAmountString = "0.01";
			// roundedAmountString = "999.14";
		}

	//	pa.performTransamountConfirmation(roundedAmountString); // Comment this line when you are performing CI and CRM
		// transactions
		return roundedAmountString;
	}

	public void performed() throws Exception, IOException, InterruptedException {
		// POS APIs 1

		Random random = new Random();
		int randomNumber = random.nextInt(6)+1;

		// int randomNumber = randomNumber;
//		System.out.println(randomNumber);

		switch (randomNumber) {
		case 1:

			// Bypass Option 2

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Get User Input
			sendRequestToAESDK(GetUserInput.MperkNumberRequest());
			Thread.sleep(300);

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			sendRequestToAESDK(GetUserInput.InvalidMperkNumberRequest());
			Thread.sleep(300);

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// show screen

			sendRequestToAESDK(ShowScreen.ALUScreen06Request());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			sendRequestToAESDK(ShowScreen.HostSuppliedMperksRequest());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Show Screen

			break;

		case 2:

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			sendRequestToAESDK(ShowScreen.receiptOptionsRequest());
			sendRequestToAESDK(ByPass.Option2());

			// Get User Input
			sendRequestToAESDK(GetUserInput.MperkPINRequest());
			Thread.sleep(300);

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			sendRequestToAESDK(GetUserInput.InvalidMperkPINRequest());
			Thread.sleep(300);

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// show screen

			sendRequestToAESDK(ShowScreen.ALUScreen07Request());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Show Screen

			break;

		case 3:
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Get User Input
			sendRequestToAESDK(GetUserInput.ALUScreen01Request());
			Thread.sleep(300);

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			sendRequestToAESDK(GetUserInput.ALUScreen02Request());
			Thread.sleep(300);

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// show screen

			sendRequestToAESDK(ShowScreen.FirstUseDiscountRequest());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Show Screen

			break;

		case 4:
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Get User Input
			sendRequestToAESDK(GetUserInput.ALUScreen03Request());
			Thread.sleep(300);

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			sendRequestToAESDK(GetUserInput.ALUScreen04Request());
			Thread.sleep(300);

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// show screen

			sendRequestToAESDK(ShowScreen.MULTICopounRequest());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Show Screen

			break;

		case 5:
			sendRequestToAESDK(ByPass.Random());

			receiveResponseFromAESDK();

			// Get User Input
			sendRequestToAESDK(GetUserInput.ALUScreen05Request());
			Thread.sleep(300);

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			sendRequestToAESDK(GetUserInput.MperkPINRequest());
			Thread.sleep(300);

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// show screen

			sendRequestToAESDK(ShowScreen.HighValuePromptRequest());
			// System.out.println(ShowScreen.HighValuePromptRequest());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Show Screen

			break;
		case 6:

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Get User Input
			sendRequestToAESDK(GetUserInput.ALUScreen05Request());
			Thread.sleep(300);

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			sendRequestToAESDK(GetUserInput.InvalidMperkPINRequest());
			Thread.sleep(300);

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// show screen

			sendRequestToAESDK(ShowScreen.DuplicatemperksRequest());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// show screen

			sendRequestToAESDK(ShowScreen.ALUScreen06Request());
			receiveResponseFromAESDK();

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Show Screen

			break;

		default:
			// Bypass Option 2

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Get User Input
			sendRequestToAESDK(GetUserInput.MperkNumberRequest());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			sendRequestToAESDK(GetUserInput.InvalidMperkNumberRequest());
			receiveResponseFromAESDK();

			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Get User Input
			sendRequestToAESDK(GetUserInput.MperkPINRequest());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Get User Input
			sendRequestToAESDK(GetUserInput.InvalidMperkPINRequest());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// show screen

			sendRequestToAESDK(ShowScreen.ALUScreen06Request());
			receiveResponseFromAESDK();

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			// Show Screen

			// Bypass Option 0
			sendRequestToAESDK(ByPass.Random());
			receiveResponseFromAESDK();

			break;

		}

	}
}
