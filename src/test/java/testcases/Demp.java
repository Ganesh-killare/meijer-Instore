package testcases;

import org.testng.annotations.Test;

import com.github.javafaker.Faker;

public class Demp {
	@Test
	public void test() {
		Faker faker = new Faker();

		// Generate a random double with two decimal places
		double randomDouble = Double.parseDouble(faker.number().digits(2) + "." + faker.number().numberBetween(30, 99));

		System.out.println("Generated Double: " + randomDouble);
	}
}
