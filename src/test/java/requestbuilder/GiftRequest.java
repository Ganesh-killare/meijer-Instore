package requestbuilder;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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

public class GiftRequest {

	private static String formattedTime = Utils.generateDateTimeAndInvoice().get(0);
	private static String finalDate = Utils.generateDateTimeAndInvoice().get(1);
	private static String invoiceNumber = Utils.generateDateTimeAndInvoice().get(2);

	private static String buildXMLRequest() {
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
			Element transRequestElement = doc.createElement("TransRequest");
			doc.appendChild(transRequestElement);

// Add child elements in the desired sequence
			appendElementWithValue(doc, transRequestElement, "POSID", Utils.getPOSID());
			appendElementWithValue(doc, transRequestElement, "APPID", "01");
			appendElementWithValue(doc, transRequestElement, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", Utils.getAESDKSpec());
			appendElementWithValue(doc, transRequestElement, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, transRequestElement, "CardPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "CardType", "GCC");
			appendElementWithValue(doc, transRequestElement, "SubCardType", "");
			appendElementWithValue(doc, transRequestElement, "CardNumber", "9840000079000000046");
			appendElementWithValue(doc, transRequestElement, "PurchaserPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "GiftPurchaseAuthIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
			appendElementWithValue(doc, transRequestElement, "CashBackFlag", Utils.getCashBackValue());

// TransAmountDetails
			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", "10.00");
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", "10.00");
			appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", "10.00");

			appendElementWithValue(doc, transRequestElement, "GiftCardTypePassCode", "");
			appendElementWithValue(doc, transRequestElement, "TransactionType", "11");
			appendElementWithValue(doc, transRequestElement, "EntrySource", "B");
			appendElementWithValue(doc, transRequestElement, "SubTransType", "");
			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", invoiceNumber);
			appendElementWithValue(doc, transRequestElement, "CardToken", "");
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "17");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "17");
			appendElementWithValue(doc, transRequestElement, "ClerkID", "001009352");
			appendElementWithValue(doc, transRequestElement, "CurrencyCode", "840");
			appendElementWithValue(doc, transRequestElement, "TransactionDate", finalDate);
			appendElementWithValue(doc, transRequestElement, "TransactionTime", formattedTime);
			appendElementWithValue(doc, transRequestElement, "TipEligible", "0");
			appendElementWithValue(doc, transRequestElement, "AmountNoBar", "1");
			appendElementWithValue(doc, transRequestElement, "SignatureFlag", "N");
			appendElementWithValue(doc, transRequestElement, "OrigAurusPayTicketNum", "");
			appendElementWithValue(doc, transRequestElement, "OrigTransactionIdentifier", "");
			appendElementWithValue(doc, transRequestElement, "PartialAllowed", "0");
			appendElementWithValue(doc, transRequestElement, "ShowResponse", Utils.getShowResponseValue());
			appendElementWithValue(doc, transRequestElement, "CardExpiryDate", "1221");
			appendElementWithValue(doc, transRequestElement, "ECommerceIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "POSType", "1");

// BillingAddress
			Element billingAddressElement = doc.createElement("BillingAddress");
			transRequestElement.appendChild(billingAddressElement);
			appendElementWithValue(doc, billingAddressElement, "BillingZip", "");

			appendElementWithValue(doc, transRequestElement, "BlackHawkUpc", "71373309079");

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


	public static String GIFT_REQUEST(String amount, String cardNumber, String EntrySource, String SubTransType,
			String transtype, String Upc, String cardToken) {
		
		String UPC_DATA = Upc ;

		amount.trim();
		cardNumber.trim();
		EntrySource.trim();
		SubTransType.trim();
		transtype.trim();
		UPC_DATA.trim();
		try {

// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

// Handling subtransce type

			String subtrans = SubTransType.trim();
			if (subtrans.isBlank()) {
				subtrans = null;
			}
			if (!(cardToken == null)) {
				cardNumber = null;
				EntrySource = null;
			}
			if (!(cardToken == null)) {
				 UPC_DATA = getUPCdata(cardToken);

			}

// Modify specific tag values
			RequestUtils.setTagValue(document, "TransactionTotal", amount);
			RequestUtils.setTagValue(document, "TenderAmount", amount);
			RequestUtils.setTagValue(document, "CardNumber", cardNumber);
			RequestUtils.setTagValue(document, "EntrySource", EntrySource);
			RequestUtils.setTagValue(document, "SubTransType", subtrans);
			RequestUtils.setTagValue(document, "TransactionType", transtype);
			RequestUtils.setTagValue(document, "CardToken", cardToken);
			RequestUtils.setTagValue(document, "BlackHawkUpc", UPC_DATA.trim());
			RequestUtils.setTagValue(document, "CardToken", cardToken);

// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getUPCdata(String key) {
		Map<String, String> data = new HashMap<>();
		// Initialize data with sample values
		data.put("9699", "71373309057");
		data.put("6424", "71373309045");
		data.put("1688", "71373309053");
		data.put("4787", "71373309077");
		data.put("2009", "71373309038");
		data.put("0046", "71373309079");
		data.put("0391", "04125010012");
		data.put("1474", "71373309078");
		data.put("7716", "07675022645");
		data.put("4208", "07675023072");
		data.put("6771", "07675017832");

		String lastFourDigits = key.substring(key.length() - 4);
		String value = data.get(lastFourDigits);
		if (value != null) {
			return value;
		} else {
			System.out.println("No value found for this card " + lastFourDigits);
			return null;
		}
	}

}