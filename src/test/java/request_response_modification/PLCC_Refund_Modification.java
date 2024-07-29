package request_response_modification;

import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class PLCC_Refund_Modification {
	
	public static String modified_Refund_Request( String transType, String amount , String AuruspayTicketNumber, String TransactionID) {
	
	try {
		// Load the XML file
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build("D:\\Eclipse\\CCT_TESTING\\src\\test\\resources\\plccAndCbcc\\Refund.xml");

		// Get the root element (TransRequest)
		Element root = doc.getRootElement();

		// Modify the parameters
		root.getChild("TransAmountDetails").getChild("TenderAmount").setText(amount);
		root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(amount);
		root.getChild("OrigAurusPayTicketNum").setText(AuruspayTicketNumber);
		root.getChild("OrigTransactionIdentifier").setText(TransactionID);
		root.getChild("TransactionType").setText(transType);
		
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
