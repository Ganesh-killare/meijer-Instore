package requestbuilder;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import utilities.DateUtilities;

public class CloseRequest {
	

	static String formattedTime =	DateUtilities.generateDateTimeAndInvoice().get(0);
	static String finalDate =	DateUtilities.generateDateTimeAndInvoice().get(1);
	static String invoiceNumber =	DateUtilities.generateDateTimeAndInvoice().get(2);



    public static String buildXMLRequest() {
        try {
            Document transRequestDocument = createSampleTransRequestDocument();  

            // Convert the modified document back to a string
            return documentToString(transRequestDocument);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Document createSampleTransRequestDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Create root element
            Element CloseTransactionRequest = doc.createElement("CloseTransactionRequest");
            doc.appendChild(CloseTransactionRequest);

            // Add child elements in the desired sequence
            appendElementWithValue(doc, CloseTransactionRequest, "POSID", "S00784R0100");
            appendElementWithValue(doc, CloseTransactionRequest, "APPID", "01");
            appendElementWithValue(doc, CloseTransactionRequest, "CCTID", "01");
            appendElementWithValue(doc, CloseTransactionRequest, "ADSDKSpecVer", "6.14.8");
            appendElementWithValue(doc, CloseTransactionRequest, "SessionId", "12345");
            appendElementWithValue(doc, CloseTransactionRequest, "CloseReasonCode", "TRANSACTION_COMPLETE");
            appendElementWithValue(doc, CloseTransactionRequest, "OrigAurusPayTicketNum", "");
            appendElementWithValue(doc, CloseTransactionRequest, "OrigTransactionIdentifier", "");
            appendElementWithValue(doc, CloseTransactionRequest, "InvoiceNumber", invoiceNumber);
            appendElementWithValue(doc, CloseTransactionRequest, "TransactionTime", formattedTime);
            appendElementWithValue(doc, CloseTransactionRequest, "TransactionDate", finalDate);
            Element ecommInfoElement = doc.createElement("ECOMMInfo");
            CloseTransactionRequest.appendChild(ecommInfoElement);
            appendElementWithValue(doc, ecommInfoElement, "MerchantIdentifier", "111111111111"); // Set value to null for empty tag
            appendElementWithValue(doc, ecommInfoElement, "StoreId", "11111"); // Set value to null for empty tag
            appendElementWithValue(doc, ecommInfoElement, "TerminalId", "11111111"); // Set value to null for empty tag
            
            appendElementWithValue(doc, CloseTransactionRequest, "EndWIC", "0");
            appendElementWithValue(doc, CloseTransactionRequest, "ProcessingMode","0");
            appendElementWithValue(doc, CloseTransactionRequest, "LanguageIndicator","");
            appendElementWithValue(doc, CloseTransactionRequest, "ClerkID", "111");

            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void appendElementWithValue(Document doc, Element parentElement, String tagName, String textContent) {
        Element element = doc.createElement(tagName);

        if (textContent != null) {
            element.appendChild(doc.createTextNode(textContent));
        } else {
            // Explicitly create an empty text node and append it
            Text emptyTextNode = doc.createTextNode("");
            element.appendChild(emptyTextNode);
        }

        parentElement.appendChild(element);
    }


    private static String documentToString(Document document) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            // Set properties for pretty formatting without the XML declaration
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            // Remove unnecessary whitespace
            document.normalize();

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            // Remove empty lines between tags
            String result = writer.toString().replaceAll("(?m)^[ \t]*\r?\n", "");

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

   
    public static String CLOSE_REQUEST() {
        try {
        	
        	// take a basic request
        	String xml = buildXMLRequest();
        	
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
         
            
            return documentToString(document);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
