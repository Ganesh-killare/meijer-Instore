package requestbuilder;

import base.POS_APIs;
import base.SessionIdManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Properties;
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

import utilities.Utils;

public class PLC_Request {

	static String formattedTime = Utils.generateDateTimeAndInvoice().get(0);
	static String finalDate = Utils.generateDateTimeAndInvoice().get(1);
	static String invoiceNumber = Utils.generateDateTimeAndInvoice().get(2);

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
			appendElementWithValue(doc, transRequestElement, "CardPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "CardType", "VIC");
			appendElementWithValue(doc, transRequestElement, "PurchaserPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "GiftPurchaseAuthIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
			appendElementWithValue(doc, transRequestElement, "CashBackFlag", Utils.getCashBackValue());
			appendElementWithValue(doc, transRequestElement, "TransactionType", "01");
			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", invoiceNumber);
			appendElementWithValue(doc, transRequestElement, "CardExpiryDate", "");
			appendElementWithValue(doc, transRequestElement, "CardToken", "");
			appendElementWithValue(doc, transRequestElement, "CRMToken", "");
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "18");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "18");
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

			// Add nested elements
			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", null); // Set value to null for empty
																							// tag
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", null); // Set value to null for
																								// empty tag
			appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", ".00");

			Element ecommInfoElement = doc.createElement("ECOMMInfo");
			transRequestElement.appendChild(ecommInfoElement);
			appendElementWithValue(doc, ecommInfoElement, "CardIdentifier", null); // Set value to null for empty tag

			/*
			 * Element billingAddressElement = doc.createElement("BillingAddress");
			 * transRequestElement.appendChild(billingAddressElement);
			 * appendElementWithValue(doc, billingAddressElement, "BillingZip", "1325");
			 */

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

	

	public static String PLC_SaleRequest(String CardToken, String CI, String CRM) {

		try {

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			String transactionAmount = POS_APIs.generateTransactionAmount();
			// transactionAmount = "10.00";

			// Modify specific tag values
			RequestUtils.setTagValue(document, "CardToken", CardToken);
			RequestUtils.setTagValue(document, "CardIdentifier", CI);
			RequestUtils.setTagValue(document, "CRMToken", CRM);

			RequestUtils.setTagValue(document, "TransactionTotal", transactionAmount);
			RequestUtils.setTagValue(document, "TenderAmount", transactionAmount);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String PLC_RW_SaleRequest(String CardToken, String CI, String CRM) {

		try {

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Random Amount generation
			String transactionAmount = POS_APIs.generateTransactionAmount();
			// transactionAmount = "10.00";

			// Modify specific tag values
			// Modify specific tag values
			RequestUtils.setTagValue(document, "CardToken", CardToken);
			RequestUtils.setTagValue(document, "CardIdentifier", CI);
			RequestUtils.setTagValue(document, "CRMToken", CRM);
			RequestUtils.setTagValue(document, "TransactionTotal", transactionAmount);
			RequestUtils.setTagValue(document, "TenderAmount", transactionAmount);
			RequestUtils.setTagValue(document, "TransactionType", "02");

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String CDPC_SALE_REQUEST(String CardToken, String CI, String CRM, String amount) {
		try {

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			RequestUtils.setTagValue(document, "CardToken", CardToken);
			RequestUtils.setTagValue(document, "CRMToken", CI);
			RequestUtils.setTagValue(document, "CardIdentifier", CRM);
			RequestUtils.setTagValue(document, "TransactionTotal", amount);
			RequestUtils.setTagValue(document, "TenderAmount", amount);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
