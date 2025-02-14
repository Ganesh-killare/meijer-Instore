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

public class CloseRequest {

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
			Element CloseTransactionRequest = doc.createElement("CloseTransactionRequest");
			doc.appendChild(CloseTransactionRequest);

			// Add child elements in the desired sequence
			appendElementWithValue(doc, CloseTransactionRequest, "POSID", Utils.getPOSID());
			appendElementWithValue(doc, CloseTransactionRequest, "APPID", "01");
			appendElementWithValue(doc, CloseTransactionRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, CloseTransactionRequest, "ADSDKSpecVer", Utils.getAESDKSpec());
			appendElementWithValue(doc, CloseTransactionRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, CloseTransactionRequest, "CloseReasonCode", "TRANSACTION_COMPLETE");
			appendElementWithValue(doc, CloseTransactionRequest, "OrigAurusPayTicketNum", "");
			appendElementWithValue(doc, CloseTransactionRequest, "OrigTransactionIdentifier", "");
			appendElementWithValue(doc, CloseTransactionRequest, "InvoiceNumber", invoiceNumber);
			appendElementWithValue(doc, CloseTransactionRequest, "TransactionTime", formattedTime);
			appendElementWithValue(doc, CloseTransactionRequest, "TransactionDate", finalDate);
			Element ecommInfoElement = doc.createElement("ECOMMInfo");
			CloseTransactionRequest.appendChild(ecommInfoElement);
			appendElementWithValue(doc, ecommInfoElement, "MerchantIdentifier", "111111111111"); // Set value to null
																									// for empty tag
			appendElementWithValue(doc, ecommInfoElement, "StoreId", "11111"); // Set value to null for empty tag
			appendElementWithValue(doc, ecommInfoElement, "TerminalId", "11111111"); // Set value to null for empty tag

			appendElementWithValue(doc, CloseTransactionRequest, "EndWIC", "0");
			appendElementWithValue(doc, CloseTransactionRequest, "ProcessingMode", "0");
			appendElementWithValue(doc, CloseTransactionRequest, "LanguageIndicator", "");
			appendElementWithValue(doc, CloseTransactionRequest, "ClerkID", "111");

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

	public static String CLOSE_REQUEST() {
		try {

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
