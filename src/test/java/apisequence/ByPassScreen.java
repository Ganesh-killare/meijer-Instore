package apisequence;

import java.io.IOException;
import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ByPassScreen {
	
	
	public static String ByPassScreenRequest(String bypassOption) throws JDOMException, IOException {

		// Load the XML file
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build("./src\\test\\resources\\API sequence\\ByPassScreen.xml");

		// Get the root element (TransRequest)
		Element root = doc.getRootElement();
		root.getChild("ByPassOptions").setText(bypassOption);
		
		Format format = Format.getPrettyFormat();
		format.setOmitDeclaration(true);

		// Convert the modified XML to a string with the custom format
		XMLOutputter xmlOutput = new XMLOutputter(format);
		StringWriter stringWriter = new StringWriter();
		xmlOutput.output(doc, stringWriter);

		return stringWriter.toString();
	}

}
