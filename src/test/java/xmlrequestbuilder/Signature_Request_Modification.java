package xmlrequestbuilder;

import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import utilities.DateUtilities;

public class Signature_Request_Modification {
	
	
public static String modified_Sign_Request(  String TransactionID) {
	
		
	try {
		// Load the XML file
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build("./src\\test\\resources\\AccountLookUp\\signature.xml");

		// Get the root element (TransRequest)
		Element root = doc.getRootElement();

		// Modify the parameters
	
		root.getChild("TransactionIdentifier").setText(TransactionID);
		
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
