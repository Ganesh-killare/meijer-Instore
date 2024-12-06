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

public class Signature {
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

			// Create root element <SignatureRequest>
			Element signatureRequestElement = doc.createElement("SignatureRequest");
			doc.appendChild(signatureRequestElement);

			// Add child elements to <SignatureRequest>
			appendElementWithValue(doc, signatureRequestElement, "POSID", "7");
			appendElementWithValue(doc, signatureRequestElement, "APPID", "01");
			appendElementWithValue(doc, signatureRequestElement, "CCTID", "01");
			appendElementWithValue(doc, signatureRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, signatureRequestElement, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, signatureRequestElement, "TransactionIdentifier", "");
			appendElementWithValue(doc, signatureRequestElement, "SignUploadFlag", "Y");
			appendElementWithValue(doc, signatureRequestElement, "SignatureData", "");
			appendElementWithValue(doc, signatureRequestElement, "HeaderLine", "Please Sign");
			appendElementWithValue(doc, signatureRequestElement, "CustomDisplay1", "Please sign & select ACCEPT.");
			appendElementWithValue(doc, signatureRequestElement, "CustomDisplay2", "");
			appendElementWithValue(doc, signatureRequestElement, "SignatureType", "02");
			appendElementWithValue(doc, signatureRequestElement, "ECAFlag", "1");

			// Add <ButtonMsg> element with its children
			Element buttonMsgElement = doc.createElement("ButtonMsg");
			signatureRequestElement.appendChild(buttonMsgElement);
			appendElementWithValue(doc, buttonMsgElement, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsgElement, "Button1Text", "Accept");
			appendElementWithValue(doc, buttonMsgElement, "Button2Text", "Refuse");
			appendElementWithValue(doc, buttonMsgElement, "Button3Text", "Clear");

			// Add <ListData> element with <Paragraph> child
			Element listDataElement = doc.createElement("ListData");
			signatureRequestElement.appendChild(listDataElement);
			appendElementWithValue(doc, listDataElement, "Paragraph",
					"I authorize this check for $118.77. If the check is not valid, I accept a return fee of $25.00 by electronic fund transfers or drafts from my account.");

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

	

	public static String Request(String transID) {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			RequestUtils.setTagValue(document, "TransactionIdentifier", transID);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String Request() {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			RequestUtils.setTagValue(document, "SignUploadFlag", "N");
			RequestUtils.setTagValue(document, "ECAFlag", "0");

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
