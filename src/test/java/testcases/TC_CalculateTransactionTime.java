package testcases;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import utilities.ExcelUtility;

public class TC_CalculateTransactionTime {
	
	

	@DataProvider(name="TransData")
	public String [][] GIft_Data_For_Swipe_And_Manual() throws IOException
	{
		String path=".\\TransactionData.xlsx";//taking xl file from testData
		
		ExcelUtility xlutil=new ExcelUtility(path);//creating an object for XLUtility
		
		int totalrows=xlutil.getRowCount("Sheet1");	
		int totalcols=xlutil.getCellCount("Sheet1",1);
		
		String logindata[][]=new String[totalrows][totalcols];//created for two dimension array which can store the data user and password
		
		for(int i=1;i<=totalrows;i++)  //1   //read the data from xl storing in two deminsional array
		{		
			for(int j=0;j<totalcols;j++)  //0    i is rows j is col
			{
				logindata[i-1][j]= xlutil.getCellData("Sheet1",i, j);  //1,0
			}
		}
		return logindata;//returning two dimension array  
	}
	
	

	
	@Test(dataProvider = "TransData", dataProviderClass = TC_CalculateTransactionTime.class)
	public void calculateTime1(String time1 , String time2) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
		time1 = time1.trim();
		time2 = time2.trim();
		LocalDateTime dateTime1 = LocalDateTime.parse(time1, formatter);
		LocalDateTime dateTime2 = LocalDateTime.parse(time2, formatter);
		
		// Calculate the duration between the two times
		Duration duration = Duration.between(dateTime1 , dateTime2 );
		
		// Print the result
		System.out.print(time2 +" - " + time1+ "     -->     ");
		System.out.println("Time difference: " + duration.toMillis() + " milliseconds");
	}
	
	
	
}
