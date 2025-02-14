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

public class ShowScreen {

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

	public static String ALUScreen06Request() {
		try {
			Document requestDocument = ALUScreen06();
			return RequestUtils.documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String ALUScreen07Request() {
		try {
			Document requestDocument = ALUScreen07();
			return RequestUtils.documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String HostSuppliedMperksRequest() {
		try {
			Document requestDocument = HostSuppliedMperks();
			return RequestUtils.documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String DuplicatemperksRequest() {
		try {
			Document requestDocument = Duplicatemperks();
			return RequestUtils.documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String HighValuePromptRequest() {
		try {
			Document requestDocument = HighValuePrompt();
			return RequestUtils.documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String MULTICopounRequest() {
		try {
			Document requestDocument = MULTICopoun();
			return RequestUtils.documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String FirstUseDiscountRequest() {
		try {
			Document requestDocument = FirstUseDiscount();
			return RequestUtils.documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String showScreenRequest(String Amount) {
		try {

			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			RequestUtils.setTagValue(document, "MessageLine2", "$-" + Amount);
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String receiptOptionsRequest() {
		try {
			Document requestDocument = receiptOptions();
			return RequestUtils.documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document ALUScreen06() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: ShowScreenRequest
			Element showScreenRequest = doc.createElement("ShowScreenRequest");
			doc.appendChild(showScreenRequest);

			// Add child elements for ShowScreenRequest
			appendElementWithValue(doc, showScreenRequest, "POSID", Utils.getPOSID());
			appendElementWithValue(doc, showScreenRequest, "APPID", "01");
			appendElementWithValue(doc, showScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, showScreenRequest, "Header", "Account Lookup Failed");
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", Utils.getAESDKSpec());
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "Please use another");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "form of payment.");
			appendElementWithValue(doc, showScreenRequest, "TimeOut", "150");
			appendElementWithValue(doc, showScreenRequest, "LineDisplay", "1");
			appendElementWithValue(doc, showScreenRequest, "SessionId", SessionIdManager.getCurrentSessionId());

			// Add ButtonMsg element with child elements
			Element buttonMsg = doc.createElement("ButtonMsg");
			showScreenRequest.appendChild(buttonMsg);

			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "Button1Text", "Continue");

			// ActivityFlag, ShowCheckBox, CheckBoxText, MessageLine3-6 are omitted assuming
			// they are not required or remain empty as per your example

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document ALUScreen07() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: ShowScreenRequest
			// Create root element: ShowScreenRequest
			Element showScreenRequest = doc.createElement("ShowScreenRequest");
			doc.appendChild(showScreenRequest);

			// Add child elements for ShowScreenRequest
			appendElementWithValue(doc, showScreenRequest, "POSID", "01");
			appendElementWithValue(doc, showScreenRequest, "APPID", "01");
			appendElementWithValue(doc, showScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, showScreenRequest, "Header", "ID Info Does Not Match");
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "Please contact");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "+1(877)816-9410");
			appendElementWithValue(doc, showScreenRequest, "TimeOut", "150");
			appendElementWithValue(doc, showScreenRequest, "LineDisplay", "1");
			appendElementWithValue(doc, showScreenRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			// Add ButtonMsg element with child elements
			Element buttonMsg = doc.createElement("ButtonMsg");
			showScreenRequest.appendChild(buttonMsg);

			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "Button1Text", "Continue");

			// ActivityFlag, ShowCheckBox, CheckBoxText, MessageLine3-6 are omitted assuming
			// they are not required or remain empty as per your example

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document HostSuppliedMperks() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: ShowScreenRequest
			Element showScreenRequest = doc.createElement("ShowScreenRequest");
			doc.appendChild(showScreenRequest);

			// Add child elements for ShowScreenRequest
			appendElementWithValue(doc, showScreenRequest, "POSID", "01");
			appendElementWithValue(doc, showScreenRequest, "APPID", "01");
			appendElementWithValue(doc, showScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, showScreenRequest, "Header", "");
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "Would you like to apply your mPerks");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "Rewards for account XXX-XXX-3456");
			appendElementWithValue(doc, showScreenRequest, "MessageLine3", "to this transaction?");
			appendElementWithValue(doc, showScreenRequest, "TimeOut", "150");
			appendElementWithValue(doc, showScreenRequest, "LineDisplay", "1");
			appendElementWithValue(doc, showScreenRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			// Add ButtonMsg element with child elements
			Element buttonMsg = doc.createElement("ButtonMsg");
			showScreenRequest.appendChild(buttonMsg);

			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "Button1Text", "Yes");
			appendElementWithValue(doc, buttonMsg, "Button2Text", "No");

			// ActivityFlag, ShowCheckBox, CheckBoxText, MessageLine4-6 are omitted assuming
			// they are not required or remain empty as per your example

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document Duplicatemperks() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: ShowScreenRequest
			Element showScreenRequest = doc.createElement("ShowScreenRequest");
			doc.appendChild(showScreenRequest);

			// Add child elements for ShowScreenRequest
			appendElementWithValue(doc, showScreenRequest, "POSID", "01");
			appendElementWithValue(doc, showScreenRequest, "APPID", "01");
			appendElementWithValue(doc, showScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, showScreenRequest, "Header", "");
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "mperks Rewards applied");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "to account previously entered.");
			appendElementWithValue(doc, showScreenRequest, "TimeOut", "150");
			appendElementWithValue(doc, showScreenRequest, "LineDisplay", "1");
			appendElementWithValue(doc, showScreenRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			// Add ButtonMsg element with child elements
			Element buttonMsg = doc.createElement("ButtonMsg");
			showScreenRequest.appendChild(buttonMsg);

			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "Button1Text", "Yes");
			appendElementWithValue(doc, buttonMsg, "Button2Text", "No");

			// ActivityFlag, ShowCheckBox, CheckBoxText, MessageLine3-6 are omitted assuming
			// they are not required or remain empty as per your example

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document HighValuePrompt() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: ShowScreenRequest
			Element showScreenRequest = doc.createElement("ShowScreenRequest");
			doc.appendChild(showScreenRequest);

			// Add child elements for ShowScreenRequest
			appendElementWithValue(doc, showScreenRequest, "POSID", "01");
			appendElementWithValue(doc, showScreenRequest, "APPID", "01");
			appendElementWithValue(doc, showScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, showScreenRequest, "Header", "");
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "Ham Nahi de rahe tere ko Offer!");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "lena hai to le nahi to nikal");
			appendElementWithValue(doc, showScreenRequest, "MessageLine3", "Aur ha, Green button press karale.");
			appendElementWithValue(doc, showScreenRequest, "MessageLine4", "Expires : 12/32/1852");
			appendElementWithValue(doc, showScreenRequest, "TimeOut", "150");
			appendElementWithValue(doc, showScreenRequest, "LineDisplay", "1");
			appendElementWithValue(doc, showScreenRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			// Add ButtonMsg element with child elements
			Element buttonMsg = doc.createElement("ButtonMsg");
			showScreenRequest.appendChild(buttonMsg);

			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "Button1Text", "Yes");
			appendElementWithValue(doc, buttonMsg, "Button2Text", "No");

			// ActivityFlag, ShowCheckBox, CheckBoxText, MessageLine5-6 are omitted assuming
			// they are not required or remain empty as per your example

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document MULTICopoun() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create the ShowScreenRequest element
			Element showScreenRequest = doc.createElement("ShowScreenRequest");

			// Set values for each child element
			appendElementWithValue(doc, showScreenRequest, "POSID", "01");
			appendElementWithValue(doc, showScreenRequest, "APPID", "01");
			appendElementWithValue(doc, showScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", "6.12");
			appendElementWithValue(doc, showScreenRequest, "LanguageIndicator", "00");
			appendElementWithValue(doc, showScreenRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, showScreenRequest, "Header", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "Selet the coupon you would");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "like to apply to this transaction.");
			appendElementWithValue(doc, showScreenRequest, "MessageLine3", "Message3");
			appendElementWithValue(doc, showScreenRequest, "MessageLine4", "Message4");
			appendElementWithValue(doc, showScreenRequest, "MessageLine5", "Message5");
			appendElementWithValue(doc, showScreenRequest, "MessageLine6", "Message6");
			appendElementWithValue(doc, showScreenRequest, "ShowCheckBox", "0");
			appendElementWithValue(doc, showScreenRequest, "CheckBoxText", "");
			appendElementWithValue(doc, showScreenRequest, "ActivityFlag", "0");

			// Set ButtonMsg parameters
			Element buttonMsg = doc.createElement("ButtonMsg");
			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "ButtonDisplayType", "1");

			// Button 1 parameters
			appendElementWithValue(doc, buttonMsg, "Button1Text", "Yes");
			appendElementWithValue(doc, buttonMsg, "Button1Image", "MperkImg");
			appendElementWithValue(doc, buttonMsg, "Button1Label1", "$100.00 Off");
			appendElementWithValue(doc, buttonMsg, "Button1Label2", "Expires 20/09/2022");

			// Button 2 parameters
			appendElementWithValue(doc, buttonMsg, "Button2Text", "No");
			appendElementWithValue(doc, buttonMsg, "Button2Image", "CouponImg");
			appendElementWithValue(doc, buttonMsg, "Button2Label1", "$500.00 Off");
			appendElementWithValue(doc, buttonMsg, "Button2Label2", "Expires 20/09/2023");

			// Append ButtonMsg to ShowScreenRequest
			showScreenRequest.appendChild(buttonMsg);

			// Append the ShowScreenRequest element to the document or another parent
			// element
			doc.appendChild(showScreenRequest);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document FirstUseDiscount() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element showScreenRequest = doc.createElement("ShowScreenRequest");
			doc.appendChild(showScreenRequest);

			// Set values for each child element
			appendElementWithValue(doc, showScreenRequest, "POSID", "01");
			appendElementWithValue(doc, showScreenRequest, "APPID", "01");
			appendElementWithValue(doc, showScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, showScreenRequest, "Header", "");
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "Would you like to apply your");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "First use Discount");
			appendElementWithValue(doc, showScreenRequest, "MessageLine3", "to this transaction?");
			appendElementWithValue(doc, showScreenRequest, "MessageLine4", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine5", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine6", "");
			appendElementWithValue(doc, showScreenRequest, "ShowCheckBox", "0");
			appendElementWithValue(doc, showScreenRequest, "CheckBoxText", "");
			appendElementWithValue(doc, showScreenRequest, "ActivityFlag", "0");

			// Create and append ButtonMsg element
			Element buttonMsg = doc.createElement("ButtonMsg");
			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "Button1Text", "Yes");
			appendElementWithValue(doc, buttonMsg, "Button2Text", "No");
			showScreenRequest.appendChild(buttonMsg);

			appendElementWithValue(doc, showScreenRequest, "TimeOut", "150");
			appendElementWithValue(doc, showScreenRequest, "LineDisplay", "1");
			appendElementWithValue(doc, showScreenRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			return doc;
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

			// Create root element: ShowScreenRequest
			Element showScreenRequest = doc.createElement("ShowScreenRequest");
			doc.appendChild(showScreenRequest);

			// Add child elements in the desired sequence
			appendElementWithValue(doc, showScreenRequest, "POSID", "11");
			appendElementWithValue(doc, showScreenRequest, "APPID", "01");
			appendElementWithValue(doc, showScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", Utils.getAESDKSpec());
			appendElementWithValue(doc, showScreenRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, showScreenRequest, "LanguageIndicator", "");
			appendElementWithValue(doc, showScreenRequest, "Header", "Confirm Transaction");
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "$-10.00");
			appendElementWithValue(doc, showScreenRequest, "MessageLine3", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine4", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine5", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine6", "");
			appendElementWithValue(doc, showScreenRequest, "ShowCheckBox", "0");
			appendElementWithValue(doc, showScreenRequest, "CheckBoxText", "");
			appendElementWithValue(doc, showScreenRequest, "ActivityFlag", "0");

			// ButtonMsg
			Element buttonMsg = doc.createElement("ButtonMsg");
			showScreenRequest.appendChild(buttonMsg);
			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "Button1Text", "YES");
			appendElementWithValue(doc, buttonMsg, "Button2Text", "NO");
			appendElementWithValue(doc, buttonMsg, "Button3Text", "");

			appendElementWithValue(doc, showScreenRequest, "TimeOut", "600");

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document receiptOptions() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: ShowScreenRequest
			Element showScreenRequest = doc.createElement("ShowScreenRequest");
			doc.appendChild(showScreenRequest);

			// Add child elements for ShowScreenRequest
			appendElementWithValue(doc, showScreenRequest, "POSID", "01");
			appendElementWithValue(doc, showScreenRequest, "APPID", "01");
			appendElementWithValue(doc, showScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", Utils.getAESDKSpec());
			appendElementWithValue(doc, showScreenRequest, "SessionId", "128");
			appendElementWithValue(doc, showScreenRequest, "LanguageIndicator", "");
			appendElementWithValue(doc, showScreenRequest, "Header", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "What type of paper receipt would");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "Please select the receipt option.");
			appendElementWithValue(doc, showScreenRequest, "MessageLine3", "Would you like a short or full receipt?");
			appendElementWithValue(doc, showScreenRequest, "TimeOut", "600");

			// Add ButtonMsg element with child elements
			Element buttonMsg = doc.createElement("ButtonMsg");
			showScreenRequest.appendChild(buttonMsg);

			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "Button1Text", "Short Receipt");
			appendElementWithValue(doc, buttonMsg, "Button2Text", "Full Receipt");

			// Omit MessageLine4-6, ShowCheckBox, CheckBoxText, and ActivityFlag since
			// they're not required in your XML

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

}
