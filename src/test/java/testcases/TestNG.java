package testcases;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.Test;

import base.BaseClass;

public class TestNG extends BaseClass {
	
	

	
@Test
	public void test() throws IOException, Exception {
		List<String> gcbResult = performGetCardBin();
		System.out.println(gcbResult);
	}

}
