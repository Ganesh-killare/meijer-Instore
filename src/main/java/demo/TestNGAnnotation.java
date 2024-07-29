package demo;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestNGAnnotation {
	
	
	@Test

	public void testSeverity () {
		Assert.assertEquals("10", "12", "Critical test cases failed.....");
	}

}
