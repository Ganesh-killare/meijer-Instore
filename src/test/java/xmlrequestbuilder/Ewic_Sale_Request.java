package xmlrequestbuilder;

import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import utilities.Utils;

public class Ewic_Sale_Request {

	public static String modified_Ewic_Sale_Request(String cardToken) {
		

		

		// Date & Time with Invoice Number
		
		

	String formattedTime =	Utils.generateDateTimeAndInvoice().get(0);
	String finalDate =	Utils.generateDateTimeAndInvoice().get(1);
	String invoiceNumber =	Utils.generateDateTimeAndInvoice().get(2);


		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\E-wic\\e-wic_Sale.xml");

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
