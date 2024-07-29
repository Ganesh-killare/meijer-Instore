package reporting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AESDKData {
	public void  writeAESDKRequestAndResponse(String request, String response) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// Get the current date and format it as a string
		String currentDateAsString = dateFormat.format(new Date());
		// Specify the file path
		String filePath = "./src\\test\\resources\\AESDK\\AESDK" + currentDateAsString + ".txt";

		// Use try-with-resources to automatically close the resources (e.g.,
		// BufferedWriter)
		try (
				BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			writer.newLine();
			writer.write("AESDK Request");
			writer.newLine();
			writer.write(request);
			writer.newLine();
			writer.write("_".repeat(50));
			writer.newLine();
			writer.newLine();
			writer.newLine();
			writer.write("AESDK Response");
			writer.newLine();
			writer.newLine(); // Add a newline after the separator
			writer.write(response);
			writer.write("=".repeat(50));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
