package xmlrequestbuilder;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import utilities.DateUtilities;

public class Refund_Request_Modification {
	public static String modified_Refund_Request(String transType, String amount, String AuruspayTicketNumber,
			String TransactionID) {

		// Date & Time with Invoice Number

		// Get the current date and time

		String formattedTime =	DateUtilities.generateDateTimeAndInvoice().get(0);
		String finalDate =	DateUtilities.generateDateTimeAndInvoice().get(1);
		String invoiceNumber =	DateUtilities.generateDateTimeAndInvoice().get(2);

		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\credit_debit\\Refund.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			root.getChild("TransAmountDetails").getChild("TenderAmount").setText(amount);
			root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(amount);
			root.getChild("OrigAurusPayTicketNum").setText(AuruspayTicketNumber);
			root.getChild("OrigTransactionIdentifier").setText(TransactionID);
			root.getChild("TransactionType").setText(transType);
			root.getChild("InvoiceNumber").setText(invoiceNumber);
			root.getChild("TransactionDate").setText(finalDate);
			root.getChild("TransactionTime").setText(formattedTime);

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
