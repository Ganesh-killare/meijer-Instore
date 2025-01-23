package base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

public class Demo {
	
	
	public static void main(String[] args) {

		
	Response res =	RestAssured.get("https://reqres.in/api/users?page=2");
	
	}

}
