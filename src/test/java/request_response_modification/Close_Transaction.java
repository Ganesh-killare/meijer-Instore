package request_response_modification;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Close_Transaction {
	public static String Close_Transaction_Request() throws JDOMException, IOException {

		File xmlFile = new File("D:\\Eclipse\\CCT_TESTING\\src\\test\\resources\\credit_debit\\close.xml");
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(xmlFile);
		// Create a custom Format that omits the XML declaration
		Format customFormat = Format.getRawFormat().setOmitDeclaration(true);

		// Save the modified XML to a file without the XML declaration
		XMLOutputter xmlOutput = new XMLOutputter(customFormat);
		String modifiedXml = xmlOutput.outputString(doc);
		return modifiedXml;

	}

}
