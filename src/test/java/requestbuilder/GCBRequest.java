package requestbuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
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

import base.BaseClass;
import base.SessionIdManager;
import utilities.Utils;

public class GCBRequest {

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
			Element GCBRequestElement = doc.createElement("GetCardBINRequest");
			doc.appendChild(GCBRequestElement);

			// Add child elements in the desired sequence
			appendElementWithValue(doc, GCBRequestElement, "POSID", Utils.getPOSID());
			appendElementWithValue(doc, GCBRequestElement, "APPID", "01");
			appendElementWithValue(doc, GCBRequestElement, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, GCBRequestElement, "ADSDKSpecVer", Utils.getAESDKSpec());
			appendElementWithValue(doc, GCBRequestElement, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, GCBRequestElement, "HeaderMessage", "Please Tap, Insert or Swipe");
			appendElementWithValue(doc, GCBRequestElement, "CashBackAmountPrompts", "10,20,399,400,401,No");
			appendElementWithValue(doc, GCBRequestElement, "LookUpFlag", "16");
			appendElementWithValue(doc, GCBRequestElement, "AllowKeyedEntry", "N");
			appendElementWithValue(doc, GCBRequestElement, "CashBackFlag", "3");
			appendElementWithValue(doc, GCBRequestElement, "EntrySource", "");
			appendElementWithValue(doc, GCBRequestElement, "PromptConfirmFlag", "1");
			appendElementWithValue(doc, GCBRequestElement, "ProcessingMode", "0");
			appendElementWithValue(doc, GCBRequestElement, "CVVFlag", "0");
			appendElementWithValue(doc, GCBRequestElement, "ClerkID", "000000596");

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

	public static String GCB_REQUEST(String AllowKeyed) {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			RequestUtils.setTagValue(document, "AllowKeyedEntry", AllowKeyed);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String GCB_REQUEST() {
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream("config.properties")) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Accessing properties
		String LookUpFlag = properties.getProperty("LookUpFlag");
		String AllowKeyedEntry = properties.getProperty("AllowKeyedEntry");
		String CashBackFlag = properties.getProperty("CashBackFlag");

		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			RequestUtils.setTagValue(document, "AllowKeyedEntry", AllowKeyedEntry);
			RequestUtils.setTagValue(document, "LookUpFlag", LookUpFlag);
			RequestUtils.setTagValue(document, "CashBackFlag", CashBackFlag);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
