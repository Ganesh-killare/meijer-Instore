package xmlrequestbuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class GCB_Modification {
	public static String GCB_Request_Modified() throws JDOMException, IOException {

		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream("config.properties")) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Accessing properties
		// String SaleType = properties.getProperty("SaleType");
		String LookUpFlag = properties.getProperty("LookUpFlag");
		String AllowKeyedEntry = properties.getProperty("AllowKeyedEntry");

		File xmlFile = new File("./src\\test\\resources\\credit_debit\\GCB.xml");
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(xmlFile);

		// Find the element you want to modify (e.g., SessionId)
		Element rootElement = doc.getRootElement();
		rootElement.getChild("AllowKeyedEntry").setText(AllowKeyedEntry);
		rootElement.getChild("LookUpFlag").setText(LookUpFlag);

		// Create a custom Format that omits the XML declaration
		Format customFormat = Format.getRawFormat().setOmitDeclaration(true);

		// Save the modified XML to a file without the XML declaration
		XMLOutputter xmlOutput = new XMLOutputter(customFormat);
		String modifiedXml = xmlOutput.outputString(doc);
		return modifiedXml;

	}
	public static String fsa_GCB_Request_Modified() throws JDOMException, IOException {
		
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream("config.properties")) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Accessing properties
		// String SaleType = properties.getProperty("SaleType");
		String LookUpFlag = properties.getProperty("LookUpFlag");
		String AllowKeyedEntry = properties.getProperty("AllowKeyedEntry");
		
		File xmlFile = new File("./src\\test\\resources\\credit_debit\\GCB.xml");
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(xmlFile);
		
		// Find the element you want to modify (e.g., SessionId)
		Element rootElement = doc.getRootElement();
		rootElement.getChild("AllowKeyedEntry").setText("Y");
		rootElement.getChild("LookUpFlag").setText(LookUpFlag);
		
		// Create a custom Format that omits the XML declaration
		Format customFormat = Format.getRawFormat().setOmitDeclaration(true);
		
		// Save the modified XML to a file without the XML declaration
		XMLOutputter xmlOutput = new XMLOutputter(customFormat);
		String modifiedXml = xmlOutput.outputString(doc);
		return modifiedXml;
		
	}
}
