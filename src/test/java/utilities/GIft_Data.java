package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class GIft_Data {
	@DataProvider(name = "Gift_PreAuth_ActivationData")
	public Object[][] giftdata() {

		return new Object[][] {
			// *************************** SET 1*******************************

				{ "Pre-Auth activation",          "50.00", "9840000065000009699", "B", "11", "04", "71373309057" },
				{ "Pre-Auth Reload activation",   "50.00", "9840000065000009699", "B", "16", "04", "71373309057" },
				{ "activation",					  "50.00", "9840000065000009699", "B", "11", " ",    "71373309057" },
				{ "Reload",						  "50.00", "9840000065000009699", "B", "16", " ",    "71373309057" },
				{ "Balance Inquiry",			  "0.00", "9840000065000009699",  "B", "12", " ",    "71373309057" },
				{ "Redeem",                       "03.00", "9840000065000009699", "B", "18", " ", "71373309057" },
				
				{ "Pre-Auth activation",          "50.00", "9840000065000009699", "K", "11", "04", "71373309057" },  //
				{ "Pre-Auth Reload activation",   "50.00", "9840000065000009699", "K", "16", "04", "71373309057" },
				{ "activation",					  "50.00", "9840000065000009699", "K", "11", " ",    "71373309057" },
				{ "Reload",						  "50.00", "9840000065000009699", "K", "16", " ",    "71373309057" },
				{ "Balance Inquiry",			  "0.00", "9840000065000009699",  "K", "12", " ",    "71373309057" },
				{ "Redeem",                       "03.00", "9840000065000009699", "K", "18", " ", "71373309057" },
				
				
				
		};

	} 
	
	
	@DataProvider(name="GIFT_DATA")
	public String [][] getData() throws IOException
	{
		String path=".\\test-Data\\Gift-Data.xlsx";//taking xl file from testData
		
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
	
	@DataProvider(name="GIFT_DATA_S_M")
	public String [][] GIft_Data_For_Swipe_And_Manual() throws IOException
	{
		String path=".\\test-Data\\Swipe&Manual.xlsx";//taking xl file from testData
		
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
	@DataProvider(name="Prepaid_Data")
	public String [][] prepaid_Data() throws IOException
	{
		String path=".\\test-Data\\prepaid_Data.xlsx";       //taking xl file from testData
		
		ExcelUtility xlutil=new ExcelUtility(path);			//creating an object for XLUtility
		
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

}
