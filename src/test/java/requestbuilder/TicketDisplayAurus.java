package requestbuilder;

import java.io.StringReader;
import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;

import com.github.javafaker.Faker;

public class TicketDisplayAurus {

	public static String request() {
		ProductXmlResult result = generateProductsData();
		String productData = result.getProductData();

		int totalAmount = result.getTotalAmount();
		int discountAmount = (totalAmount * 20) / 100;
		int discountedAmount = totalAmount - discountAmount;
		int taxAmount = (discountedAmount * 20) / 100;
		int totalTransAmount = discountedAmount + taxAmount;

		int totalQuantity = result.TransTotalQuantity();

		String FTaxdAmount = String.valueOf(taxAmount);
		String FDiscountedAmount = String.valueOf(discountAmount);
		String amount = String.valueOf(totalTransAmount);

		try {
			// Create the root element (CCTTicketDisplayRequest)
			Element root = new Element("CCTTicketDisplayRequest");

			// Modify the parameters
			root.addContent(new Element("POSID").setText("01"));
			root.addContent(new Element("APPID").setText("01"));
			root.addContent(new Element("CCTID").setText("01"));
			root.addContent(new Element("ClerkID").setText("111"));
			root.addContent(new Element("DisplayFlag").setText("07"));
			root.addContent(new Element("AllowCardReader").setText("Y"));
			root.addContent(new Element("ADSDKSpecVer").setText("6.13.0"));
			root.addContent(new Element("SessionId").setText("12345"));
			root.addContent(new Element("HeaderText"));
			root.addContent(new Element("FooterText"));
			root.addContent(new Element("ButtonText1").setText("Next"));
			root.addContent(new Element("ButtonText2"));
			root.addContent(new Element("ButtonText3"));

			root.addContent(new SAXBuilder().build(new InputSource(new StringReader(productData))).detachRootElement());

			root.addContent(new Element("TransTotalDiscountAmt").setText(FDiscountedAmount + ".00"));
			root.addContent(new Element("TransTotalQuantity").setText(String.valueOf(totalQuantity)));
			root.addContent(new Element("TransSubTotalAmount"));
			root.addContent(new Element("TaxAmount").setText(FTaxdAmount + ".00"));
			root.addContent(new Element("ServicesTotalAmount").setText("0.00"));
			root.addContent(new Element("TransTotalAmount").setText(amount + ".00"));
			root.addContent(new Element("PONumber"));
			root.addContent(new Element("LanguageIndicator"));
			root.addContent(new Element("CashBackFlag").setText("0"));
			root.addContent(new Element("TransactionType"));

			// Create the XML document
			Document doc = new Document(root);

			// Format the XML output
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

	private static ProductXmlResult generateProductsData() {
		Faker faker = new Faker();
		int numProducts = faker.random().nextInt(1, 4);

		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append("<TicketProductData>\n");
		xmlBuilder.append("<TicketCount>1</TicketCount>\n");
		xmlBuilder.append("<Tickets>\n");
		xmlBuilder.append("<Ticket>\n");
		xmlBuilder.append("<TicketNumber>001</TicketNumber>\n");
		xmlBuilder.append("<ProductCount>").append(numProducts).append("</ProductCount>\n");
		xmlBuilder.append("<Products>\n");

		int totalAmount = 0;

		for (int i = 1; i <= numProducts; i++) {
			int productPrice = faker.random().nextInt(2, 30); // Generate random price for the product (in cents)
			totalAmount += productPrice;

			xmlBuilder.append("<Product>\n");
			xmlBuilder.append("<ProductID>").append(i).append("</ProductID>\n");
			xmlBuilder.append("<ProductName>Product ").append(i).append("</ProductName>\n");
			xmlBuilder.append("<ProductTypeIndicator>1</ProductTypeIndicator>\n");
			xmlBuilder.append("<ProductDataType></ProductDataType>\n");
			xmlBuilder.append("<ServiceLevel>F</ServiceLevel>\n");
			xmlBuilder.append("<ProductCode></ProductCode>\n");
			xmlBuilder.append("<ProductShortDescription>Skechers  ka jutta ").append(i).append("</ProductShortDescription>\n");
			xmlBuilder.append("<ProductStatementDescriptor></ProductStatementDescriptor>\n");
			xmlBuilder.append("<UnitOfMeasure>U</UnitOfMeasure>\n");
			xmlBuilder.append("<ProductUnitPrice>").append(productPrice).append("</ProductUnitPrice>\n");
			xmlBuilder.append("<ProductShipDate></ProductShipDate>\n");
			xmlBuilder.append("<ProductCommodityCode></ProductCommodityCode>\n");
			xmlBuilder.append("<ProductCreditDebitIndicator></ProductCreditDebitIndicator>\n");
			xmlBuilder.append("<ProductDiscountFlag>1</ProductDiscountFlag>\n");
			xmlBuilder.append("<DiscountPercentage></DiscountPercentage>\n");
			xmlBuilder.append("<ProductDiscount>1.00</ProductDiscount>\n");
			xmlBuilder.append("<DepartmentID>").append(String.format("%03d", i)).append("</DepartmentID>\n");
			xmlBuilder.append("<DepartmentName>Electronic's</DepartmentName>\n");
			xmlBuilder.append("<Quantity>").append(1).append("</Quantity>\n");
			xmlBuilder.append("<Price>").append(productPrice).append(".00</Price>\n");
			xmlBuilder.append("<Tax></Tax>\n");
			xmlBuilder.append("<ProductTaxRate></ProductTaxRate>\n");
			xmlBuilder.append("<ProductTypeFlag></ProductTypeFlag>\n");
			xmlBuilder.append("<ProductNetGrossIndicator></ProductNetGrossIndicator>\n");
			xmlBuilder.append("<SubscriptionEvery></SubscriptionEvery>\n");
			xmlBuilder.append("<SubscriptionEveryPeriod></SubscriptionEveryPeriod>\n");
			xmlBuilder.append("<ProductQuantityDecimal></ProductQuantityDecimal>\n");
			xmlBuilder.append("<LineProductTotal></LineProductTotal>\n");
			xmlBuilder.append("<ReservedField1></ReservedField1>\n");
			xmlBuilder.append("<ReservedField2></ReservedField2>\n");
			xmlBuilder.append("<ShippingAddressId></ShippingAddressId>\n");
			xmlBuilder.append("</Product>\n");
		}

		xmlBuilder.append("</Products>\n");
		xmlBuilder.append("</Ticket>\n");
		xmlBuilder.append("</Tickets>\n");
		xmlBuilder.append("</TicketProductData>\n"); // Close TicketProductData

		return new ProductXmlResult(xmlBuilder.toString(), totalAmount, numProducts);
	}

	static class ProductXmlResult {
		private String xml;
		private int totalAmount;
		private int TotalQuantity;

		public ProductXmlResult(String xml, int totalAmount, int TotalQuantity) {
			this.xml = xml;
			this.totalAmount = totalAmount;
			this.TotalQuantity = TotalQuantity;
		}

		public int TransTotalQuantity() {

			return TotalQuantity;
		}

		public String getProductData() {
			return xml;
		}

		public int getTotalAmount() {
			return totalAmount;
		}
	}

	@Test
	void test() {

		String ticketRequest = TicketDisplayAurus.request();
		System.out.println(ticketRequest);

	}
}
