package xmlrequestbuilder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import utilities.Utils;

public class Ewic_BalanceInquiry {
	public static String Ewic_Balance_Inquiry(String cardToken) throws JDOMException, IOException {
		double amount = ThreadLocalRandom.current().nextDouble(30.0, 99.99);

		// Format the double to have exactly 2 decimal places
		DecimalFormat df = new DecimalFormat("0.00");
		String transactionAmount = df.format(amount);

		// Date & Time with Invoice Number


		String formattedTime =	Utils.generateDateTimeAndInvoice().get(0);
		String finalDate =	Utils.generateDateTimeAndInvoice().get(1);
		String invoiceNumber =	Utils.generateDateTimeAndInvoice().get(2);

		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder
					.build("./src\\test\\resources\\E-wic\\BalanceInquiry.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters

			root.getChild("CardToken").setText(cardToken);
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
