package request_response_modification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import java.io.StringReader;

public class GCB_Modification {
	public static String GCB_Request_Modified() throws JDOMException, IOException {

		File xmlFile = new File("D:\\Eclipse\\CCT_TESTING\\src\\test\\resources\\credit_debit\\GCB.xml");
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(xmlFile);

		// Find the element you want to modify (e.g., SessionId)
		Element rootElement = doc.getRootElement();
		Element sessionIdElement = rootElement.getChild("SessionId");

		// Modify the value
		// sessionIdElement.setText("54321");

		// Create a custom Format that omits the XML declaration
		Format customFormat = Format.getRawFormat().setOmitDeclaration(true);

		// Save the modified XML to a file without the XML declaration
		XMLOutputter xmlOutput = new XMLOutputter(customFormat);
		String modifiedXml = xmlOutput.outputString(doc);
		return modifiedXml;

	}
}
