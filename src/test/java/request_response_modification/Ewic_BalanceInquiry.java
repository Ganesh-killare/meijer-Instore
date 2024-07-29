package request_response_modification;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Ewic_BalanceInquiry {
	public static String Ewic_Balance_Inquiry(String cardToken) throws JDOMException, IOException {
		double amount = ThreadLocalRandom.current().nextDouble(30.0, 99.99);

		// Format the double to have exactly 2 decimal places
		DecimalFormat df = new DecimalFormat("0.00");
		String transactionAmount = df.format(amount);

		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("D:\\Eclipse\\CCT_TESTING\\src\\test\\resources\\E-wic\\BalanceInquiry.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			
			root.getChild("CardToken").setText(cardToken);
			

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
