package xmlrequestbuilder;
import base.POS_APIs;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.github.javafaker.Faker;

import utilities.Utils;

public class PLCC_Sale_Request_Modification {
	static Faker faker = new Faker();

	public static String modified_PLCCSale_Request(String cardToken, String cardIdentifier, String CRMToken, String transType) throws UnknownHostException, IOException, InterruptedException, JDOMException {
		
		
		
		
		

	  // Date & Time with Invoice Number
		String formattedTime =	Utils.generateDateTimeAndInvoice().get(0);
		String finalDate =	Utils.generateDateTimeAndInvoice().get(1);
		String invoiceNumber =	Utils.generateDateTimeAndInvoice().get(2);


     // Transaction Amount 
		 double amount = ThreadLocalRandom.current().nextDouble(30.0, 99.99);

		 
		   String  transactionAmount = POS_APIs.generateTransactionAmount();
		  	
			
;
		
		
		
	
		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\plccAndCbcc\\sale.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			if (CRMToken != null || cardIdentifier !=null) {
			//	root.getChild("CardExpiryDate").setText("1225");
				
			}
			root.getChild("TransAmountDetails").getChild("TenderAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(transactionAmount);
			root.getChild("CardToken").setText(cardToken);
			root.getChild("ECOMMInfo").getChild("CardIdentifier").setText(cardIdentifier);
			root.getChild("CRMToken").setText(CRMToken);
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
