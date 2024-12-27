package utilities;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class LogResponse {

	private Document document;

	public LogResponse(String xml) throws Exception {
		SAXBuilder saxBuilder = new SAXBuilder();
		document = saxBuilder.build(new StringReader(xml));
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

	public List<String> print_Response(String trans, List<String> parameters, String CardToken) {
		List<String> response = new ArrayList<>();

		for (int i = 0; i < parameters.size(); i++) {
			String responseParameter = getParameterValue(parameters.get(i));
			if (i == 15 && trans.contains("Sale")) {
				responseParameter = getAIDContent();
			}
			response.add(responseParameter);
		}

		String transactionType = response.get(4);
		if (transactionType.equals(null)) {
			transactionType = "GCB";
			System.out.println("This is a GCB Request");
		}
		switch (transactionType) {
		case "1":
		case "21":
			System.out.print("Sale" + " : ");
			System.out.println(response);
			response.add(1, "Sale");

			break;
		case "2":
		case "22":
			if (CardToken == null || CardToken.equalsIgnoreCase("null")) {
				System.out.print("Refund" + " : ");
				System.out.println(response);
				response.add(1, "Refund");
			} else {
				System.out.print("Refund Without Sale" + " : ");
				System.out.println(response);
				response.add(1, "Refund Without Sale");
			}
			break;

		case "5":
			System.out.print("Void" + " : ");
			System.out.println(response);
			response.add(1, "Void");
			break;
		case "91":
			System.out.print("Check Sale" + " : ");
			System.out.println(response);
			response.add(1, "Check Sale");
			break;
		case "GCB":
			System.out.print("GCB" + " : ");
			System.out.println(response);
			response.add(1, "GCB");
			break;
		default:
			System.out.print("Aurus" + " : ");
			System.out.println(response);
			response.add(1, "Aurus");
			break;
		}

		return response;
	}

	public List<String> cardData() {

		return null;

	}

	// Method to extract content within <Line>AID:A0000000041010</Line>
	public String getAIDContent() {
		try {
			Element root = document.getRootElement();
			Element additionalReceiptInfo = root.getChild("TransDetailsData").getChild("TransDetailData")
					.getChild("AdditionalReceiptInfo");
			List<Element> lines = additionalReceiptInfo.getChildren("Line");
			if (!lines.isEmpty()) {
				for (Element line : lines) {
					String content = line.getTextNormalize();
					if (content.startsWith("AID:")) {
						return content;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "AID Not Receive ";
	}

}