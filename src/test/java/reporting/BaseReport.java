package reporting;

import java.util.List;

import org.json.JSONArray;



public class BaseReport {

	public static void GCB_Reporting(String trackData, List<String> prompts, String cardType, String responseText,
			String cardEntryMode, String RequestBody, String ResponseBody, String responseCode) {
		ExtentReportManager.logInfoDetails("Track Data is ");
		ExtentReportManager.logCardDataDetails("  " + trackData+ " ");
		ExtentReportManager.logInfoDetails("GCB Request body is ");
		ExtentReportManager.logJSONDetails(RequestBody);
		ExtentReportManager.logInfoDetails("Fleet Prompts Flag Tags are ");
		ExtentReportManager.logDetails(prompts.toString());
		ExtentReportManager.logInfoDetails("Card Entry Mode ");
		ExtentReportManager.logDetails(cardEntryMode);
		ExtentReportManager.logInfoDetails("Card Type is ");
		ExtentReportManager.logCardDataDetails(cardType);
		ExtentReportManager.logInfoDetails("GCB Response Text is ");
		ExtentReportManager.logDetails(responseText);
		ExtentReportManager.logInfoDetails("GCB Response Code is ");
		ExtentReportManager.logDetails(responseCode);
		ExtentReportManager.logInfoDetails("GCB Response Body is ");
		ExtentReportManager.logJSONDetails(ResponseBody.toString());

	}

	public static void pre_Auth_Reporting(String ResponseText, String AurusPayTicketNum, String ResponseCode,
			JSONArray AdditionalReceiptInfo, JSONArray ReceiptInfo, String TransactionIdentifier,
			String AuruspayTransactionId, String TotalApprovedAmount, String ProcessorResponseCode, String CardNumber,
			String CardExpiryDate , String preAuthRequest , String preAuthResponse) {
		
		ExtentReportManager.logInfoDetails("Pre-Auth Request ");
		ExtentReportManager.logJSONDetails(preAuthRequest.toString());
		
		ExtentReportManager.logInfoDetails("Total Approved Amount ");
		ExtentReportManager.logDetails(TotalApprovedAmount);
		
		ExtentReportManager.logInfoDetails("Response Text is  ");
		ExtentReportManager.logDetails(ResponseText);
		
		ExtentReportManager.logInfoDetails("Response Code is ");
		ExtentReportManager.logDetails(ResponseCode);

		ExtentReportManager.logInfoDetails("Aurus Pay Ticket Number is  ");
		ExtentReportManager.logDetails(AurusPayTicketNum);

		ExtentReportManager.logInfoDetails("Transaction ID is");
		ExtentReportManager.logDetails(TransactionIdentifier);

		ExtentReportManager.logInfoDetails("Processor Response Code is");
		ExtentReportManager.logDetails(ProcessorResponseCode);

		ExtentReportManager.logInfoDetails("Card Number is");
		ExtentReportManager.logDetails(CardNumber);

		ExtentReportManager.logInfoDetails("Card Expiry Date");
		ExtentReportManager.logDetails(CardExpiryDate);

		ExtentReportManager.logInfoDetails("Auruspay Transaction Id");
		ExtentReportManager.logDetails(AuruspayTransactionId);

		ExtentReportManager.logInfoDetails(" Receipt Info ");
		ExtentReportManager.logDetails(ReceiptInfo.toString());

		ExtentReportManager.logInfoDetails("Additional Receipt Info");
		ExtentReportManager.logDetails(AdditionalReceiptInfo.toString());

		ExtentReportManager.logInfoDetails("Pre-Auth Response  ");
		ExtentReportManager.logJSONDetails(preAuthResponse.toString());

		

	}
	/*
	 * public static void
	 * printRequestLogInReportWithoutRequestBody(RequestSpecification
	 * requestSpecification) { QueryableRequestSpecification
	 * queryableRequestSpecification =
	 * SpecificationQuerier.query(requestSpecification);
	 * ExtentReportManager.logInfoDetails("Endpoint is " +
	 * queryableRequestSpecification.getBaseUri());
	 * ExtentReportManager.logInfoDetails("Method is " +
	 * queryableRequestSpecification.getMethod());
	 * ExtentReportManager.logInfoDetails("Headers are ");
	 * ExtentReportManager.logHeaders(queryableRequestSpecification.getHeaders().
	 * asList()); }
	 * 
	 * public static void printResponseLogInReport(Response response) {
	 * ExtentReportManager.logInfoDetails("Response status is " +
	 * response.getStatusCode());
	 * ExtentReportManager.logInfoDetails("Response Headers are ");
	 * ExtentReportManager.logHeaders(response.getHeaders().asList());
	 * ExtentReportManager.logInfoDetails("Response body is ");
	 * ExtentReportManager.logJSONDetails(response.getBody().prettyPrint()); }
	 */

}
