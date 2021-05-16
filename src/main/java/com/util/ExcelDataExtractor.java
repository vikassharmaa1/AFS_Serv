package com.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataExtractor {

	static DataFormatter dataFormatter = new DataFormatter();

	public static Object[][] getExcelData(XSSFSheet sh, String methodName, int sheetCount, XSSFWorkbook wb) {
		Map<String, Map<String, String>> multi = new HashMap<String, Map<String, String>>();
		Map<String, String> dataMap = null;
		for (int sheetNumber = 0; sheetNumber < sheetCount; sheetNumber++) {

			sh = wb.getSheetAt(sheetNumber);
			int rowCount = sh.getLastRowNum();
			int testCaserowCount = getTestCaseRowCount(methodName, rowCount, sh);
			if (rowCount != 0 && testCaserowCount != 0) {
				int columnCount = sh.getRow(0).getLastCellNum();
				int found = 0;
				int foundRowNum = getTestCaseRowFound(methodName, rowCount, sh);
				for (int i = foundRowNum; i < rowCount; i++) {
					if (dataFormatter.formatCellValue(sh.getRow(i + 1).getCell(0)).equalsIgnoreCase(methodName)) {
						found = found + 1;
						dataMap = new HashMap<String, String>();
						for (int j = 0; j < columnCount; j++) {

							dataMap.put(dataFormatter.formatCellValue(sh.getRow(0).getCell(j)),
									dataFormatter.formatCellValue(sh.getRow(i + 1).getCell(j),
											wb.getCreationHelper().createFormulaEvaluator()));

						}

						if (multi.get(Integer.toString(found)) == null) {
							multi.put(Integer.toString(found), dataMap);
						} else {
							dataMap.putAll(multi.get(Integer.toString(found)));
							multi.put(Integer.toString(found), dataMap);
						}

					}

					if (testCaserowCount == found) {
						break;
					}
				}
			}
		}
		Object[][] dataObject = new Object[multi.size()][1];
		for (int k = 0; k < multi.size(); k++) {
			dataObject[k][0] = multi.get(Integer.toString(k + 1));
		}

		return dataObject;
	}

	private static int getTestCaseRowCount(String testCaseName, int overallRowCount, XSSFSheet sh) {
		int counter = 0;
		for (int i = 0; i < overallRowCount; i++) {
			if (dataFormatter.formatCellValue(sh.getRow(i + 1).getCell(0)).equalsIgnoreCase(testCaseName)) {
				counter = counter + 1;
			}
		}
		return counter;

	}

	private static int getTestCaseRowFound(String testCaseName, int overallRowCount, XSSFSheet sh) {
		int rowNumFound = 0;
		for (int i = 0; i < overallRowCount; i++) {
			if (dataFormatter.formatCellValue(sh.getRow(i + 1).getCell(0)).equalsIgnoreCase(testCaseName)) {
				rowNumFound = i;
				break;
			}
		}
		return rowNumFound;

	}

}
