package requestbuilder;

import java.io.StringWriter;
import base.POS_APIs;
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

import utilities.DateUtilities;

public class EBTRequest {

	private static String formattedTime = DateUtilities.generateDateTimeAndInvoice().get(0);
	private static String finalDate = DateUtilities.generateDateTimeAndInvoice().get(1);
	private static String invoiceNumber = DateUtilities.generateDateTimeAndInvoice().get(2);

	private static String buildXMLRequest() {
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
			appendElementWithValue(doc, transRequestElement, "POSID", "S00744R0011");
			appendElementWithValue(doc, transRequestElement, "APPID", "01");
			appendElementWithValue(doc, transRequestElement, "CCTID", "01");
			appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, transRequestElement, "SessionId", "12345");
			appendElementWithValue(doc, transRequestElement, "CardPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "CardType", "EBC");
			appendElementWithValue(doc, transRequestElement, "PurchaserPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "GiftPurchaseAuthIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
			appendElementWithValue(doc, transRequestElement, "CardExpiryDate", "1226");
			appendElementWithValue(doc, transRequestElement, "CashBackFlag", "1");

			// TransAmountDetails
			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", "10.00");
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", "10.00");
			appendElementWithValue(doc, transAmountDetailsElement, "EBTAmount", "10.00");

			appendElementWithValue(doc, transRequestElement, "TransactionType", "01");
			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", invoiceNumber);
			appendElementWithValue(doc, transRequestElement, "CardToken", "");
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "261");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "261");
			appendElementWithValue(doc, transRequestElement, "ClerkID", "001039151");
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

	public static String EBT_SALE_REQUEST(String CardToken, String CI, String CRM) {
		try {

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Random Amount generation

			String transactionAmount = POS_APIs.generateTransactionAmount();
			// String transactionAmount = "100.13";

			// Modify specific tag values
			setTagValue(document, "CardToken", CardToken);
			setTagValue(document, "CRMToken", CI);
			setTagValue(document, "CardIdentifier", CRM);
			setTagValue(document, "TransactionTotal", transactionAmount);
			setTagValue(document, "TenderAmount", transactionAmount);
			setTagValue(document, "EBTAmount", transactionAmount);

			// Convert the modified document back to a string
			return documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String EBT_RW_SALE_REQUEST(String CardToken, String CI, String CRM) {
		try {

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Random Amount generation

			String transactionAmount = POS_APIs.generateTransactionAmount();

			// Modify specific tag values
			setTagValue(document, "CardToken", CardToken);
			setTagValue(document, "CRMToken", CI);
			setTagValue(document, "CardIdentifier", CRM);
			setTagValue(document, "TransactionTotal", transactionAmount);
			setTagValue(document, "TenderAmount", transactionAmount);
			setTagValue(document, "EBTAmount", transactionAmount);
			setTagValue(document, "TransactionType", "02");

			// Convert the modified document back to a string
			return documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
