package demo;

import java.util.HashMap;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.util.Map;
import java.util.Random;
import com.github.javafaker.Faker;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.Random;
public class TicketDisplay {  public static void main(String[] args) {
    try {
        // Parse the XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("your_xml_file.xml"));

        // Get the root element
        Element root = doc.getDocumentElement();

        // Add products randomly
        addRandomProducts(doc, root);

        // Update TransactionTotalAmount
        updateTransactionTotalAmount(doc, root);

        // Write the updated XML back to the file
        writeXmlToFile(doc, "updated_xml_file.xml");

    } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
        e.printStackTrace();
    }
}

private static void addRandomProducts(Document doc, Element root) {
    NodeList products = root.getElementsByTagName("Product");

    Random random = new Random();

    // Add random products between 1 to 9 times
    int numProductsToAdd = random.nextInt(9) + 1;

    for (int i = 0; i < numProductsToAdd; i++) {
        Element product = (Element) products.item(random.nextInt(products.getLength()));
        Node clonedProduct = product.cloneNode(true);
        root.appendChild(clonedProduct);
    }
}

private static void updateTransactionTotalAmount(Document doc, Element root) {
    NodeList priceElements = root.getElementsByTagName("Price");
    double totalAmount = 0.0;

    for (int i = 0; i < priceElements.getLength(); i++) {
        Element priceElement = (Element) priceElements.item(i);
        totalAmount += Double.parseDouble(priceElement.getTextContent());
    }

    // Update TransactionTotalAmount
    Element transTotalAmount = (Element) root.getElementsByTagName("TransTotalAmount").item(0);
    transTotalAmount.setTextContent(String.format("%.2f", totalAmount));
}

private static void writeXmlToFile(Document doc, String filename) {
    try {
        // Write the updated XML back to the file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);

        System.out.println("XML file updated successfully!");

    } catch (TransformerException e) {
        e.printStackTrace();
    }
}
}