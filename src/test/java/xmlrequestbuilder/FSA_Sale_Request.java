package xmlrequestbuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.github.javafaker.Faker;

import utilities.Utils;

public class FSA_Sale_Request {

	static Faker faker = new Faker();

	public static String modified_FSA_SALE_Request(String cardToken, String cardIdentifier, String CRMToken ,  String transType) {
		

		 Properties properties = new Properties();
	        try (FileInputStream input = new FileInputStream("config.properties")) {
	            properties.load(input);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        // Accessing properties
	        String SaleType = properties.getProperty("TokenType");
	     if(SaleType.equalsIgnoreCase("CRMToken")) {
	    	 cardToken=null;
	    	 cardIdentifier = null;
	     }
	     else if(SaleType.equalsIgnoreCase("CardIdentifier")) {
	    	 cardToken=null;
	    	 CRMToken = null;
	     }
	     else if(SaleType.equalsIgnoreCase("CardToken")) {
	    	 CRMToken=null;
	    	 cardIdentifier = null;
	     }
	

		
		

		   // Get the current date and time

		String formattedTime =	Utils.generateDateTimeAndInvoice().get(0);
		String finalDate =	Utils.generateDateTimeAndInvoice().get(1);
		String invoiceNumber =	Utils.generateDateTimeAndInvoice().get(2);

	     int Prescription = ThreadLocalRandom.current().nextInt(0, 7);
	        int VisionOptical = ThreadLocalRandom.current().nextInt(0, 7);
	        int CoPayment = ThreadLocalRandom.current().nextInt(0, 7);
	        int Dental = ThreadLocalRandom.current().nextInt(0, 7);

	        int amount = Prescription + VisionOptical + CoPayment + Dental;

	        // DecimalFormat to format integers with exactly 2 decimal places
	        DecimalFormat df = new DecimalFormat("00.00");

	        // Format the integers to have exactly 2 decimal places
	        String PrescriptionAmount = df.format(Prescription);
	        String VisionOpticalAmount = df.format(VisionOptical);
	        String CoPaymentAmount = df.format(CoPayment);
	        String DentalAmount = df.format(Dental);

	        // Format the total transaction amount
	        String transactionAmount = df.format(amount);
		// String transactionAmount = "05.05";


		
		
	
		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\FSA\\sale.xml");

			// Get the root element (TransRequest)
			Element root = doc.getRootElement();

			// Modify the parameters
			if (CRMToken != null || cardIdentifier !=null) {
			//	root.getChild("CardExpiryDate").setText("1225");
				
			}
			root.getChild("TransAmountDetails").getChild("TenderAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("FSAAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("HealthCareAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("PrescriptionAmount").setText(PrescriptionAmount);
			root.getChild("TransAmountDetails").getChild("VisionOpticalAmount").setText(VisionOpticalAmount);
			root.getChild("TransAmountDetails").getChild("CoPaymentAmount").setText(CoPaymentAmount);
			root.getChild("TransAmountDetails").getChild("DentalAmount").setText(DentalAmount);
			
			
			
			root.getChild("CardToken").setText(cardToken);
			//root.getChild("ECOMMInfo").getChild("CardIdentifier").setText(cardIdentifier);
			//root.getChild("CRMToken").setText(CRMToken);
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
	public static String P_FSA_SALE_Request(String cardToken, String cardIdentifier, String CRMToken,  String transType ,  double  Amount) {
		
		
		// Date & Time with Invoice Number
		
	
		
		// Get the current date and time
		
		String formattedTime =	Utils.generateDateTimeAndInvoice().get(0);
		String finalDate =	Utils.generateDateTimeAndInvoice().get(1);
		String invoiceNumber =	Utils.generateDateTimeAndInvoice().get(2);
		
		// Transaction Amount 
		
		DecimalFormat df = new DecimalFormat("00.00");
		
	    double Prescription = 25.00;
        double VisionOptical = 20.00;
        double CoPayment = 30.00;
        double Dental = 25.00 + Amount;  // Adjusted to make the total 100.13

		
		double amount = Prescription + VisionOptical + CoPayment + Dental ;
		
		
		String PrescriptionAmount = df.format(Prescription);
		String VisionOpticalAmount = df.format(VisionOptical);
		String CoPaymentAmount = df.format(CoPayment);
		String DentalAmount = df.format(Dental);
		
		// Format the double to have exactly 2 decimal places
		
		String transactionAmount = df.format(amount);
		
		
		
		
		
		try {
			// Load the XML file
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build("./src\\test\\resources\\FSA\\sale.xml");
			
			// Get the root element (TransRequest)
			Element root = doc.getRootElement();
			
			// Modify the parameters
			if (CRMToken != null || cardIdentifier !=null) {
				//	root.getChild("CardExpiryDate").setText("1225");
				
			}
			root.getChild("TransAmountDetails").getChild("TenderAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("TransactionTotal").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("FSAAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("HealthCareAmount").setText(transactionAmount);
			root.getChild("TransAmountDetails").getChild("PrescriptionAmount").setText(PrescriptionAmount);
			root.getChild("TransAmountDetails").getChild("VisionOpticalAmount").setText(VisionOpticalAmount);
			root.getChild("TransAmountDetails").getChild("CoPaymentAmount").setText(CoPaymentAmount);
			root.getChild("TransAmountDetails").getChild("DentalAmount").setText(DentalAmount);
			
			
			
			root.getChild("CardToken").setText(cardToken);
			//root.getChild("ECOMMInfo").getChild("CardIdentifier").setText(cardIdentifier);
			//root.getChild("CRMToken").setText(CRMToken);
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
