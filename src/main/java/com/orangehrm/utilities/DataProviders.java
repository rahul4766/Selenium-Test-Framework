package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	private static String FILE_PATH = System.getProperty("user.dir")+"/src/test/resources/testdata/TestData.xlsx";
    
	
	@DataProvider(name="Valid_Login_Data")
	public static Object[][] Valid_Login_Data(){
		return getSheetData("Valid_Login_Data");
	}
	@DataProvider(name="Invalid_Login_Data")
	public static Object[][] Invalid_Login_Data(){
		return getSheetData("Invalid_Login_Data");
	}
	
	
	@DataProvider(name="emplVerification")
	public static Object[][] emplVerification(){
		return getSheetData("emplVerification");
	}
	
	 private static synchronized  Object[][] getSheetData(String sheetname) {
		 List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetname);
		 Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];
		 
		 for(int i=0;i<sheetData.size();i++) {
			 data[i] = sheetData.get(i);
		 }
		 return data;
	 }
}
;