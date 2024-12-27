package requestbuilder;

import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import base.POS_APIs;
import utilities.Utils;

public class Check {
	private static String finalTime = Utils.generateDateTimeAndInvoice().get(0);
	private static String finalDate = Utils.generateDateTimeAndInvoice().get(1);
	private static String InvoiceNumber = Utils.generateDateTimeAndInvoice().get(2);
	static int checkData = 1;

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

			// Create root element <TransRequest>
			Element transRequestElement = doc.createElement("TransRequest");
			doc.appendChild(transRequestElement);

			// Add child elements to <TransRequest>
			appendElementWithValue(doc, transRequestElement, "POSID", "985");
			appendElementWithValue(doc, transRequestElement, "APPID", "01");
			appendElementWithValue(doc, transRequestElement, "CCTID", "01");
			appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", "6.14.8");
			appendElementWithValue(doc, transRequestElement, "CardPresent", ""); // Empty string if not specified
			appendElementWithValue(doc, transRequestElement, "PurchaserPresent", "Y");
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "GiftPurchaseAuthIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
			appendElementWithValue(doc, transRequestElement, "CashBackFlag", Utils.getCashBackValue());

			// Add <TransAmountDetails> element with its children
			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "CashBackAmount", "0.00");
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", "30.00");
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", "30.00");
			appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", ".00");

			appendElementWithValue(doc, transRequestElement, "TransactionType", "36");
			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", InvoiceNumber); // Empty string if not
																								// specified
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "78");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "78");
			appendElementWithValue(doc, transRequestElement, "ClerkID", "108488");
			appendElementWithValue(doc, transRequestElement, "CurrencyCode", "840");
			appendElementWithValue(doc, transRequestElement, "TransactionDate", finalDate); // Empty string if not
																							// specified
			appendElementWithValue(doc, transRequestElement, "TransactionTime", finalTime); // Empty string if not
																							// specified
			appendElementWithValue(doc, transRequestElement, "TipEligible", "0");
			appendElementWithValue(doc, transRequestElement, "AmountNoBar", "1");
			appendElementWithValue(doc, transRequestElement, "SignatureFlag", "N");
			appendElementWithValue(doc, transRequestElement, "OrigAurusPayTicketNum", ""); // Empty string if not
																							// specified
			appendElementWithValue(doc, transRequestElement, "OrigTransactionIdentifier", "");
			appendElementWithValue(doc, transRequestElement, "PartialAllowed", "0");
			appendElementWithValue(doc, transRequestElement, "ShowResponse", Utils.getShowResponseValue());
			appendElementWithValue(doc, transRequestElement, "ECommerceIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "POSType", "1");

			// Add <BillingAddress> element with <BillingZip> child
			Element billingAddressElement = doc.createElement("BillingAddress");
			transRequestElement.appendChild(billingAddressElement);
			appendElementWithValue(doc, billingAddressElement, "BillingZip", "49544");

			// Add <CheckInfo> element with its children
			Element checkInfoElement = doc.createElement("CheckInfo");
			transRequestElement.appendChild(checkInfoElement);

			switch (checkData) {
			case 1:
				appendElementWithValue(doc, checkInfoElement, "CheckEntryMode", "S");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseEntryMode", "M");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseData", "11112222");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseState", "TX");
				appendElementWithValue(doc, checkInfoElement, "FullMICR", "t031100649t222 670173768870o 2233");
				appendElementWithValue(doc, checkInfoElement, "CheckType", "P");
				System.out.println("FullMICR = t031100649t222 670173768870o 2233");
				checkData++;

				break;

			case 2:
				appendElementWithValue(doc, checkInfoElement, "CheckEntryMode", "S");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseEntryMode", "M");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseData", "11112222");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseState", "TX");
				appendElementWithValue(doc, checkInfoElement, "FullMICR", "t123456780t 0014741o5678 5678");
				appendElementWithValue(doc, checkInfoElement, "CheckType", "P");
				System.out.println("FullMICR = t123456780t 0014741o5678 5678");
				checkData++;
				break;
			case 3:
				appendElementWithValue(doc, checkInfoElement, "CheckEntryMode", "S");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseEntryMode", "M");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseData", "11112222");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseState", "TX");
				appendElementWithValue(doc, checkInfoElement, "FullMICR", "o002020o t123456780t 458745210o 002020");
				appendElementWithValue(doc, checkInfoElement, "CheckType", "P");
				System.out.println("FullMICR = o002020o t123456780t 458745210o 002020");
				checkData++;
				break;

			case 4:
				appendElementWithValue(doc, checkInfoElement, "CheckEntryMode", "S");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseEntryMode", "M");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseData", "11112222");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseState", "TX");
				appendElementWithValue(doc, checkInfoElement, "FullMICR",
						"o1098765432o t321270742t 012547854o 1098765432");
				appendElementWithValue(doc, checkInfoElement, "CheckType", "P");
				System.out.println("FullMICR = o1098765432o t321270742t 012547854o 1098765432");
				checkData++;
				break;

			default:
				appendElementWithValue(doc, checkInfoElement, "CheckEntryMode", "S");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseEntryMode", "M");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseData", "11112222");
				appendElementWithValue(doc, checkInfoElement, "DriverLicenseState", "TX");
				appendElementWithValue(doc, checkInfoElement, "FullMICR", "o891o t06532d010t 23d45678o 891");
				appendElementWithValue(doc, checkInfoElement, "CheckType", "P");
				System.out.println("FullMICR = o891o t06532d010t 23d45678o 891 ");
				checkData = 1;
				break;
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

	public static String Request() {
		try {
			// take a basic request
			String xml = buildXMLRequest();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
			String AMT = POS_APIs.generateTransactionAmount();
			// Modify specific tag values
			RequestUtils.setTagValue(document, "TenderAmount", AMT);
			RequestUtils.setTagValue(document, "TransactionTotal", AMT);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
