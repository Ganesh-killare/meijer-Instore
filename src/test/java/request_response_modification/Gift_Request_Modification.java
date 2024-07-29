package request_response_modification;

import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Gift_Request_Modification {
	
	
	public static String modified_Gift_Request(String amount, String cardNumber, String EntrySource, String SubTransType, String transtype , String Upc ) {

		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("D:\\Eclipse\\CCT_TESTING\\src\\test\\resources\\Gift\\Activation.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			root.getChild("TransAmountDetails").getChild("TenderAmount").setText(amount);
			root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(amount);
			root.getChild("CardNumber").setText(cardNumber);
			root.getChild("EntrySource").setText(EntrySource);
			Element subTransTypeElement = root.getChild("SubTransType");
			if (subTransTypeElement != null) {
			    subTransTypeElement.setText(SubTransType);
			}
			root.getChild("TransactionType").setText(transtype);
			root.getChild("BlackHawkUpc").setText(Upc);

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
