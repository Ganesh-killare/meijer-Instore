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

import utilities.DateUtilities;

public class FSARequest {

	static String formattedTime = DateUtilities.generateDateTimeAndInvoice().get(0);
	static String finalDate = DateUtilities.generateDateTimeAndInvoice().get(1);
	static String invoiceNumber = DateUtilities.generateDateTimeAndInvoice().get(2);

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

	public static String buildXMLRequest() {
		try {
			Document transRequestDocument = createSampleTransRequestDocument();

			// Convert the modified document back to a string
			return documentToString(transRequestDocument);
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
			appendElementWithValue(doc, transRequestElement, "SessionId", "12345");
			appendElementWithValue(doc, transRequestElement, "CardPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "CardType", "MCS");
			appendElementWithValue(doc, transRequestElement, "PurchaserPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "GiftPurchaseAuthIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
			 appendElementWithValue(doc, transRequestElement, "CRMToken", "");
			appendElementWithValue(doc, transRequestElement, "CashBackFlag", "1");

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
			appendElementWithValue(doc, transRequestElement, "ShowResponse", "0");
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

	private static String documentToString(Document document) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();

			// Set properties for pretty formatting without the XML declaration
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			// Remove unnecessary whitespace
			document.normalize();

			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(writer));

			// Remove empty lines between tags
			String result = writer.toString().replaceAll("(?m)^[ \t]*\r?\n", "");

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void setTagValue(Document document, String tagName, String newValue) {
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Element element = (Element) nodeList.item(0);
			element.setTextContent(newValue);
		}
	}

	public static String FSA_SALE_REQUEST(String CardToken, String CI, String CRM) {
		try {

			Faker faker = new Faker();

			double Prescription = faker.number().randomDouble(2, 00, 01);
			double VisionOptical = faker.number().randomDouble(2, 00, 01);
			double CoPayment = faker.number().randomDouble(2, 00, 01);
			double Dental = faker.number().randomDouble(2, 00, 01);

			// double Dental = AMT; // Adjusted to make the total 100.13

			double Famount = Prescription + VisionOptical + CoPayment + Dental;
			double Totalamount = Famount + 1.00;

			DecimalFormat df = new DecimalFormat("0.00");
			String formattedFamount = df.format(Famount);
			String formattedTotalamount = df.format(Totalamount);
			formattedTotalamount = "0.00";

			System.out.println("FSA AMOUNT : " + formattedFamount);
			System.out.println("Total AMOUNT : " + formattedTotalamount);

			String PrescriptionAmount = df.format(Prescription);
			String VisionOpticalAmount = df.format(VisionOptical);
			String CoPaymentAmount = df.format(CoPayment);
			String DentalAmount = df.format(Dental);

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			setTagValue(document, "CardToken", CardToken);
			setTagValue(document, "CRMToken", CRM);
			setTagValue(document, "CardIdentifier", CI);

			setTagValue(document, "TenderAmount", formattedTotalamount);
			setTagValue(document, "TransactionTotal", formattedTotalamount);
			setTagValue(document, "FSAAmount", formattedFamount);
			setTagValue(document, "HealthCareAmount", formattedFamount);
			setTagValue(document, "PrescriptionAmount", PrescriptionAmount);
			setTagValue(document, "VisionOpticalAmount", VisionOpticalAmount);
			setTagValue(document, "CoPaymentAmount", CoPaymentAmount);
			setTagValue(document, "DentalAmount", DentalAmount);
			setTagValue(document, "TaxAmount", "0.00");

			// Convert the modified document back to a string
			return documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String FSA_RW_SALE_REQUEST(String CardToken, String CI, String CRM) {
		try {
			// Amount settelement
			Faker faker = new Faker();

			double Prescription = faker.number().randomDouble(2, 01, 05); // Generates a random double between 10.00 and
																			// 50.00
			double VisionOptical = faker.number().randomDouble(2, 01, 05); // Generates a random double between 10.00
																			// and 50.00
			double CoPayment = faker.number().randomDouble(2, 01, 05); // Generates a random double between 50.00 and
																		// 100.00

			// double Dental = AMT; // Adjusted to make the total 100.13

			double Famount = Prescription + VisionOptical + CoPayment + Dental;
			double Totalamount = Famount + 5.00;

			DecimalFormat df = new DecimalFormat("0.00");
			String formattedFamount = df.format(Famount);
			String formattedTotalamount = df.format(Totalamount);

			System.out.println("FSA AMOUNT : " + formattedFamount);
			System.out.println("Total AMOUNT : " + formattedTotalamount);

			String PrescriptionAmount = df.format(Prescription);
			String VisionOpticalAmount = df.format(VisionOptical);
			String CoPaymentAmount = df.format(CoPayment);
			String DentalAmount = df.format(Dental);

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			setTagValue(document, "CardToken", CardToken);
			setTagValue(document, "CRMToken", CI);
			setTagValue(document, "CardIdentifier", CRM);
			setTagValue(document, "TransactionType", "02");

			setTagValue(document, "TenderAmount", formattedTotalamount);
			setTagValue(document, "TransactionTotal", formattedTotalamount);
			setTagValue(document, "FSAAmount", formattedFamount);
			setTagValue(document, "HealthCareAmount", formattedFamount);
			setTagValue(document, "PrescriptionAmount", PrescriptionAmount);
			setTagValue(document, "VisionOpticalAmount", VisionOpticalAmount);
			setTagValue(document, "CoPaymentAmount", CoPaymentAmount);
			setTagValue(document, "DentalAmount", DentalAmount);
			setTagValue(document, "TaxAmount", "0.00");

			// Convert the modified document back to a string
			return documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String FSA_SALE_REQUEST(String CardToken, String CI, String CRM, double AMT) {
		try {

			// Amount settelement

			double Prescription = 25.00;
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
			setTagValue(document, "CardToken", CardToken);
			setTagValue(document, "CRMToken", CI);
			setTagValue(document, "CardIdentifier", CRM);

			setTagValue(document, "TenderAmount", transactionTotal);
			setTagValue(document, "TransactionTotal", transactionTotal);
			setTagValue(document, "FSAAmount", FSAAmount);
			setTagValue(document, "HealthCareAmount", FSAAmount);
			setTagValue(document, "PrescriptionAmount", PrescriptionAmount);
			setTagValue(document, "VisionOpticalAmount", VisionOpticalAmount);
			setTagValue(document, "CoPaymentAmount", CoPaymentAmount);
			setTagValue(document, "DentalAmount", DentalAmount);
			setTagValue(document, "TaxAmount", "0.00");

			// Convert the modified document back to a string
			return documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
