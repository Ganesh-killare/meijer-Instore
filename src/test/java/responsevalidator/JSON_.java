package responsevalidator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JSON_ {

	private JsonNode rootNode;

	public JSON_(String json) throws Exception {
		try {
			// Initialize Jackson ObjectMapper to parse JSON
			ObjectMapper objectMapper = new ObjectMapper();
			// Sanitize and parse the JSON string
			String sanitizedJson = json.replaceAll("[^\\x20-\\x7E]", "");
			rootNode = objectMapper.readTree(sanitizedJson);
		} catch (Exception e) {
			System.err.println("Error while processing JSON");
			System.out.println(json);
			throw e;
		}
	}
	public String getParameterValue(String elementName) {
	    try {
	        // Traverse the JSON using the element name in the TransResponse
	        JsonNode node = rootNode.at("/TransResponse/" + elementName); // Using 'at' for absolute path search
	        String parameterValue = null;

	        // If the parameter is not found in TransResponse, fall back to TransDetailsData
	        if (node.isMissingNode()) {
	            // TransDetailData is an array, so we need to iterate through the elements
	            JsonNode transDetailDataArray = rootNode.at("/TransResponse/TransDetailsData/TransDetailData");

	            if (transDetailDataArray.isArray()) {
	                // Traverse through each object in the array
	                for (JsonNode transDetail : transDetailDataArray) {
	                    node = transDetail.get(elementName);
	                    if (node != null && !node.isMissingNode()) {
	                        parameterValue = node.asText();
	                        System.out.println("Found in TransDetailData: " + parameterValue);
	                        break; // If found, exit the loop
	                    }
	                }
	            }
	        } else {
	            // If found in TransResponse
	            parameterValue = node.asText();
	        }

	        // Return the value if found, or null if not found
	        return parameterValue;

	    } catch (Exception e) {
	        e.printStackTrace();  // Log the exception for debugging
	        return null;  // If any error occurs, return null
	    }
	}


	// Method to print the response for List<String> parameters
	public List<String> print_Response(String trans, List<String> parameters) {
		System.out.print(trans + " : ");
		List<String> responseValues = new ArrayList<>();
		for (String element : parameters) {
			String parameterValue = getParameterValue(element); // Get the parameter value
			responseValues.add(parameterValue); // Add the value to the response list
		}
		System.out.println(responseValues); // Print the response values
		return responseValues;
	}

	// Method to print the response for String[] parameters
	public List<String> print_Response(String trans, String[] parameters) {
		System.out.print(trans + " : ");
		List<String> responseValues = new ArrayList<>();
		for (String element : parameters) {
			String parameterValue = getParameterValue(element); // Get the parameter value
			responseValues.add(parameterValue); // Add the value to the response list
		}
		System.out.println(responseValues); // Print the response values
		return responseValues;
	}
}