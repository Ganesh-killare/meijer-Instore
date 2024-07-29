package request_response_modification;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.github.javafaker.Faker;

public class PLCC_Sale_Request_Modification {
	static Faker faker = new Faker();

	public static String modified_PLCCSale_Request(String cardToken, String cardIdentifier, String CRMToken) {
		 double amount = ThreadLocalRandom.current().nextDouble(30.0, 99.99);

		    // Format the double to have exactly 2 decimal places
		    DecimalFormat df = new DecimalFormat("0.00");
		    String transactionAmount = df.format(amount);
		
		
		
	
		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("D:\\Eclipse\\CCT_TESTING\\src\\test\\resources\\plccAndCbcc\\sale.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			root.getChild("TransAmountDetails").getChild("TenderAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(transactionAmount);
			root.getChild("CardToken").setText(cardToken);
			root.getChild("ECOMMInfo").getChild("CardIdentifier").setText(cardIdentifier);
			root.getChild("CRMToken").setText(CRMToken);

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
