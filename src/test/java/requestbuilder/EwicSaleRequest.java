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

import base.SessionIdManager;
import utilities.Utils;

public class EwicSaleRequest {

	private static String formattedTime = Utils.generateDateTimeAndInvoice().get(0);
	private static String finalDate = Utils.generateDateTimeAndInvoice().get(1);
	private static String invoiceNumber = Utils.generateDateTimeAndInvoice().get(2);

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
            appendElementWithValue(doc, transRequestElement, "POSID", "11");
            appendElementWithValue(doc, transRequestElement, "APPID", "01");
            appendElementWithValue(doc, transRequestElement, "CCTID", "01");
            appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, transRequestElement, "SessionId", SessionIdManager.getCurrentSessionId());
            appendElementWithValue(doc, transRequestElement, "CardType", "EBW");
            appendElementWithValue(doc, transRequestElement, "PurchaserPresent", "Y");
            appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
            appendElementWithValue(doc, transRequestElement, "GiftPurchaseAuthIndicator", "N");
            appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
            appendElementWithValue(doc, transRequestElement, "CashBackFlag", Utils.getCashBackValue());

            // TransAmountDetails
            Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
            transRequestElement.appendChild(transAmountDetailsElement);
            appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", "4.43");
            appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", "8.08");
            appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", ".00");

            appendElementWithValue(doc, transRequestElement, "TransactionType", "24");
            appendElementWithValue(doc, transRequestElement, "CardPresent", "Y");
            appendElementWithValue(doc, transRequestElement, "InvoiceNumber", invoiceNumber);
            appendElementWithValue(doc, transRequestElement, "CardToken", "");
            appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "134452452452445");
            appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "134");
            appendElementWithValue(doc, transRequestElement, "ClerkID", "000000101");
            appendElementWithValue(doc, transRequestElement, "CurrencyCode", "840");
            appendElementWithValue(doc, transRequestElement, "TransactionDate", finalDate);
            appendElementWithValue(doc, transRequestElement, "TransactionTime", formattedTime);
            appendElementWithValue(doc, transRequestElement, "TipEligible", "0");
            appendElementWithValue(doc, transRequestElement, "AmountNoBar", "1");
            appendElementWithValue(doc, transRequestElement, "SignatureFlag", "N");
            appendElementWithValue(doc, transRequestElement, "AurusPayTicketNum", "");
            appendElementWithValue(doc, transRequestElement, "OrigAurusPayTicketNum", "");
            appendElementWithValue(doc, transRequestElement, "IssuerNumber", "");

            // UPCDetails
            Element upcDetailsElement = doc.createElement("UPCDetails");
            transRequestElement.appendChild(upcDetailsElement);

            // UPCItems
            Element upcItemsElement = doc.createElement("UPCItems");
            upcDetailsElement.appendChild(upcItemsElement);

            // UPCItem
            Element upcItemElement = doc.createElement("UPCItem");
            upcItemsElement.appendChild(upcItemElement);
            appendElementWithValue(doc, upcItemElement, "UPCIndicator", "0");
            appendElementWithValue(doc, upcItemElement, "UPCPLUData", "0000008441341768");
            appendElementWithValue(doc, upcItemElement, "UPCItemPrice", "000443");
            appendElementWithValue(doc, upcItemElement, "UPCQuantity", "00100");

            appendElementWithValue(doc, upcDetailsElement, "UPCCount", "1");

            appendElementWithValue(doc, transRequestElement, "PartialAllowed", "0");
            appendElementWithValue(doc, transRequestElement, "ShowResponse", Utils.getShowResponseValue());
            appendElementWithValue(doc, transRequestElement, "ECommerceIndicator", "N");
            appendElementWithValue(doc, transRequestElement, "POSType", "1");

            // BillingAddress
            Element billingAddressElement = doc.createElement("BillingAddress");
            transRequestElement.appendChild(billingAddressElement);
            appendElementWithValue(doc, billingAddressElement, "BillingZip", "48846");

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

	public static String EWIC_SALE_REQUEST(String CardToken, String CI, String CRM) {
		try {

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			setTagValue(document, "CardToken", CardToken);
			setTagValue(document, "CRMToken", CI);
			setTagValue(document, "CardIdentifier", CRM);
			
			
			// Convert the modified document back to a string
			return documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
