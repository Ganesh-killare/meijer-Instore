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

import base.SessionIdManager;

public class ShowList {

	public static String buildXMLRequest() {
		try {
			Document transRequestDocument = createShowListRequestDocument();

			// Convert the modified document back to a string
			return RequestUtils.documentToString(transRequestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document createShowListRequestDocument() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element <ShowListRequest>
			Element showListRequestElement = doc.createElement("ShowListRequest");
			doc.appendChild(showListRequestElement);

			// Add child elements to <ShowListRequest>
			appendElementWithValue(doc, showListRequestElement, "POSID", "01");
			appendElementWithValue(doc, showListRequestElement, "APPID", "01");
			appendElementWithValue(doc, showListRequestElement, "CCTID", "01");
			appendElementWithValue(doc, showListRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, showListRequestElement, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, showListRequestElement, "LanguageIndicator", "00");

			// Add <ListData> element with its children
			Element listDataElement = doc.createElement("ListData");
			showListRequestElement.appendChild(listDataElement);
			appendElementWithValue(doc, listDataElement, "Paragraph",
					"WARNING: Section 1001 of Title 18, United States Code, states that whoever, with respect to the logbook, knowingly and willfully falsifies, conceals, or covers up by any trick, scheme, or device a material fact, or makes any materially false, fictitious, or fraudulent statement or representation, or makes or uses any false writing or document knowingly the same to contain any materially false, fictitious, or fraudulent statement or entry, shall be fined not more than $250,000 if an individual or $500,000 if an organization, imprisoned not more than five years, or both.");

			// Add remaining elements
			appendElementWithValue(doc, showListRequestElement, "ButtonText1", "ACCEPT");
			appendElementWithValue(doc, showListRequestElement, "FontStyle", "1");
			appendElementWithValue(doc, showListRequestElement, "ScreenTimeOut", "240");
			appendElementWithValue(doc, showListRequestElement, "AllowCancel", "0");

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

	

	public static String Request() {

		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
