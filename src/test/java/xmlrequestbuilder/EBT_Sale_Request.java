package xmlrequestbuilder;
import base.POS_APIs;

import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.github.javafaker.Faker;

import utilities.Utils;

public class EBT_Sale_Request {

	static Faker faker = new Faker();

	public static String modified_Sale_Request(String cardToken , 	String transType ) throws UnknownHostException, IOException, InterruptedException, JDOMException {



		  
		   String  amt = POS_APIs.generateTransactionAmount();
		  	
			String transactionAmount = amt+".00";
			if(transactionAmount.equalsIgnoreCase("45.00")) {
				transactionAmount= "10.00";
				
			}

		// Date & Time with Invoice Number

		String formattedTime = Utils.generateDateTimeAndInvoice().get(0);
		String finalDate = Utils.generateDateTimeAndInvoice().get(1);
		String invoiceNumber = Utils.generateDateTimeAndInvoice().get(2);

		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\EBT\\sale.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			root.getChild("TransAmountDetails").getChild("TenderAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("EBTAmount").setText(transactionAmount);
			root.getChild("CardToken").setText(cardToken);
			// root.getChild("ECOMMInfo").getChild("CardIdentifier").setText(cardIdentifier);
			// root.getChild("CRMToken").setText(CRMToken);
			root.getChild("TransactionType").setText(transType);
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

	public static String P_EBT_SALE_REQUEST(String cardToken, 	String transType,  String amt) {
		

		  
		  

		// Date & Time with Invoice Number

		String formattedTime = Utils.generateDateTimeAndInvoice().get(0);
		String finalDate = Utils.generateDateTimeAndInvoice().get(1);
		String invoiceNumber = Utils.generateDateTimeAndInvoice().get(2);

		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\EBT\\sale.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			root.getChild("TransAmountDetails").getChild("TenderAmount").setText(amt);
			root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(amt);
			root.getChild("TransAmountDetails").getChild("EBTAmount").setText(amt);
			root.getChild("CardToken").setText(cardToken);
			// root.getChild("ECOMMInfo").getChild("CardIdentifier").setText(cardIdentifier);
			// root.getChild("CRMToken").setText(CRMToken);
			root.getChild("TransactionType").setText(transType);
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
