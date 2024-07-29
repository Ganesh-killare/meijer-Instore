package xmlrequestbuilder;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class PrepaidGCB_Request {
	public static String GCB_Request_Modified(String A_Keyed) throws JDOMException, IOException {

	File xmlFile = new File("./src\\test\\resources\\Gift\\GCBPrepaid.xml");
	SAXBuilder saxBuilder = new SAXBuilder();
	Document doc = saxBuilder.build(xmlFile);

	// Find the element you want to modify (e.g., SessionId)
	Element rootElement = doc.getRootElement();
	Element AllowKeyedEntry = rootElement.getChild("AllowKeyedEntry");

	
	AllowKeyedEntry.setText(A_Keyed);

	// Create a custom Format that omits the XML declaration
	Format customFormat = Format.getRawFormat().setOmitDeclaration(true);

	// Save the modified XML to a file without the XML declaration
	XMLOutputter xmlOutput = new XMLOutputter(customFormat);
	String modifiedXml = xmlOutput.outputString(doc);
	return modifiedXml;

}

}
