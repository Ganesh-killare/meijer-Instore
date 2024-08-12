package xmlrequestbuilder;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.github.javafaker.Faker;

import utilities.Utils;

public class POA_RequestModification {

	public static String modified_POA_Request(String transType,  String AuruspayTicketNumber, String cardIdentifier, String paymentMethod) {

		double amount = ThreadLocalRandom.current().nextDouble(05.00, 30.00);

		// Format the double to have exactly 2 decimal places
		DecimalFormat df = new DecimalFormat("0.00");
		String transactionAmount = df.format(amount);

		// Get the current date and time & Invoice number

		String formattedTime =	Utils.generateDateTimeAndInvoice().get(0);
		String finalDate =	Utils.generateDateTimeAndInvoice().get(1);
		String invoiceNumber =	Utils.generateDateTimeAndInvoice().get(2);

		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\AccountLookUp\\POARequest.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			root.getChild("TransAmountDetails").getChild("TenderAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(transactionAmount);
			root.getChild("OrigAurusPayTicketNum").setText(AuruspayTicketNumber);
			root.getChild("ECOMMInfo").getChild("CardIdentifier").setText(cardIdentifier);
			root.getChild("TransactionType").setText(transType);
			root.getChild("InvoiceNumber").setText(invoiceNumber);
			root.getChild("TransactionDate").setText(finalDate);
			root.getChild("TransactionTime").setText(formattedTime);
			root.getChild("PLCCPaymentMethod").setText(paymentMethod);

			Format format = Format.getPrettyFormat();
			format.setOmitDeclaration(true);

			// Convert the modified XML to a string with the custom format
			XMLOutputter xmlOutput = new XMLOutputter(format);
			StringWriter stringWriter = new StringWriter();
			xmlOutput.output(doc, stringWriter);

			return stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
