package requestbuilder;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

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

public class Fleet {

	// Constants
	private static final String CCTID = "01";
	private static final String POSID = "01";
	private static final String APPID = "01";
	private static final String ADSDK_SPEC_VER = "6.14.8";
	private static final String KEYED_ENTRY_AVS_FLAG = "N";
	private static final String ECOMMERCE_INDICATOR = "N";
	private static final String PROCESSING_MODE = "0";
	private static final String CURRENCY_CODE = "840";
	private static final String CLERK_ID = "000000551";
	private static final String REFERENCE_NUMBER = "00"; // Placeholder
	private static final String RECEIPT_NUMBER = "18"; // Placeholder
	private static final String AMOUNT_NO_BAR = "1"; // Placeholder
	private static final String TIP_ELIGIBLE = "0";
	private static final String PARTIAL_ALLOWED = "1";
	private static final String SHOW_RESPONSE = "1";

	private static String formattedTime = Utils.generateDateTimeAndInvoice().get(0);
	private static String finalDate = Utils.generateDateTimeAndInvoice().get(1);
	private static String invoiceNumber = Utils.generateDateTimeAndInvoice().get(2);

	public static String SaleRequest(List<String> tokens) {
		return createXmlDocument(tokens, "01", false);
	}

	public static String RW_Sale(List<String> tokens) {
		return createXmlDocument(tokens, "02", false);
	}

	public static String RefundRequest(List<String> Data) {
		return createXmlDocument(Data, "02", true);
	}

	private static String createXmlDocument(List<String> Data, String transType, boolean isRefund) {
		try {
			Document doc = createDocument();

			Element transRequestElement = doc.createElement("TransRequest");
			doc.appendChild(transRequestElement);

			addCommonElements(doc, transRequestElement,  transType);

			if (isRefund) {
				addRefundSpecificElements(doc, transRequestElement, Data);
			} else {
				addTransactionSpecificElements(doc, transRequestElement, Data);
			}

			return RequestUtils.documentToString(doc);
		} catch (Exception e) {
			// Consider using a logging framework here
			e.printStackTrace();
			return null;
		}
	}

