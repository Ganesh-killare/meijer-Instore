package requestbuilder;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.github.javafaker.Faker;

import base.SessionIdManager;
import utilities.Utils;

public class FSARequest {

	static String formattedTime = Utils.generateDateTimeAndInvoice().get(0);
	static String finalDate = Utils.generateDateTimeAndInvoice().get(1);
	static String invoiceNumber = Utils.generateDateTimeAndInvoice().get(2);
	private static int currentValue = 1;
	private static String EmptyValueFormat = "0.00";
	private static int VoidCount = 1;

	// amount genaration

	// Transaction Amount
	static DecimalFormat df = new DecimalFormat("00.00");
	static double Prescription = ThreadLocalRandom.current().nextDouble(00.00, 06.00);
	static double VisionOptical = ThreadLocalRandom.current().nextDouble(00.00, 06.00);
	static double CoPayment = ThreadLocalRandom.current().nextDouble(00.00, 06.00);
	static double Dental = ThreadLocalRandom.current().nextDouble(00.00, 06.00);

	static double amount = Prescription + VisionOptical + CoPayment + Dental + 5.00;
	static double fsaamount = Prescription + VisionOptical + CoPayment + Dental;

	static String PrescriptionAmount = df.format(Prescription);
	static String VisionOpticalAmount = df.format(VisionOptical);
	static String CoPaymentAmount = df.format(CoPayment);
	static String DentalAmount = df.format(Dental);
	static String FSAtotal = df.format(fsaamount);

// Format the double to have exactly 2 decimal places

	static String transactionAmount = df.format(amount);

	public static int getNextSequentialValueForVoid() {
		int value = VoidCount;

		// Update the current value, reset if exceeding the maximum value (5 in this
		// case)
		VoidCount++;
		if (VoidCount > 5) {
			VoidCount = 1; // Reset to 1 if exceeding the max value
		}

		return value;
	}

	public static int getNextSequentialValue() {
		int value = currentValue;

		// Update the current value, reset if exceeding the maximum value (3 in this
		// case)
		currentValue++;
		if (currentValue > 5) {
			currentValue = 1; // Reset to 1 if exceeding the max value
		}

		return value;
	}

