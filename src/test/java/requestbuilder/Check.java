package requestbuilder;

import java.io.StringWriter;

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

import base.POS_APIs;
import utilities.Utils;

public class Check {
	private static String finalTime = Utils.generateDateTimeAndInvoice().get(0);
	private static String finalDate = Utils.generateDateTimeAndInvoice().get(1);
	private static String InvoiceNumber = Utils.generateDateTimeAndInvoice().get(2);

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

			// Create root element <TransRequest>
			Element transRequestElement = doc.createElement("TransRequest");
			doc.appendChild(transRequestElement);

			// Add child elements to <TransRequest>
			appendElementWithValue(doc, transRequestElement, "POSID", "985");
			appendElementWithValue(doc, transRequestElement, "APPID", "01");
			appendElementWithValue(doc, transRequestElement, "CCTID", "01");
			appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, transRequestElement, "CardPresent", ""); // Empty string if not specified
			appendElementWithValue(doc, transRequestElement, "PurchaserPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "GiftPurchaseAuthIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
			appendElementWithValue(doc, transRequestElement, "CashBackFlag", Utils.getCashBackValue());

			// Add <TransAmountDetails> element with its children
			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "CashBackAmount", "0.00");
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", "30.00");
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", "30.00");
			appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", ".00");

			appendElementWithValue(doc, transRequestElement, "TransactionType", "36");
			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", InvoiceNumber); // Empty string if not
																								// specified
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "78");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "78");
			appendElementWithValue(doc, transRequestElement, "ClerkID", "108488");
			appendElementWithValue(doc, transRequestElement, "CurrencyCode", "840");
			appendElementWithValue(doc, transRequestElement, "TransactionDate", finalDate); // Empty string if not
																							// specified
			appendElementWithValue(doc, transRequestElement, "TransactionTime", finalTime); // Empty string if not
																							// specified
			appendElementWithValue(doc, transRequestElement, "TipEligible", "0");
			appendElementWithValue(doc, transRequestElement, "AmountNoBar", "1");
			appendElementWithValue(doc, transRequestElement, "SignatureFlag", "N");
			appendElementWithValue(doc, transRequestElement, "OrigAurusPayTicketNum", ""); // Empty string if not
																							// specified
			appendElementWithValue(doc, transRequestElement, "OrigTransactionIdentifier", "");
			appendElementWithValue(doc, transRequestElement, "PartialAllowed", "0");
			appendElementWithValue(doc, transRequestElement, "ShowResponse", Utils.getShowResponseValue());
			appendElementWithValue(doc, transRequestElement, "ECommerceIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "POSType", "1");

			// Add <BillingAddress> element with <BillingZip> child
			Element billingAddressElement = doc.createElement("BillingAddress");
			transRequestElement.appendChild(billingAddressElement);
			appendElementWithValue(doc, billingAddressElement, "BillingZip", "49544");

			// Add <CheckInfo> element with its children
			Element checkInfoElement = doc.createElement("CheckInfo");
			transRequestElement.appendChild(checkInfoElement);
			appendElementWithValue(doc, checkInfoElement, "CheckEntryMode", "S");
			appendElementWithValue(doc, checkInfoElement, "DriverLicenseEntryMode", "M");
			appendElementWithValue(doc, checkInfoElement, "DriverLicenseData", "11112222");
			appendElementWithValue(doc, checkInfoElement, "DriverLicenseState", "TX");
			appendElementWithValue(doc, checkInfoElement, "FullMICR", "t031100649t222 670173768870o 2233");
			appendElementWithValue(doc, checkInfoElement, "CheckType", "P");

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

	public static String Request() {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
			String AMT = POS_APIs.generateTransactionAmount();
			// Modify specific tag values
			setTagValue(document, "TenderAmount", AMT);
			setTagValue(document, "TransactionTotal", AMT);

			// Convert the modified document back to a string
			return documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
