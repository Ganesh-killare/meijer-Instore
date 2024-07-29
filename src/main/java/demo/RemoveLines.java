package demo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RemoveLines {
    public static void main(String[] args) {
        // Input file path
        String inputFilePath = "./src\\main\\resources\\aesdkLogs\\Store0065POS113_Bypass.txt";
        // Output file path
        String outputFilePath = "modified_data.txt";

        // Flag to determine if the line should be printed
        // Counter to keep track of lines
        int lineNumber = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            // Read all lines from the input file
            String line;
            while ((line = reader.readLine()) != null) {
                // Print the line if it's the first or fourth line
                if (lineNumber == 1 || lineNumber == 4) {
                    System.out.println(line);
                }

                // Increment the line number and reset to 1 after 4
                lineNumber = (lineNumber % 4) + 1;
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

}
