package xmlrequestbuilder;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import utilities.Utils;

public class Ewic_Void_Request {
	public static String modified_Void_Request( String AuruspayTicketNumber, String TransactionID) {
		

		// Date & Time with Invoice Number
		
		


		String formattedTime =	Utils.generateDateTimeAndInvoice().get(0);
		String finalDate =	Utils.generateDateTimeAndInvoice().get(1);
		String invoiceNumber =	Utils.generateDateTimeAndInvoice().get(2);

		
		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\E-wic\\e-wic_void.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
	//		root.getChild("TransAmountDetails").getChild("TenderAmount").setText(amount);
		//	root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(amount);
			root.getChild("OrigAurusPayTicketNum").setText(AuruspayTicketNumber);
		//	root.getChild("OrigTransactionIdentifier").setText(TransactionID);
		//	root.getChild("TransactionType").setText(transType);
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
