package requestbuilder;

import java.io.StringWriter;
import java.util.Random;

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

public class AccountLookUp {

	private static String formattedTime = DateUtilities.generateDateTimeAndInvoice().get(0);
	private static String finalDate = DateUtilities.generateDateTimeAndInvoice().get(1);

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
			Element GCBRequestElement = doc.createElement("GetAccountLookupRequest");
			doc.appendChild(GCBRequestElement);

			// Add child elements from GetAccountLookupRequest
			appendElementWithValue(doc, GCBRequestElement, "POSID", "11");
			appendElementWithValue(doc, GCBRequestElement, "APPID", "01");
			appendElementWithValue(doc, GCBRequestElement, "CCTID", "01");
			appendElementWithValue(doc, GCBRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, GCBRequestElement, "SessionId", "50");
			appendElementWithValue(doc, GCBRequestElement, "HeaderMessage", "");
			appendElementWithValue(doc, GCBRequestElement, "MessageLine1", "");
			appendElementWithValue(doc, GCBRequestElement, "MessageLine2", "");
			appendElementWithValue(doc, GCBRequestElement, "LookUpFlag", "11");
			appendElementWithValue(doc, GCBRequestElement, "LookupType", "AC");
			appendElementWithValue(doc, GCBRequestElement, "TokenValidateFlag", "Y");
			appendElementWithValue(doc, GCBRequestElement, "LookupPhone", "");
			appendElementWithValue(doc, GCBRequestElement, "LookupSSN", "");
			appendElementWithValue(doc, GCBRequestElement, "LookupZIP", "");
			appendElementWithValue(doc, GCBRequestElement, "ProcessorToken", "");
			appendElementWithValue(doc, GCBRequestElement, "LookupAccount", "");
			appendElementWithValue(doc, GCBRequestElement, "IDData", "");
			appendElementWithValue(doc, GCBRequestElement, "DateOfBirth", "");
			appendElementWithValue(doc, GCBRequestElement, "SubTransType", "69");
			appendElementWithValue(doc, GCBRequestElement, "OrigTransactionIdentifier", "");
			appendElementWithValue(doc, GCBRequestElement, "TransactionDate", finalDate);
			appendElementWithValue(doc, GCBRequestElement, "TransactionTime", formattedTime);
			appendElementWithValue(doc, GCBRequestElement, "ShowResponse", "0");
			appendElementWithValue(doc, GCBRequestElement, "SubCardType", "");
			appendElementWithValue(doc, GCBRequestElement, "LanguageIndicator", "00");
			appendElementWithValue(doc, GCBRequestElement, "ClerkID", "001039151");
			appendElementWithValue(doc, GCBRequestElement, "TimeOut", "240");

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

	public static String withProcessorToken() {

		String ProcessorToken = "O1Y8eX+BMs2yRwwuTtW7wbQX+m2LLdNuOPGfid2qsTqE3Ov7Zr0cjcXFNebHFiJU";

		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			setTagValue(document, "ProcessorToken", ProcessorToken);

			// Convert the modified document back to a string
			return documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String withSSNandPhoneNumber() {

		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			Random random = new Random();
			int randomNumber = random.nextInt(10);

			// Modify specific tag values
			if (randomNumber % 2 == 0) {
				setTagValue(document, "LookupSSN", "4377");
				setTagValue(document, "LookupPhone", "6056910168");
			} else {
				setTagValue(document, "LookupSSN", "4032");
				setTagValue(document, "LookupPhone", "6056910025");
			}

			// Convert the modified document back to a string
			return documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
