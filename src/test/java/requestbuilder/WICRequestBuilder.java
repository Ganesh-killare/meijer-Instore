package requestbuilder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import base.SessionIdManager;
import utilities.Utils;

public class WICRequestBuilder {

	static String formattedTime = Utils.sWICTimeAndDate().get(0);
	static String finalDate = Utils.sWICTimeAndDate().get(1);
	static String invoiceNumber = Utils.sWICTimeAndDate().get(2);

	// Common parameters across all requests
	private static final String POSID = Utils.getPOSID();
	private static final String APPID = "01";
	private static final String CCTID = Utils.getCCTID();
	private static final String ClerkID = "000108488";
	private static final String ADSDKSpecVer = Utils.getAESDKSpec();

	// Helper function to append elements to the document
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

	// Function to build the base WICCardRequest structure
	private static Document buildBaseWICCardRequest() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element
			Element wicCardRequest = doc.createElement("WICCardRequest");
			doc.appendChild(wicCardRequest);

			appendElementWithValue(doc, wicCardRequest, "ADSDKSpecVer", ADSDKSpecVer);
			appendElementWithValue(doc, wicCardRequest, "POSID", POSID);
			appendElementWithValue(doc, wicCardRequest, "APPID", APPID);
			appendElementWithValue(doc, wicCardRequest, "CCTID", CCTID);
			appendElementWithValue(doc, wicCardRequest, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, wicCardRequest, "ClerkID", ClerkID);

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Function to build a WICCardRequest for PIN transaction
	public static String buildWICCardRequestForPin() {
		try {
			Document doc = buildBaseWICCardRequest();
			Element wicCardRequest = (Element) doc.getFirstChild();

			appendElementWithValue(doc, wicCardRequest, "DisplayOnly", "");
			appendElementWithValue(doc, wicCardRequest, "LangIndicator", "00");
			appendElementWithValue(doc, wicCardRequest, "MessageLine1", "");
			appendElementWithValue(doc, wicCardRequest, "MessageLine2", "");
			appendElementWithValue(doc, wicCardRequest, "ScreenTimeOut", "240");
			appendElementWithValue(doc, wicCardRequest, "TransactionDate", finalDate);
			System.out.println(finalDate);
			appendElementWithValue(doc, wicCardRequest, "TransactionTime", formattedTime);
			appendElementWithValue(doc, wicCardRequest, "WICTransType", "07");

			return RequestUtils.documentToString(doc);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Function to build a WICCardRequest for Balance Inquiry
	public static String buildWICCardRequestForBalanceInquiry() {
		try {
			Document doc = buildBaseWICCardRequest();
			Element wicCardRequest = (Element) doc.getFirstChild();

			appendElementWithValue(doc, wicCardRequest, "DisplayOnly", "");
			appendElementWithValue(doc, wicCardRequest, "LangIndicator", "00");
			appendElementWithValue(doc, wicCardRequest, "MessageLine1", "");
			appendElementWithValue(doc, wicCardRequest, "MessageLine2", "");
			appendElementWithValue(doc, wicCardRequest, "ScreenTimeOut", "240");
			appendElementWithValue(doc, wicCardRequest, "TransactionDate", finalDate);
			appendElementWithValue(doc, wicCardRequest, "TransactionTime", formattedTime);
			appendElementWithValue(doc, wicCardRequest, "WICTransType", "08");

			return RequestUtils.documentToString(doc);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Function to build a WICCardRequest for Sale
	public static String buildWICCardRequestForSale() {
		try {
			Document doc = buildBaseWICCardRequest();
			Element wicCardRequest = (Element) doc.getFirstChild();

			appendElementWithValue(doc, wicCardRequest, "WICTransType", "01");
			appendElementWithValue(doc, wicCardRequest, "TransactionDate", finalDate);
			appendElementWithValue(doc, wicCardRequest, "TransactionTime", formattedTime);
			appendElementWithValue(doc, wicCardRequest, "LangIndicator", "00");
			appendElementWithValue(doc, wicCardRequest, "DisplayOnly", "");

			// Add PrescriptionData
			Element prescriptionData = doc.createElement("PrescriptionData");
			wicCardRequest.appendChild(prescriptionData);
			appendElementWithValue(doc, prescriptionData, "PrescriptionDataCount", "1");

			// Add PrescriptionItems
			Element prescriptionItems = doc.createElement("PrescriptionItems");
			prescriptionData.appendChild(prescriptionItems);
			Element prescriptionItem = doc.createElement("PrescriptionItem");
			prescriptionItems.appendChild(prescriptionItem);
			appendElementWithValue(doc, prescriptionItem, "ItemNumber", "1");
			appendElementWithValue(doc, prescriptionItem, "Category", "19");
			appendElementWithValue(doc, prescriptionItem, "SubCategory", "001");
			appendElementWithValue(doc, prescriptionItem, "Quantity", "2300");
			appendElementWithValue(doc, prescriptionItem, "UnitofMeasure", "NUM");

			return RequestUtils.documentToString(doc);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Function to build a WICCardRequest for Void
	public static String buildWICCardRequestForVoid() {
		try {
			Document doc = buildBaseWICCardRequest();
			Element wicCardRequest = (Element) doc.getFirstChild();

			appendElementWithValue(doc, wicCardRequest, "WICTransType", "06");
			appendElementWithValue(doc, wicCardRequest, "TransactionDate", finalDate);
			appendElementWithValue(doc, wicCardRequest, "TransactionTime", formattedTime);
			appendElementWithValue(doc, wicCardRequest, "LangIndicator", "00");
			appendElementWithValue(doc, wicCardRequest, "DisplayOnly", "");

			return RequestUtils.documentToString(doc);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Test
	void test() {
		System.out.println(WICRequestBuilder.buildWICCardRequestForPin());
		System.out.println(WICRequestBuilder.buildWICCardRequestForBalanceInquiry());
		System.out.println(WICRequestBuilder.buildWICCardRequestForSale());
		System.out.println(WICRequestBuilder.buildWICCardRequestForVoid());
	}

}
