package responsemodification;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class Void_Response_Parameters {
	


	private static Document document;

	public Void_Response_Parameters(String xml) throws Exception {
		SAXBuilder saxBuilder = new SAXBuilder();
		document = saxBuilder.build(new StringReader(xml));
	}

	public static String getElementValue(String elementName) {
		return getElementValue(document.getRootElement(), elementName);
	}

	private static String getElementValue(Element element, String elementName) {
		if (element == null) {
			return null;
		}

		if (element.getName().equals(elementName)) {
			return element.getText();
		}

		List<Element> children = element.getChildren();
		for (Element child : children) {
			String result = getElementValue(child, elementName);
			if (result != null) {
				return result;
			}

		}
		return null;

	}

	public static List<String> print_Void_Response(String[] parameters) {
		List<String> GCB_Response = new ArrayList<>();
		System.out.print( "Void :  ");
		for (int i = 0; i <= parameters.length - 1; i++) {
			String GCBParameters = getElementValue(parameters[i]);
			GCB_Response.add(GCBParameters);
		}
		System.out.println(GCB_Response);
		return GCB_Response;
	}
	

}
