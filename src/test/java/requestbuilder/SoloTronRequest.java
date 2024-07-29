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

import utilities.DateUtilities;

public class SoloTronRequest {

	private static String formattedTime = DateUtilities.generateDateTimeAndInvoice().get(0);
	private static String finalDate = DateUtilities.generateDateTimeAndInvoice().get(1);
	private static String invoiceNumber = DateUtilities.generateDateTimeAndInvoice().get(2);

	public static String Request(String cardToken, String transType) {
		try {
			Document transRequestDocument = createSampleTransRequestDocument(cardToken, transType);

			// Convert the modified document back to a string
			return documentToString(transRequestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document createSampleTransRequestDocument(String cardToken, String TransType) {

		Faker faker = new Faker();
		int productValues = faker.random().nextInt(01, 10);

		// int productValues = 10;
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
			appendElementWithValue(doc, transRequestElement, "CCTID", "01");
			appendElementWithValue(doc, transRequestElement, "POSID", "01");
			appendElementWithValue(doc, transRequestElement, "APPID", "01");
			appendElementWithValue(doc, transRequestElement, "CardToken", cardToken);
			appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "CardExpiryDate", "1229");
			appendElementWithValue(doc, transRequestElement, "EntrySource", "");
			appendElementWithValue(doc, transRequestElement, "EcommerceIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");

			// Add TransAmountDetails
			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", amount);
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", amount);

			appendElementWithValue(doc, transRequestElement, "TransactionType", TransType);

			// Add EPPDetailsInfo
			Element eppDetailsInfoElement = doc.createElement("EPPDetailsInfo");
			transRequestElement.appendChild(eppDetailsInfoElement);
			appendElementWithValue(doc, eppDetailsInfoElement, "AmountDue", "");
			appendElementWithValue(doc, eppDetailsInfoElement, "ProductCount", ProductCount);
			appendElementWithValue(doc, eppDetailsInfoElement, "POSCapability",
					"BAR.UNK.CAT.UNK.00010000000000001000000000000000");

			// Add EPPDetails with EPPProductData
			Element eppDetailsElement = doc.createElement("EPPDetails");
			eppDetailsInfoElement.appendChild(eppDetailsElement);
			// Loop to add EPPProductData

			for (int i = 1; i <= productValues; i++) {
				Element eppProductDataElement = doc.createElement("EPPProductData");
				eppDetailsElement.appendChild(eppProductDataElement);
				appendElementWithValue(doc, eppProductDataElement, "ItemCode", "00003800000120");
				appendElementWithValue(doc, eppProductDataElement, "ItemReferenceNumber", String.format("%04d", i));
				appendElementWithValue(doc, eppProductDataElement, "Quantity", "001");
				appendElementWithValue(doc, eppProductDataElement, "RedemptionReqAmount", "1.00");
				appendElementWithValue(doc, eppProductDataElement, "MBASQuantityDiscount", "0.00");
				appendElementWithValue(doc, eppProductDataElement, "SignIndicator", "C");
				appendElementWithValue(doc, eppProductDataElement, "DepartmentID", "150");
				appendElementWithValue(doc, eppProductDataElement, "TaxAmount", "0.00");

			}

			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", invoiceNumber);
			appendElementWithValue(doc, transRequestElement, "TipEligible", "0");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "873");
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "157");
			appendElementWithValue(doc, transRequestElement, "ShowResponse", "0");
			appendElementWithValue(doc, transRequestElement, "CurrencyCode", "840");
			appendElementWithValue(doc, transRequestElement, "ClerkID", "1111");
			appendElementWithValue(doc, transRequestElement, "TransactionDate", finalDate);
			appendElementWithValue(doc, transRequestElement, "TransactionTime", formattedTime);
			appendElementWithValue(doc, transRequestElement, "BatchNumber", "");
			appendElementWithValue(doc, transRequestElement, "PartialAllowed", "1");

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
