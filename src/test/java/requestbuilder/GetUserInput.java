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
import org.w3c.dom.Text;

public class GetUserInput {

	public static String ALUScreen01Request() {
		try {
			Document requestDocument = ALUScreen01();
			return documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String ALUScreen02Request() {
		try {
			Document requestDocument = ALUScreen02();
			return documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String ALUScreen03Request() {
		try {
			Document requestDocument = ALUScreen03();
			return documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String ALUScreen04Request() {
		try {
			Document requestDocument = ALUScreen04();
			return documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String ALUScreen05Request() {
		try {
			Document requestDocument = ALUScreen05();
			return documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String MperkNumberRequest() {
		try {
			Document requestDocument = MperkNumber();
			return documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String InvalidMperkNumberRequest() {
		try {
			Document requestDocument = InvalidMperkNumber();
			return documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String MperkPINRequest() {
		try {
			Document requestDocument = MperkPIN();
			return documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String InvalidMperkPINRequest() {
		try {
			Document requestDocument = InvalidMperkPIN();
			return documentToString(requestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document ALUScreen01() {
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
			appendElementWithValue(doc, showScreenRequest, "CCTID", "01");
			appendElementWithValue(doc, showScreenRequest, "Header", "Account Lookup");
			appendElementWithValue(doc, showScreenRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, showScreenRequest, "MessageLine1", "Would you like to use your");
			appendElementWithValue(doc, showScreenRequest, "MessageLine2", "Meijer credit card for this transaction?");
			appendElementWithValue(doc, showScreenRequest, "TimeOut", "150");
			appendElementWithValue(doc, showScreenRequest, "LineDisplay", "1");
			appendElementWithValue(doc, showScreenRequest, "SessionId", "12345");

			// ButtonMsg element and its children
			Element buttonMsg = doc.createElement("ButtonMsg");
			showScreenRequest.appendChild(buttonMsg);

			appendElementWithValue(doc, buttonMsg, "ButtonSizeFlag", "00");
			appendElementWithValue(doc, buttonMsg, "Button1Text", "YES");
			appendElementWithValue(doc, buttonMsg, "Button2Text", "NO");

			// Other elements with empty values
			appendElementWithValue(doc, showScreenRequest, "MessageLine3", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine4", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine5", "");
			appendElementWithValue(doc, showScreenRequest, "MessageLine6", "");
			appendElementWithValue(doc, showScreenRequest, "ShowCheckBox", "0");
			appendElementWithValue(doc, showScreenRequest, "CheckBoxText", "");
			appendElementWithValue(doc, showScreenRequest, "ActivityFlag", "0");

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document ALUScreen02() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: GetUserInputRequest
			Element getUserInputRequest = doc.createElement("GetUserInputRequest");
			doc.appendChild(getUserInputRequest);

			// Add child elements for GetUserInputRequest
			appendElementWithValue(doc, getUserInputRequest, "POSID", "01");
			appendElementWithValue(doc, getUserInputRequest, "APPID", "01");
			appendElementWithValue(doc, getUserInputRequest, "CCTID", "01");
			appendElementWithValue(doc, getUserInputRequest, "Type", "4");
			appendElementWithValue(doc, getUserInputRequest, "HeaderText", "Account Lookup");
			appendElementWithValue(doc, getUserInputRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, getUserInputRequest, "MessageLine1", "Type last 4 digit of your social");
			appendElementWithValue(doc, getUserInputRequest, "MessageLine2", "security number & press ENTER.");
			appendElementWithValue(doc, getUserInputRequest, "MinInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "MaxInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "Format", "1");
			appendElementWithValue(doc, getUserInputRequest, "ScreenTimeOut", "150");
			appendElementWithValue(doc, getUserInputRequest, "AllowCancel", "1");
			appendElementWithValue(doc, getUserInputRequest, "SessionId", "");

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document ALUScreen03() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: GetUserInputRequest
			Element getUserInputRequest = doc.createElement("GetUserInputRequest");
			doc.appendChild(getUserInputRequest);

			// Add child elements for GetUserInputRequest
			appendElementWithValue(doc, getUserInputRequest, "POSID", "01");
			appendElementWithValue(doc, getUserInputRequest, "APPID", "01");
			appendElementWithValue(doc, getUserInputRequest, "CCTID", "01");
			appendElementWithValue(doc, getUserInputRequest, "Type", "4");
			appendElementWithValue(doc, getUserInputRequest, "HeaderText", "Account Lookup");
			appendElementWithValue(doc, getUserInputRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, getUserInputRequest, "MessageLine1", "Confirm last 4 digit of your social");
			appendElementWithValue(doc, getUserInputRequest, "MessageLine2", "security number & press ENTER.");
			appendElementWithValue(doc, getUserInputRequest, "MinInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "MaxInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "Format", "1");
			appendElementWithValue(doc, getUserInputRequest, "ScreenTimeOut", "150");
			appendElementWithValue(doc, getUserInputRequest, "AllowCancel", "1");
			appendElementWithValue(doc, getUserInputRequest, "SessionId", "");

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document ALUScreen04() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: GetUserInputRequest
			Element getUserInputRequest = doc.createElement("GetUserInputRequest");
			doc.appendChild(getUserInputRequest);

			// Add child elements for GetUserInputRequest
			appendElementWithValue(doc, getUserInputRequest, "POSID", "01");
			appendElementWithValue(doc, getUserInputRequest, "APPID", "01");
			appendElementWithValue(doc, getUserInputRequest, "CCTID", "01");
			appendElementWithValue(doc, getUserInputRequest, "Type", "5");
			appendElementWithValue(doc, getUserInputRequest, "HeaderText", "Account Lookup");
			appendElementWithValue(doc, getUserInputRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, getUserInputRequest, "MessageLine1", "Type your phone number");
			appendElementWithValue(doc, getUserInputRequest, "MessageLine2", "& press ENTER.");
			appendElementWithValue(doc, getUserInputRequest, "MinInputLimit", "10");
			appendElementWithValue(doc, getUserInputRequest, "MaxInputLimit", "10");
			appendElementWithValue(doc, getUserInputRequest, "Format", "0");
			appendElementWithValue(doc, getUserInputRequest, "ScreenTimeOut", "150");
			appendElementWithValue(doc, getUserInputRequest, "AllowCancel", "1");
			appendElementWithValue(doc, getUserInputRequest, "SessionId", "");

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document ALUScreen05() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: GetUserInputRequest
			Element getUserInputRequest = doc.createElement("GetUserInputRequest");
			doc.appendChild(getUserInputRequest);

			// Add child elements for GetUserInputRequest
			appendElementWithValue(doc, getUserInputRequest, "POSID", "01");
			appendElementWithValue(doc, getUserInputRequest, "APPID", "01");
			appendElementWithValue(doc, getUserInputRequest, "CCTID", "01");
			appendElementWithValue(doc, getUserInputRequest, "Type", "4");
			appendElementWithValue(doc, getUserInputRequest, "HeaderText", "Account Lookup");
			appendElementWithValue(doc, getUserInputRequest, "ADSDKSpecVer", "6.10");
			appendElementWithValue(doc, getUserInputRequest, "MessageLine1", "Type last 4 digit of your social");
			appendElementWithValue(doc, getUserInputRequest, "MessageLine2", "security number & press ENTER.");
			appendElementWithValue(doc, getUserInputRequest, "FooterText",
					"Your entries did not match. Please try again.");
			appendElementWithValue(doc, getUserInputRequest, "MinInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "MaxInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "Format", "1");
			appendElementWithValue(doc, getUserInputRequest, "ScreenTimeOut", "150");
			appendElementWithValue(doc, getUserInputRequest, "AllowCancel", "1");
			appendElementWithValue(doc, getUserInputRequest, "SessionId", "");

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document MperkNumber() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: GetUserInputRequest
			Element getUserInputRequest = doc.createElement("GetUserInputRequest");
			doc.appendChild(getUserInputRequest);

			// Add child elements for GetUserInputRequest
			  // Set values for each child element
            appendElementWithValue(doc, getUserInputRequest, "POSID", "20");
            appendElementWithValue(doc, getUserInputRequest, "APPID", "01");
            appendElementWithValue(doc, getUserInputRequest, "CCTID", "01");
            appendElementWithValue(doc, getUserInputRequest, "ADSDKSpecVer", "6.14.8");
            appendElementWithValue(doc, getUserInputRequest, "SessionId", "25");
            appendElementWithValue(doc, getUserInputRequest, "Type", "11");
            appendElementWithValue(doc, getUserInputRequest, "LanguageIndicator", "00");
            appendElementWithValue(doc, getUserInputRequest, "MinInputLimit", "10");
            appendElementWithValue(doc, getUserInputRequest, "MaxInputLimit", "10");
            appendElementWithValue(doc, getUserInputRequest, "BackgroundImage", "Customnumber1");
            appendElementWithValue(doc, getUserInputRequest, "Format", "1");
            appendElementWithValue(doc, getUserInputRequest, "ScreenTimeOut", "3600");
            appendElementWithValue(doc, getUserInputRequest, "AllowCancel", "1");
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document InvalidMperkNumber() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: GetUserInputRequest
			Element getUserInputRequest = doc.createElement("GetUserInputRequest");
			doc.appendChild(getUserInputRequest);

			// Add child elements for GetUserInputRequest
			appendElementWithValue(doc, getUserInputRequest, "POSID", "01");
			appendElementWithValue(doc, getUserInputRequest, "APPID", "01");
			appendElementWithValue(doc, getUserInputRequest, "CCTID", "01");
			appendElementWithValue(doc, getUserInputRequest, "Type", "11");
			appendElementWithValue(doc, getUserInputRequest, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, getUserInputRequest, "BackgroundImage", "Customnumber2");
			appendElementWithValue(doc, getUserInputRequest, "ButtonText1", "MperkYES");
			appendElementWithValue(doc, getUserInputRequest, "ButtonText2", "MperkNO");
			appendElementWithValue(doc, getUserInputRequest, "ButtonText3", "MperkSKIP");
			appendElementWithValue(doc, getUserInputRequest, "MinInputLimit", "10");
			appendElementWithValue(doc, getUserInputRequest, "MaxInputLimit", "10");
			appendElementWithValue(doc, getUserInputRequest, "Format", "1");
			appendElementWithValue(doc, getUserInputRequest, "ScreenTimeOut", "150");
			appendElementWithValue(doc, getUserInputRequest, "AllowCancel", "1");
			appendElementWithValue(doc, getUserInputRequest, "SessionId", "");

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document MperkPIN() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: GetUserInputRequest
			Element getUserInputRequest = doc.createElement("GetUserInputRequest");

			doc.appendChild(getUserInputRequest);
			// Add child elements for GetUserInputRequest
			appendElementWithValue(doc, getUserInputRequest, "POSID", "01");
			appendElementWithValue(doc, getUserInputRequest, "APPID", "01");
			appendElementWithValue(doc, getUserInputRequest, "CCTID", "01");
			appendElementWithValue(doc, getUserInputRequest, "Type", "12");
			appendElementWithValue(doc, getUserInputRequest, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, getUserInputRequest, "BackgroundImage", "CustomPIN1");
			appendElementWithValue(doc, getUserInputRequest, "ButtonText1", "MperkYES");
			appendElementWithValue(doc, getUserInputRequest, "ButtonText2", "MperkNO");
			appendElementWithValue(doc, getUserInputRequest, "ButtonText3", "MperkSKIP");
			appendElementWithValue(doc, getUserInputRequest, "MinInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "MaxInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "Format", "1");
			appendElementWithValue(doc, getUserInputRequest, "ScreenTimeOut", "150");
			appendElementWithValue(doc, getUserInputRequest, "AllowCancel", "1");
			appendElementWithValue(doc, getUserInputRequest, "SessionId", "");

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document InvalidMperkPIN() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element: GetUserInputRequest
			Element getUserInputRequest = doc.createElement("GetUserInputRequest");

			doc.appendChild(getUserInputRequest);// Add child elements for GetUserInputRequest
			appendElementWithValue(doc, getUserInputRequest, "POSID", "01");
			appendElementWithValue(doc, getUserInputRequest, "APPID", "01");
			appendElementWithValue(doc, getUserInputRequest, "CCTID", "01");
			appendElementWithValue(doc, getUserInputRequest, "Type", "12");
			appendElementWithValue(doc, getUserInputRequest, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, getUserInputRequest, "BackgroundImage", "CustomPIN2");
			appendElementWithValue(doc, getUserInputRequest, "ButtonText1", "MperkYES");
			appendElementWithValue(doc, getUserInputRequest, "ButtonText2", "MperkNO");
			appendElementWithValue(doc, getUserInputRequest, "ButtonText3", "MperkSKIP");
			appendElementWithValue(doc, getUserInputRequest, "MinInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "MaxInputLimit", "4");
			appendElementWithValue(doc, getUserInputRequest, "Format", "1");
			appendElementWithValue(doc, getUserInputRequest, "ScreenTimeOut", "150");
			appendElementWithValue(doc, getUserInputRequest, "AllowCancel", "1");
			appendElementWithValue(doc, getUserInputRequest, "SessionId", "");

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

}
