package responsevalidator;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import utilities.Utils;

public class Response_Parameters {

	private Document document;

	public Response_Parameters(String xml) throws Exception {
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			String sanitizedXml = xml.replaceAll("[^\\x20-\\x7E]", "");

			document = saxBuilder.build(new StringReader(sanitizedXml));
		} catch (Exception e) {
			System.err.println("We are getting something fishy in XML");
			System.out.println(xml);
		}

	}

	public String getParameterValue(String elementName) {
		try {
			return getElementValue(document.getRootElement(), elementName);
		} catch (Exception e) {
			return null;
		}
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

	public List<String> print_Response(String trans, List<String> parameters) {
		System.out.print(trans + " : ");
		List<String> GCB_Response = new ArrayList<>();
		for (String element : parameters) {

			String GCBParameters;

			if (element.contains("AID")) {
				GCBParameters = getAIDContent();

			} else {
				GCBParameters = getParameterValue(element);
			}

			GCB_Response.add(GCBParameters);
		}
		System.out.println(GCB_Response);
		return GCB_Response;
	}
	public List<String> print_Response(String trans, String [] parameters) {
		System.out.print(trans + " : ");
		List<String> GCB_Response = new ArrayList<>();
		for (String element : parameters) {
			
			String GCBParameters;
			
			if (element.contains("AID")) {
				GCBParameters = getAIDContent();
				
			} else {
				GCBParameters = getParameterValue(element);
			}
			
			GCB_Response.add(GCBParameters);
		}
		System.out.println(GCB_Response);
		return GCB_Response;
	}

	public List<String> cardData() {

		return null;

	}

	public String getAIDContent() {
		try {
			Element root = document.getRootElement();
			Element transDetailsData = root.getChild("TransDetailsData");

			if (transDetailsData != null) {
				Element transDetailData = transDetailsData.getChild("TransDetailData");

				if (transDetailData != null) {
					Element additionalReceiptInfo = transDetailData.getChild("AdditionalReceiptInfo");

					if (additionalReceiptInfo != null) {
						List<Element> lines = additionalReceiptInfo.getChildren("Line");

						if (lines != null && !lines.isEmpty()) {
							for (Element line : lines) {
								String content = line.getTextNormalize();
								// Return the first AID found
								if (content.startsWith("AID:")) {
									return content;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // Consider using logging in production code
		}

		return " "; // Return a default message if no AID found
	}

}
