package base;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import org.jdom2.JDOMException;

public class AESDKClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final String serverAddress;
    private final int serverPort;

    public AESDKClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void establishConnection() throws UnknownHostException, IOException {
        // Initialize the socket and streams only once
        if (socket == null || socket.isClosed()) {
            socket = new Socket(serverAddress, serverPort);
            OutputStream outputStream = socket.getOutputStream();
            out = new PrintWriter(outputStream, true);
            InputStream inputStream = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inputStream));
        }
    }

    public void sendRequestToAESDK(String data) throws IOException, InterruptedException {
        // Ensure the connection is established before sending data
        if (socket == null || socket.isClosed()) {
            throw new IOException("Socket is not connected.");
        }

     
        out.println(data);

        // Optionally wait for a while if needed (e.g., for server processing)
        // Thread.sleep(500);
    }

    public String receiveResponseFromAESDK() throws IOException, InterruptedException, JDOMException {
        // Ensure the connection is established before receiving data
        if (socket == null || socket.isClosed()) {
            throw new IOException("Socket is not connected.");
        }

        String response = in.readLine();
      
        System.out.println(response);
        return response;
    }

    public void closeConnection() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
