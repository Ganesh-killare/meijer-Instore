package apisequence;

import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.github.javafaker.Faker;

import utilities.DateUtilities;

public class CheckRequest {
	static Faker faker = new Faker();

	public static String modified_Check_Request( ) {

		 // Amount 
			/*
			 * DecimalFormat df = new DecimalFormat("00.00");
			 * 
			 * double cashback = ThreadLocalRandom.current().nextDouble(00.00, 99.00);
			 * double tender = ThreadLocalRandom.current().nextDouble(00.00, 99.00);
			 * 
			 * double Total = cashback +tender ; String cashbackAmount =
			 * df.format(cashback); String tenderamount = df.format(tender); String
			 * transactionTotal = df.format(Total);
			 */
		
		

		// Date & Time with Invoice Number


		String formattedTime =	DateUtilities.generateDateTimeAndInvoice().get(0);
		String finalDate =	DateUtilities.generateDateTimeAndInvoice().get(1);
		String invoiceNumber =	DateUtilities.generateDateTimeAndInvoice().get(2);


		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\AccountLookUp\\check.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			/*
			 * root.getChild("TransAmountDetails").getChild("CashBackAmount").setText(
			 * cashbackAmount);
			 * root.getChild("TransAmountDetails").getChild("TenderAmount").setText(
			 * tenderamount);
			 * root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(
			 * transactionTotal);
			 */
			root.getChild("InvoiceNumber").setText(invoiceNumber);
			root.getChild("TransactionDate").setText(finalDate);
			root.getChild("TransactionTime").setText(formattedTime);

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
