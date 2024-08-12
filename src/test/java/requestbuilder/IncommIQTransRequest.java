package requestbuilder;

import java.io.StringWriter;
import java.text.DecimalFormat;

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

import com.github.javafaker.Faker;

import base.SessionIdManager;
import utilities.Utils;

public class IncommIQTransRequest {

	private static String formattedTime = Utils.generateDateTimeAndInvoice().get(0);
	private static String finalDate = Utils.generateDateTimeAndInvoice().get(1);
	private static String invoiceNumber = Utils.generateDateTimeAndInvoice().get(2);

	public static String Request(String cardToken, String TransType) {
		try {
			Document transRequestDocument = createSampleTransRequestDocument(cardToken, TransType);

			// Convert the modified document back to a string
			return documentToString(transRequestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document createSampleTransRequestDocument(String cardToken, String TransType) {

		// Product values

		Faker faker = new Faker();

		int productValues = faker.random().nextInt(10, 20);
		System.out.println(productValues);

		double totalTransAmount = 1.00 * productValues;

		// Using DecimalFormat to format to two decimal places
		DecimalFormat df = new DecimalFormat("#0.00");
		String amount = df.format(totalTransAmount);

		String ProductCount;
		ProductCount = String.valueOf(productValues);

		// Ensure ProductCount is three digits
		if (ProductCount.length() == 2) {
			ProductCount = "0" + ProductCount;
		} else if (ProductCount.length() == 1) {
			ProductCount = "00" + ProductCount;
		}
		System.out.println("We have Product count: " + ProductCount + " And Amount is: " + amount);

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Create root element
			Element transRequestElement = doc.createElement("TransRequest");
			doc.appendChild(transRequestElement);

			// Add child elements in the desired sequence
			appendElementWithValue(doc, transRequestElement, "POSID", "1");
			appendElementWithValue(doc, transRequestElement, "APPID", "01");
			appendElementWithValue(doc, transRequestElement, "CCTID", "01");
			appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, transRequestElement, "SessionId", SessionIdManager.getCurrentSessionId());
			appendElementWithValue(doc, transRequestElement, "CardType", "EPP");
			appendElementWithValue(doc, transRequestElement, "SubCardType", "EPP");
			appendElementWithValue(doc, transRequestElement, "CardExpiryDate", "1237");
			appendElementWithValue(doc, transRequestElement, "PurchaserPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "GiftPurchaseAuthIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "CashBackFlag", Utils.getCashBackValue());
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");

			// Add TransAmountDetails
			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", amount);
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", amount);
			appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", "0.00");

			appendElementWithValue(doc, transRequestElement, "TransactionType", TransType);
			appendElementWithValue(doc, transRequestElement, "ProgramId", "01");
			appendElementWithValue(doc, transRequestElement, "CardPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", invoiceNumber);
			appendElementWithValue(doc, transRequestElement, "CardNumber", "");
			appendElementWithValue(doc, transRequestElement, "CardToken", cardToken); // Updated with runtime value
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "12345");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "10753");
			appendElementWithValue(doc, transRequestElement, "ClerkID", "001039181");
			appendElementWithValue(doc, transRequestElement, "EntrySource", "");
			appendElementWithValue(doc, transRequestElement, "CurrencyCode", "840");
			appendElementWithValue(doc, transRequestElement, "TransactionDate", finalDate);
			appendElementWithValue(doc, transRequestElement, "TransactionTime", formattedTime);
			appendElementWithValue(doc, transRequestElement, "TipEligible", "0");
			appendElementWithValue(doc, transRequestElement, "AmountNoBar", "1");
			appendElementWithValue(doc, transRequestElement, "SignatureFlag", "N");
			appendElementWithValue(doc, transRequestElement, "OrigAurusPayTicketNum", "");
			appendElementWithValue(doc, transRequestElement, "OrigTransactionIdentifier", "");
			appendElementWithValue(doc, transRequestElement, "PartialAllowed", "1");
			appendElementWithValue(doc, transRequestElement, "ShowResponse", Utils.getShowResponseValue());
			appendElementWithValue(doc, transRequestElement, "ECommerceIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "POSType", "1");

			// Billing Address
			Element billingAddressElement = doc.createElement("BillingAddress");
			transRequestElement.appendChild(billingAddressElement);
			appendElementWithValue(doc, billingAddressElement, "BillingZip", "48846");

			// EPPDetailsInfo
			Element eppDetailsInfoElement = doc.createElement("EPPDetailsInfo");
			transRequestElement.appendChild(eppDetailsInfoElement);
			appendElementWithValue(doc, eppDetailsInfoElement, "AmountDue", "0.00");
			appendElementWithValue(doc, eppDetailsInfoElement, "ProductCount", ProductCount);
			appendElementWithValue(doc, eppDetailsInfoElement, "POSCapability",
					"BAR.UNK.CAT.UNK.00010000000000001000000000000000");

			// EPPDetails with EPPProductData
			Element eppDetailsElement = doc.createElement("EPPDetails");
			eppDetailsInfoElement.appendChild(eppDetailsElement);

			// Loop to add EPPProductData
			for (int i = 1; i <= productValues; i++) {
				Element eppProductDataElement = doc.createElement("EPPProductData");
				eppDetailsElement.appendChild(eppProductDataElement);
				appendElementWithValue(doc, eppProductDataElement, "ItemCode", "00003800000120");
				appendElementWithValue(doc, eppProductDataElement, "ItemReferenceNumber", String.format("%04d", i));
				appendElementWithValue(doc, eppProductDataElement, "Price", "01.00");
				appendElementWithValue(doc, eppProductDataElement, "Quantity", "001");
				appendElementWithValue(doc, eppProductDataElement, "MBASQuantityDiscount", "0.00");
				appendElementWithValue(doc, eppProductDataElement, "SignIndicator", "C");
			}

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
