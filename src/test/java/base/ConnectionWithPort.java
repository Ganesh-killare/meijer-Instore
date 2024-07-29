package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ConnectionWithPort {
	public String serverAddress = getHostIP();
	public int serverPort = 8060;
	PrintWriter out = null;
	Socket socket = null;

	public void sendRequestToAESDK(String data) throws UnknownHostException, IOException, InterruptedException {
		socket = new Socket(serverAddress, serverPort);
		OutputStream outputStream = socket.getOutputStream();
		out = new PrintWriter(outputStream, true);
		// System.out.println("The Request is send  send is "+ data);
		out.println(data);
		//Thread.sleep(500);
	}
	
	private static String getHostIP() {
		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
		String ipAddress = localhost.getHostAddress();
		return ipAddress;
	}

	public String receiveResponseToAESDK() throws IOException, InterruptedException, JDOMException {
		InputStream inputStream = socket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String response = in.readLine();
		// Parse the XML string
		  SAXBuilder saxBuilder = new SAXBuilder();
          Document document = saxBuilder.build(new StringReader(response));

          // Create a custom format for pretty printing
          Format format = Format.getPrettyFormat();

          // Create an XMLOutputter with the custom format
          XMLOutputter xmlOutput = new XMLOutputter(format);

          // Convert the document to a string with pretty formatting
          StringWriter stringWriter = new StringWriter();
          xmlOutput.output(document, stringWriter);

          return stringWriter.toString();

		
	}
	

}
