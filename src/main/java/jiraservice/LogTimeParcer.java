package jiraservice;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogTimeParcer {

    public static void main(String[] args) {
        String logFilePath = "./aesdkLogs\\AURUS_ESDK_20240530.log"; // Update with your log file path
        String outputFilePath = "./aesdkLogs//Results//STORE0065POS017.txt"; // Path to the output text file

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
             BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {

            String line;
            Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})");
            Map<String, String> requestResponseMap = new HashMap<>();
            Map<String, String> tagsMap = new HashMap<>();
            tagsMap.put("::  <CloseTransactionRequest>", "CloseTransactionRequest");  // 
            tagsMap.put("::<CloseTransactionResponse>", "CloseTransactionResponse");
            tagsMap.put("::  <ByPassScreenRequest>", "ByPassScreenRequest");
            tagsMap.put("::<ByPassScreenResponse>", "ByPassScreenResponse");
            tagsMap.put("::  <ShowScreenRequest>", "ShowScreenRequest");
            tagsMap.put("::<ShowScreenResponse>", "ShowScreenResponse");
            tagsMap.put("::  <TransRequest>", "TransRequest");
            tagsMap.put("TransResponse : ", "TransResponse : ");
            tagsMap.put("::  <GetCardBINRequest>", "GetCardBINRequest");
            tagsMap.put("::<GetCardBINResponse>", "GetCardBINResponse : ");
            tagsMap.put("::  <GetUserInputRequest>", "GetUserInputRequest");
            tagsMap.put("::<GetUserInputResponse>", "GetUserInputResponse : ");
            tagsMap.put("::  <SignatureRequest>", "SignatureRequest");
            tagsMap.put("::<SignatureResponse>", "SignatureResponse : ");   
            
  
            
                
            
            while ((line = br.readLine()) != null) {
                for (Map.Entry<String, String> entry : tagsMap.entrySet()) {
                    if (line.contains(entry.getKey())) {
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            requestResponseMap.put(entry.getValue(), matcher.group(1));
                            writer.write( " " + matcher.group(1)+"  <" + entry.getValue()+ ">"+ "\n");
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    System.out.println("File is Saved");}
}