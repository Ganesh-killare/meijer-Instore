package requestbuilder;

import java.util.Random;
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

import com.github.javafaker.Faker;

import base.BaseClass;
import base.SessionIdManager;
import utilities.Utils;

public class ByPass {
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
			Element byPassScreenRequest = doc.createElement("ByPassScreenRequest");
			doc.appendChild(byPassScreenRequest);

			appendElementWithValue(doc, byPassScreenRequest, "POSID", Utils.getPOSID());
			appendElementWithValue(doc, byPassScreenRequest, "APPID", "01");
			appendElementWithValue(doc, byPassScreenRequest, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, byPassScreenRequest, "ADSDKSpecVer", Utils.getAESDKSpec());
			appendElementWithValue(doc, byPassScreenRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, byPassScreenRequest, "ByPassReason", "Idle");
			appendElementWithValue(doc, byPassScreenRequest, "ByPassOptions", "0");
			appendElementWithValue(doc, byPassScreenRequest, "ClerkID", "000000563");
			appendElementWithValue(doc, byPassScreenRequest, "LanguageIndicator", "00");

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

	public static String Option0() {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			RequestUtils.setTagValue(document, "ByPassOptions", "0");

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String Option1() {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			RequestUtils.setTagValue(document, "ByPassOptions", "1");

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String Option2() {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Modify specific tag values
			RequestUtils.setTagValue(document, "ByPassOptions", "2");

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String Option(int option) {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			String options = String.valueOf(option);

			// Modify specific tag values
			RequestUtils.setTagValue(document, "ByPassOptions", options);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String Random() {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Create an instance of Random class
			Random random = new Random();

			// Generate a random number that could be 0, 1, or 2
			int randomNumber = random.nextInt(100); // Generates 0, 1, or 2

			// Adjust the random number to be either 0 or 2, excluding 1
			if (randomNumber % 2 == 0) {
				randomNumber = 2; // Change 1 to 2
			} else {
				randomNumber = 0;
			}

			// Print the generated number
			// System.out.println(" ByPass Option " + randomNumber);

			String randomNumberAsString = String.valueOf(randomNumber);
			// Modify specific tag values
			RequestUtils.setTagValue(document, "ByPassOptions", randomNumberAsString);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String pureRandom() {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Create an instance of Random class
			Random random = new Random();

			// Generate a random number that could be 0, 1, or 2
			int randomNumber = random.nextInt(3); // Generates 0, 1, or 2

			// Print the generated number
			// System.out.println(" ByPass Option " + randomNumber);

			String randomNumberAsString = String.valueOf(randomNumber);
			// Modify specific tag values
			RequestUtils.setTagValue(document, "ByPassOptions", randomNumberAsString);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
