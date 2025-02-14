package requestbuilder;

import java.text.DecimalFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import base.SessionIdManager;
import utilities.Utils;

public class WorldPayEPP {
	private static String formattedTime = Utils.generateDateTimeAndInvoice().get(0);
	private static String finalDate = Utils.generateDateTimeAndInvoice().get(1);
	private static String invoiceNumber = Utils.generateDateTimeAndInvoice().get(2);

	private static String intemCode;

	public static String Request(String cardToken, String transType) {
		try {
			Document transRequestDocument = createSampleTransRequestDocument(cardToken, transType);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(transRequestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String returnRequest(List<String> saleData) {
		System.out.println(saleData);

		String transID = saleData.get(11);
		String AurusPayTicketNum = saleData.get(12);
		String Amount = saleData.get(8);

		try {
			Document transRequestDocument = createSampleRefundRequestDocument(transID, AurusPayTicketNum, Amount);

			// Convert the modified document back to a string
			return RequestUtils.documentToString(transRequestDocument);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Document createSampleTransRequestDocument(String cardToken, String TransType) {

		String BIN = cardToken.substring(0, 6);
		setItemCode(BIN);

		int productValues = Utils.getWPProductCount();

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
			appendElementWithValue(doc, transRequestElement, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, transRequestElement, "POSID", Utils.getPOSID());
			appendElementWithValue(doc, transRequestElement, "APPID", "01");
			appendElementWithValue(doc, transRequestElement, "CardToken", cardToken);
			appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", Utils.getAESDKSpec());
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "CardExpiryDate", "1229");
			appendElementWithValue(doc, transRequestElement, "EntrySource", "");
			appendElementWithValue(doc, transRequestElement, "EcommerceIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
			appendElementWithValue(doc, transRequestElement, "SessionId", SessionIdManager.getCurrentSessionId());

			// Add TransAmountDetails

			String zeroAmount = "0.00";

			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", amount);
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", amount);
			appendElementWithValue(doc, transAmountDetailsElement, "ServicesTotalAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "ProductTotalAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "EBTAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "FSAAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "DutyTotalAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "FreightTotalAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "CashBackAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "CashBackFees", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "DonationAmount", zeroAmount);

			appendElementWithValue(doc, transRequestElement, "TransactionType", TransType);

			// Add EPPDetailsInfo
			Element eppDetailsInfoElement = doc.createElement("EPPDetailsInfo");
			transRequestElement.appendChild(eppDetailsInfoElement);
			appendElementWithValue(doc, eppDetailsInfoElement, "AmountDue", amount);
			appendElementWithValue(doc, eppDetailsInfoElement, "ProductCount", ProductCount);
			// appendElementWithValue(doc, eppDetailsInfoElement,
			// "POSCapability","BAR.UNK.CAT.UNK.00010000000000001000000000000000"); // This
			// Field not required for the WP EPP

			final String itemCode = getItemCode();

			// Add EPPDetails with EPPProductData
			Element eppDetailsElement = doc.createElement("EPPDetails");
			eppDetailsInfoElement.appendChild(eppDetailsElement);
			// Loop to add EPPProductData
			for (int i = 1; i <= productValues; i++) {
				Element eppProductDataElement = doc.createElement("EPPProductData"); // 610233
				eppDetailsElement.appendChild(eppProductDataElement);

				appendElementWithValue(doc, eppProductDataElement, "ItemCode", itemCode);
				appendElementWithValue(doc, eppProductDataElement, "ItemReferenceNumber", String.format("%04d", i));
				appendElementWithValue(doc, eppProductDataElement, "Quantity", "001");
				appendElementWithValue(doc, eppProductDataElement, "RedemptionReqAmount", "1.00");
				appendElementWithValue(doc, eppProductDataElement, "Price", "1.00");
				appendElementWithValue(doc, eppProductDataElement, "MBASQuantityDiscount", "0.00");
				appendElementWithValue(doc, eppProductDataElement, "SignIndicator", "C");
				appendElementWithValue(doc, eppProductDataElement, "EPPPLUIndicator", "0");

				if (productValues <= 132) {

					// Last transaction approved if we added this block; where product count is 132
					// Thats why I have added these block
					appendElementWithValue(doc, eppProductDataElement, "DepartmentID", "150");
					appendElementWithValue(doc, eppProductDataElement, "TaxAmount", "0.00");
				}

			}

			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", invoiceNumber);
			appendElementWithValue(doc, transRequestElement, "TipEligible", "0");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "873");
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "157");
			appendElementWithValue(doc, transRequestElement, "ShowResponse", Utils.getShowResponseValue());
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

	private static Document createSampleRefundRequestDocument(String transactionId, String AurusPayTicketNum,
			String Amount) {

		double amount = Double.valueOf(Amount);

		double productValues = amount / 1.00;

		// Using DecimalFormat to format to two decimal places

		String ProductCount = Amount.split("\\.")[0];

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
			appendElementWithValue(doc, transRequestElement, "CCTID", Utils.getCCTID());
			appendElementWithValue(doc, transRequestElement, "POSID", "01");
			appendElementWithValue(doc, transRequestElement, "APPID", "01");
			appendElementWithValue(doc, transRequestElement, "CardToken", null);
			appendElementWithValue(doc, transRequestElement, "ADSDKSpecVer", Utils.getAESDKSpec());
			appendElementWithValue(doc, transRequestElement, "KeyedEntryAVSFlag", "N");
			appendElementWithValue(doc, transRequestElement, "CardExpiryDate", "1229");
			appendElementWithValue(doc, transRequestElement, "EntrySource", "");
			appendElementWithValue(doc, transRequestElement, "EcommerceIndicator", "N");
			appendElementWithValue(doc, transRequestElement, "ProcessingMode", "0");
			appendElementWithValue(doc, transRequestElement, "SessionId", SessionIdManager.getCurrentSessionId());

			// Add TransAmountDetails

			String zeroAmount = "0.00";

			Element transAmountDetailsElement = doc.createElement("TransAmountDetails");
			transRequestElement.appendChild(transAmountDetailsElement);
			appendElementWithValue(doc, transAmountDetailsElement, "TenderAmount", Amount);
			appendElementWithValue(doc, transAmountDetailsElement, "TransactionTotal", Amount);
			appendElementWithValue(doc, transAmountDetailsElement, "ServicesTotalAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "ProductTotalAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "TaxAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "EBTAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "FSAAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "DutyTotalAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "FreightTotalAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "CashBackAmount", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "CashBackFees", zeroAmount);
			appendElementWithValue(doc, transAmountDetailsElement, "DonationAmount", zeroAmount);

			appendElementWithValue(doc, transRequestElement, "TransactionType", "02");

			// Add EPPDetailsInfo
			Element eppDetailsInfoElement = doc.createElement("EPPDetailsInfo");
			transRequestElement.appendChild(eppDetailsInfoElement);
			appendElementWithValue(doc, eppDetailsInfoElement, "AmountDue", "");
			appendElementWithValue(doc, eppDetailsInfoElement, "ProductCount", ProductCount);
			// appendElementWithValue(doc, eppDetailsInfoElement,
			// "POSCapability","BAR.UNK.CAT.UNK.00010000000000001000000000000000");

			final String iteamCode = getItemCode();
			// Add EPPDetails with EPPProductData
			Element eppDetailsElement = doc.createElement("EPPDetails");
			eppDetailsInfoElement.appendChild(eppDetailsElement);
			// Loop to add EPPProductData
			for (int i = 1; i <= productValues; i++) {
				Element eppProductDataElement = doc.createElement("EPPProductData"); // 610233
				eppDetailsElement.appendChild(eppProductDataElement);
				appendElementWithValue(doc, eppProductDataElement, "ItemCode", iteamCode);
				appendElementWithValue(doc, eppProductDataElement, "ItemReferenceNumber", String.format("%04d", i));
				appendElementWithValue(doc, eppProductDataElement, "Quantity", "001");
				appendElementWithValue(doc, eppProductDataElement, "RedemptionReqAmount", "1.00");
				appendElementWithValue(doc, eppProductDataElement, "Price", "1.00");
				appendElementWithValue(doc, eppProductDataElement, "MBASQuantityDiscount", "0.00");
				appendElementWithValue(doc, eppProductDataElement, "SignIndicator", "C");
				appendElementWithValue(doc, eppProductDataElement, "EPPPLUIndicator", "0");

			}
			appendElementWithValue(doc, transRequestElement, "OrigAurusPayTicketNum", AurusPayTicketNum);

			appendElementWithValue(doc, transRequestElement, "OrigTransactionIdentifier", transactionId);

			appendElementWithValue(doc, transRequestElement, "InvoiceNumber", invoiceNumber);
			appendElementWithValue(doc, transRequestElement, "TipEligible", "0");
			appendElementWithValue(doc, transRequestElement, "ReceiptNumber", "873");
			appendElementWithValue(doc, transRequestElement, "ReferenceNumber", "157");
			appendElementWithValue(doc, transRequestElement, "ShowResponse", Utils.getShowResponseValue());
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

	private synchronized static void setItemCode(String BIN) {
		if (BIN.equalsIgnoreCase("610233")) {
			intemCode = "00001650056770";
		}
		if (BIN.equalsIgnoreCase("532527")) {
			intemCode = "0000004100008454";
		} else {
			intemCode = "00003800000120";
		}
	}

	private synchronized static String getItemCode() {

		return intemCode;
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

}