	private static Document createDocument() throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		return docBuilder.newDocument();
	}

	private static void addCommonElements(Document doc, Element parentElement,  String transType) {
		appendElementWithValue(doc, parentElement, "POSID", POSID);
		appendElementWithValue(doc, parentElement, "APPID", APPID);
		appendElementWithValue(doc, parentElement, "CCTID", CCTID);
		appendElementWithValue(doc, parentElement, "ADSDKSpecVer", ADSDK_SPEC_VER);
		appendElementWithValue(doc, parentElement, "InvoiceNumber", invoiceNumber);
		appendElementWithValue(doc, parentElement, "ReferenceNumber", REFERENCE_NUMBER);
		appendElementWithValue(doc, parentElement, "ReceiptNumber", RECEIPT_NUMBER);
		appendElementWithValue(doc, parentElement, "ClerkID", CLERK_ID);
		appendElementWithValue(doc, parentElement, "CurrencyCode", CURRENCY_CODE);
		appendElementWithValue(doc, parentElement, "TransactionType", transType);
		appendElementWithValue(doc, parentElement, "SessionId", SessionIdManager.getCurrentSessionId());
		appendElementWithValue(doc, parentElement, "CardPresent", "Y");
		appendElementWithValue(doc, parentElement, "CardType", "VIC");
		appendElementWithValue(doc, parentElement, "PurchaserPresent", "Y");
		appendElementWithValue(doc, parentElement, "KeyedEntryAVSFlag", KEYED_ENTRY_AVS_FLAG);
		appendElementWithValue(doc, parentElement, "GiftPurchaseAuthIndicator", "N");
		appendElementWithValue(doc, parentElement, "ProcessingMode", PROCESSING_MODE);
		appendElementWithValue(doc, parentElement, "CashBackFlag", Utils.getCashBackValue());
	}

	private static void addTransactionSpecificElements(Document doc, Element parentElement, List<String> tokens) {
		Faker faker = new Faker();
		int productValues = faker.random().nextInt(2, 10); // Generate random product count between 1 and 10
		double L3ProductUnitPrice = 2.0;
		double totalTransAmount = L3ProductUnitPrice * productValues;

		DecimalFormat df = new DecimalFormat("#0.00");
		String amount = df.format(totalTransAmount);
		String productCount = String.format("%03d", productValues);

		Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
		parentElement.appendChild(transAmountDetailsElement);
		appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", amount);
		appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", amount);
		appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", "0.00");

		Element level3ProductsDataElement = doc.createElement("Level3ProductsData");
		parentElement.appendChild(level3ProductsDataElement);
		appendElementWithValue(doc, level3ProductsDataElement, "Level3ProductCount", productCount);

		Element level3ProductsElement = doc.createElement("Level3Products");
		level3ProductsDataElement.appendChild(level3ProductsElement);

		for (int i = 1; i <= productValues; i++) {
			Element level3ProductElement = doc.createElement("Level3Product");
			level3ProductsElement.appendChild(level3ProductElement);
			appendElementWithValue(doc, level3ProductElement, "L3ProductSeqNo", String.valueOf(i));
			appendElementWithValue(doc, level3ProductElement, "L3ProductCode", "001");
			appendElementWithValue(doc, level3ProductElement, "L3ProductName", "Unleaded");
			appendElementWithValue(doc, level3ProductElement, "L3UnitOfMeasure", "G");
			appendElementWithValue(doc, level3ProductElement, "L3ProductQuantity", "1.000");
			appendElementWithValue(doc, level3ProductElement, "L3ProductUnitPrice", String.valueOf(L3ProductUnitPrice));
			appendElementWithValue(doc, level3ProductElement, "L3ProductTotalAmount",
					String.valueOf(L3ProductUnitPrice));
		}

		appendElementWithValue(doc, parentElement, "CardToken", tokens.get(1));
		appendElementWithValue(doc, parentElement, "CRMToken", tokens.get(3));
		Element ecommInfoElement = doc.createElement("ECOMMInfo");
		parentElement.appendChild(ecommInfoElement);
		appendElementWithValue(doc, ecommInfoElement, "CardIdentifier", tokens.get(2));
		appendElementWithValue(doc, parentElement, "TransactionDate", finalDate);
		appendElementWithValue(doc, parentElement, "TransactionTime", formattedTime);
		appendElementWithValue(doc, parentElement, "TipEligible", TIP_ELIGIBLE);
		appendElementWithValue(doc, parentElement, "AmountNoBar", AMOUNT_NO_BAR);
		appendElementWithValue(doc, parentElement, "OrigAurusPayTicketNum", "");
		appendElementWithValue(doc, parentElement, "OrigTransactionIdentifier", "");
		appendElementWithValue(doc, parentElement, "PartialAllowed", PARTIAL_ALLOWED);
		appendElementWithValue(doc, parentElement, "ShowResponse", SHOW_RESPONSE);
		appendElementWithValue(doc, parentElement, "ECommerceIndicator", ECOMMERCE_INDICATOR);
	}

	private static void addRefundSpecificElements(Document doc, Element parentElement, List<String> SaleData) {
		double amount = Double.valueOf(SaleData.get(8));
		double L3ProductUnitPrice = 2.0;
		int productValues = (int) (amount / L3ProductUnitPrice);
		DecimalFormat df = new DecimalFormat("#0.00");
		String formattedAmount = df.format(amount);
		String productCount = String.format("%03d", productValues);

		Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
		parentElement.appendChild(transAmountDetailsElement);
		appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", formattedAmount);
		appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", formattedAmount);
		appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", "0.00");
		Element level3ProductsDataElement = doc.createElement("Level3ProductsData");
		parentElement.appendChild(level3ProductsDataElement);
		appendElementWithValue(doc, level3ProductsDataElement, "Level3ProductCount", productCount);

		Element level3ProductsElement = doc.createElement("Level3Products");
		level3ProductsDataElement.appendChild(level3ProductsElement);

		for (int i = 1; i <= productValues; i++) {
			Element level3ProductElement = doc.createElement("Level3Product");
			level3ProductsElement.appendChild(level3ProductElement);
			appendElementWithValue(doc, level3ProductElement, "L3ProductSeqNo", String.valueOf(i));
			appendElementWithValue(doc, level3ProductElement, "L3ProductCode", "001");
			appendElementWithValue(doc, level3ProductElement, "L3ProductName", "Unleaded");
			appendElementWithValue(doc, level3ProductElement, "L3UnitOfMeasure", "G");
			appendElementWithValue(doc, level3ProductElement, "L3ProductQuantity", "1.000");
			appendElementWithValue(doc, level3ProductElement, "L3ProductUnitPrice", String.valueOf(L3ProductUnitPrice));
			appendElementWithValue(doc, level3ProductElement, "L3ProductTotalAmount",
					String.valueOf(L3ProductUnitPrice));
		}

		appendElementWithValue(doc, parentElement, "CardToken", "");
		appendElementWithValue(doc, parentElement, "CRMToken", "");
		Element ecommInfoElement = doc.createElement("ECOMMInfo");
		parentElement.appendChild(ecommInfoElement);
		appendElementWithValue(doc, ecommInfoElement, "CardIdentifier", "");
		appendElementWithValue(doc, parentElement, "TransactionDate", finalDate);
		appendElementWithValue(doc, parentElement, "TransactionTime", formattedTime);
		appendElementWithValue(doc, parentElement, "TipEligible", TIP_ELIGIBLE);
		appendElementWithValue(doc, parentElement, "AmountNoBar", AMOUNT_NO_BAR);
		appendElementWithValue(doc, parentElement, "OrigAurusPayTicketNum", SaleData.get(12));
		appendElementWithValue(doc, parentElement, "OrigTransactionIdentifier", SaleData.get(11));
		appendElementWithValue(doc, parentElement, "PartialAllowed", PARTIAL_ALLOWED);
		appendElementWithValue(doc, parentElement, "ShowResponse", SHOW_RESPONSE);
		appendElementWithValue(doc, parentElement, "ECommerceIndicator", ECOMMERCE_INDICATOR);
	}

	private static void appendElementWithValue(Document doc, Element parentElement, String tagName,
			String textContent) {
		Element element = doc.createElement(tagName);
		Text textNode = doc.createTextNode(textContent != null ? textContent : "");
		element.appendChild(textNode);
		parentElement.appendChild(element);
	}

}