	public static String buildXMLRequest() {
		try {
			Document transRequestDocument = createSampleTransRequestDocument();

			// Convert the modified document back to a string
			return RequestUtils.documentToString(transRequestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document createSampleTransRequestDocument() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element
			Element transRequestElement = doc.createElement("TransRequest");
			doc.appendChild(transRequestElement);

			// Add child elements in the desired sequence
			appendElementWithValue(doc, transRequestElement, "POSID", "S00784R0100");
			appendElementWithValue(doc, transRequestElement, "APPID", "01");
			appendElementWithValue(doc, transRequestElement, "CCTID", "01");
			appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, transRequestElement, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, transRequestElement, "CardType", "MCS");
			appendElementWithValue(doc, transRequestElement, "PurchaserPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "GiftPurchaseAuthIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
			appendElementWithValue(doc, transRequestElement, "CRMToken", "");
			appendElementWithValue(doc, transRequestElement, "CashBackFlag", Utils.getCashBackValue());

			// TransAmountDetails
			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", transactionAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", transactionAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "FSAAmount", FSAtotal);
			appendElementWithValue(doc, transAmountDetailsElement, "HealthCareAmount", FSAtotal);
			appendElementWithValue(doc, transAmountDetailsElement, "PrescriptionAmount", PrescriptionAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "VisionOpticalAmount", VisionOpticalAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "CoPaymentAmount", CoPaymentAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "DentalAmount", DentalAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", ".00");

			appendElementWithValue(doc, transRequestElement, "TransactionType", "01");
			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", invoiceNumber);
			appendElementWithValue(doc, transRequestElement, "CardToken", "");
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "12");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "12");
			appendElementWithValue(doc, transRequestElement, "ClerkID", "000000551");
			appendElementWithValue(doc, transRequestElement, "CurrencyCode", "840");
			appendElementWithValue(doc, transRequestElement, "TransactionDate", finalDate);
			appendElementWithValue(doc, transRequestElement, "TransactionTime", formattedTime);
			appendElementWithValue(doc, transRequestElement, "TipEligible", "0");
			appendElementWithValue(doc, transRequestElement, "AmountNoBar", "1");
			appendElementWithValue(doc, transRequestElement, "SignatureFlag", "N");
			appendElementWithValue(doc, transRequestElement, "OrigAurusPayTicketNum", "");
			appendElementWithValue(doc, transRequestElement, "OrigTransactionIdentifier", "");
			appendElementWithValue(doc, transRequestElement, "PartialAllowed", "0");
			appendElementWithValue(doc, transRequestElement, "ShowResponse", Utils.getShowResponseValue());
			appendElementWithValue(doc, transRequestElement, "ECommerceIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "POSType", "1");

			Element ecommInfoElement = doc.createElement("ECOMMInfo");
			transRequestElement.appendChild(ecommInfoElement);
			appendElementWithValue(doc, ecommInfoElement, "CardIdentifier", null);

			// BillingAddress
			Element billingAddressElement = doc.createElement("BillingAddress");
			transRequestElement.appendChild(billingAddressElement);
			appendElementWithValue(doc, billingAddressElement, "BillingZip", "49544");

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void appendElementWithValue(Document doc, Element parentElement, String tagName,
			String textContent) {
		Element element = doc.createElement(tagName);

		if (textContent != null) {
			element.appendChild(doc.createTextNode(textContent));
		} else {
			// Explicitly create an empty text node and append it
			Text emptyTextNode = doc.createTextNode("");
			element.appendChild(emptyTextNode);
		}

		parentElement.appendChild(element);
	}

	public static String FSA_SALE_REQUEST(String CardToken, String CI, String CRM) {
		try {
			Faker faker = new Faker();
			// Generate random amounts as integers (representing cents)
			int prescription = faker.number().numberBetween(1, 5); // Range 0 to 500 inclusive
			int visionOptical = faker.number().numberBetween(1, 5);
			int coPayment = faker.number().numberBetween(1, 5);
			int dental = faker.number().numberBetween(1, 5);

			// Calculate total amount as integer
			int totalAmountInCents = prescription + visionOptical + coPayment + dental;
			// int finalAmountInCents = totalAmountInCents; // Add 100 cents (1.00)

			// Format amounts for output
			DecimalFormat df = new DecimalFormat("0.00");
			String formattedPrescription = df.format(prescription);
			String formattedVisionOptical = df.format(visionOptical);
			String formattedCoPayment = df.format(coPayment);
			String formattedDental = df.format(dental);
			String formattedTotalAmount = null;
			String formattedFinalAmount = null;

			int decVer = getNextSequentialValue();
			switch (decVer) {
			case 1:
				formattedTotalAmount = df.format(totalAmountInCents);
				formattedFinalAmount = df.format(totalAmountInCents);
				// For all Amounts are 0.00
				formattedVisionOptical = EmptyValueFormat;
				formattedCoPayment = EmptyValueFormat;
				formattedDental = EmptyValueFormat;
				break;

			case 2:
				formattedTotalAmount = df.format(totalAmountInCents);
				formattedFinalAmount = EmptyValueFormat;
				formattedPrescription = EmptyValueFormat;
				// For all Amounts are 0.00
				formattedVisionOptical = EmptyValueFormat;
				formattedCoPayment = EmptyValueFormat;
				formattedDental = EmptyValueFormat;
				break;

			case 3:

				formattedTotalAmount = df.format(totalAmountInCents);
				formattedFinalAmount = df.format(totalAmountInCents);
				break;

			case 4:
				formattedTotalAmount = EmptyValueFormat;
				formattedFinalAmount = df.format(totalAmountInCents);
				// For all Amounts are 0.00
				formattedVisionOptical = EmptyValueFormat;
				formattedCoPayment = EmptyValueFormat;
				formattedDental = EmptyValueFormat;
				break;

			default:
				String Sale_Request = CD_Sale_Request.CD_SALE_REQUEST(CardToken, CI, CRM);
				return Sale_Request;

			}

			// Print results

			System.out.println("Total Amount: " + formattedTotalAmount);
			System.out.println("FSA Amount: " + formattedFinalAmount);
			System.out.println("PrescriptionAmount  :  " + formattedPrescription);
			System.out.println("VisionOpticalAmount  :  " + formattedVisionOptical);
			System.out.println("CoPaymentAmount  :  " + formattedCoPayment);
			System.out.println("DentalAmount  :  " + formattedDental);

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			if ((CRM != null || CI != null) && formattedTotalAmount.equalsIgnoreCase("0.00")) {
				formattedTotalAmount = formattedFinalAmount;
				System.out.println("Updated Total Amount is: " + formattedTotalAmount);
			}

			// Modify specific tag values
			RequestUtils.setTagValue(document, "CardToken", CardToken);
			RequestUtils.setTagValue(document, "CRMToken", CRM);
			RequestUtils.setTagValue(document, "CardIdentifier", CI);

			RequestUtils.setTagValue(document, "TenderAmount", formattedTotalAmount);
			RequestUtils.setTagValue(document, "TransactionTotal", formattedTotalAmount);
			RequestUtils.setTagValue(document, "FSAAmount", formattedFinalAmount);
			RequestUtils.setTagValue(document, "HealthCareAmount", formattedFinalAmount);
			RequestUtils.setTagValue(document, "PrescriptionAmount", formattedPrescription);
			RequestUtils.setTagValue(document, "VisionOpticalAmount", formattedVisionOptical);
			RequestUtils.setTagValue(document, "CoPaymentAmount", formattedCoPayment);
			RequestUtils.setTagValue(document, "DentalAmount", formattedDental);
			RequestUtils.setTagValue(document, "TaxAmount", "0.23");

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String FSA_RW_SALE_REQUEST(String CardToken, String CI, String CRM) {
		try {

			Faker faker = new Faker();
			// Generate random amounts as integers (representing cents)
			int prescription = faker.number().numberBetween(1, 5); // Range 0 to 500 inclusive
			int visionOptical = faker.number().numberBetween(1, 5);
			int coPayment = faker.number().numberBetween(1, 5);
			int dental = faker.number().numberBetween(1, 5);

			// Calculate total amount as integer
			int totalAmountInCents = prescription + visionOptical + coPayment + dental;
			// int finalAmountInCents = totalAmountInCents; // Add 100 cents (1.00)

			// Format amounts for output
			DecimalFormat df = new DecimalFormat("0.00");
			String formattedPrescription = df.format(prescription);
			String formattedVisionOptical = df.format(visionOptical);
			String formattedCoPayment = df.format(coPayment);
			String formattedDental = df.format(dental);
			String formattedTotalAmount;
			String formattedFinalAmount;

			/*
			 * formattedTotalAmount = df.format(totalAmountInCents); formattedFinalAmount =
			 * df.format(totalAmountInCents) ;
			 */

			// Determine if the visionOptical amount is even or odd

			int decVer = getNextSequentialValue();
			switch (decVer) {
			case 1:
				formattedTotalAmount = df.format(totalAmountInCents);
				formattedFinalAmount = df.format(totalAmountInCents);
				// For all Amounts are 0.00
				formattedVisionOptical = EmptyValueFormat;
				formattedCoPayment = EmptyValueFormat;
				formattedDental = EmptyValueFormat;
				break;

			case 2:
				formattedTotalAmount = df.format(totalAmountInCents);
				formattedFinalAmount = EmptyValueFormat;
				formattedPrescription = EmptyValueFormat;
				// For all Amounts are 0.00
				formattedVisionOptical = EmptyValueFormat;
				formattedCoPayment = EmptyValueFormat;
				formattedDental = EmptyValueFormat;
				break;

			case 3:

				formattedTotalAmount = df.format(totalAmountInCents);
				formattedFinalAmount = df.format(totalAmountInCents);
				break;

			case 4:
				formattedTotalAmount = EmptyValueFormat;
				formattedFinalAmount = df.format(totalAmountInCents);
				// For all Amounts are 0.00
				formattedVisionOptical = EmptyValueFormat;
				formattedCoPayment = EmptyValueFormat;
				formattedDental = EmptyValueFormat;
				break;

			default:

				String Sale_Request = CD_Sale_Request.CD_RW_SALE_REQUEST(CardToken, CI, CRM);
				return Sale_Request;

			}

			/*
			 * formattedPrescription = ""; formattedTotalAmount = ""; formattedVisionOptical
			 * = ""; formattedCoPayment = ""; formattedDental = "";
			 */

			// Print results

			System.out.println("Total Amount: " + formattedTotalAmount);
			System.out.println("FSA Amount: " + formattedFinalAmount);
			System.out.println("PrescriptionAmount  :  " + formattedPrescription);
			System.out.println("VisionOpticalAmount  :  " + formattedVisionOptical);
			System.out.println("CoPaymentAmount  :  " + formattedCoPayment);
			System.out.println("DentalAmount  :  " + formattedDental);
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			if ((CRM != null || CI != null) && formattedTotalAmount.equalsIgnoreCase("0.00")) {
				formattedTotalAmount = formattedFinalAmount;
				System.out.println("Updated Total Amount is: " + formattedTotalAmount);
			}

			// Modify specific tag values
			RequestUtils.setTagValue(document, "CardToken", CardToken);
			RequestUtils.setTagValue(document, "CRMToken", CRM);
			RequestUtils.setTagValue(document, "CardIdentifier", CI);

			RequestUtils.setTagValue(document, "TransactionType", "02");

			RequestUtils.setTagValue(document, "TenderAmount", formattedTotalAmount);
			RequestUtils.setTagValue(document, "TransactionTotal", formattedTotalAmount);
			RequestUtils.setTagValue(document, "FSAAmount", formattedFinalAmount);
			RequestUtils.setTagValue(document, "HealthCareAmount", formattedFinalAmount);
			RequestUtils.setTagValue(document, "PrescriptionAmount", formattedPrescription);
			RequestUtils.setTagValue(document, "VisionOpticalAmount", formattedVisionOptical);
			RequestUtils.setTagValue(document, "CoPaymentAmount", formattedCoPayment);
			RequestUtils.setTagValue(document, "DentalAmount", formattedDental);
			RequestUtils.setTagValue(document, "TaxAmount", "0.23");

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String FSA_VOID_REQUEST(String transID, String AuruspayTicketNum, String amount) {
		try {

			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			RequestUtils.setTagValue(document, "TenderAmount", amount);
			RequestUtils.setTagValue(document, "TransactionTotal", amount);
			RequestUtils.setTagValue(document, "FSAAmount", amount);
			RequestUtils.setTagValue(document, "HealthCareAmount", amount);

			int decVer = getNextSequentialValueForVoid();
			switch (decVer) {
			case 1:
				RequestUtils.setTagValue(document, "PrescriptionAmount", amount);
				RequestUtils.setTagValue(document, "VisionOpticalAmount", EmptyValueFormat);
				RequestUtils.setTagValue(document, "CoPaymentAmount", EmptyValueFormat);
				RequestUtils.setTagValue(document, "DentalAmount", EmptyValueFormat);
				break;

			case 2:
				RequestUtils.setTagValue(document, "PrescriptionAmount", EmptyValueFormat);
				RequestUtils.setTagValue(document, "VisionOpticalAmount", amount);
				RequestUtils.setTagValue(document, "CoPaymentAmount", EmptyValueFormat);
				RequestUtils.setTagValue(document, "DentalAmount", EmptyValueFormat);
				break;

			case 3:
				RequestUtils.setTagValue(document, "PrescriptionAmount", EmptyValueFormat);
				RequestUtils.setTagValue(document, "VisionOpticalAmount", EmptyValueFormat);
				RequestUtils.setTagValue(document, "CoPaymentAmount", amount);
				RequestUtils.setTagValue(document, "DentalAmount", EmptyValueFormat);
				break;

			default:

				RequestUtils.setTagValue(document, "PrescriptionAmount", EmptyValueFormat);
				RequestUtils.setTagValue(document, "VisionOpticalAmount", EmptyValueFormat);
				RequestUtils.setTagValue(document, "CoPaymentAmount", EmptyValueFormat);
				RequestUtils.setTagValue(document, "DentalAmount", amount);
				break;
			}

			RequestUtils.setTagValue(document, "TaxAmount", "0.23");
			RequestUtils.setTagValue(document, "OrigAurusPayTicketNum", AuruspayTicketNum);
			RequestUtils.setTagValue(document, "OrigTransactionIdentifier", transID);
			RequestUtils.setTagValue(document, "TransactionType", "06");

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String FSA_SALE_REQUEST(String CardToken, String CI, String CRM, double AMT, String TransType) {

		System.out.println(TransType);

		try {

			double Prescription = 20.00;
			double VisionOptical = 20.00;
			double CoPayment = 55.00;
			double Dental = AMT; // Adjusted to make the total 100.13

			double Famount = Prescription + VisionOptical + CoPayment + Dental;
			double Totalamount = Prescription + VisionOptical + CoPayment + Dental + 5.00;

			String PrescriptionAmount = df.format(Prescription);
			String VisionOpticalAmount = df.format(VisionOptical);
			String CoPaymentAmount = df.format(CoPayment);
			String DentalAmount = df.format(Dental);
			String FSAAmount = df.format(Famount);
			String transactionTotal = df.format(Totalamount);

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			RequestUtils.setTagValue(document, "CardToken", CardToken);
			RequestUtils.setTagValue(document, "CRMToken", CRM);
			RequestUtils.setTagValue(document, "CardIdentifier", CI);
			RequestUtils.setTagValue(document, "TransactionType", TransType);
			RequestUtils.setTagValue(document, "TenderAmount", transactionTotal);
			RequestUtils.setTagValue(document, "TransactionTotal", transactionTotal);
			RequestUtils.setTagValue(document, "FSAAmount", FSAAmount);
			RequestUtils.setTagValue(document, "HealthCareAmount", FSAAmount);
			RequestUtils.setTagValue(document, "PrescriptionAmount", PrescriptionAmount);
			RequestUtils.setTagValue(document, "VisionOpticalAmount", VisionOpticalAmount);
			RequestUtils.setTagValue(document, "CoPaymentAmount", CoPaymentAmount);
			RequestUtils.setTagValue(document, "DentalAmount", DentalAmount);
			RequestUtils.setTagValue(document, "TaxAmount", "0.00");

			// Convert the modified document back to a string

			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
