package com.demo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * Class Name : CsvReader
 * Description: ISO 29119 Compliant Test Data Utility for reading CSV files.
 *              Separates test data from test execution logic.
 * ============================================================================
 */
public class CsvReader {

    /**
     * Reads a CSV file from the resources folder and maps it to a List of Maps.
     * Each Map represents a row, with the keys being the CSV headers.
     * 
     * @param fileName The name of the CSV file in src/test/resources/testdata
     * @return A list of mapped rows
     */
    public static List<Map<String, String>> readCsvData(String fileName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (InputStream is = CsvReader.class.getClassLoader().getResourceAsStream("testdata/" + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            if (is == null) {
                throw new RuntimeException("Test data file not found: " + fileName);
            }

            String line;
            String[] headers = null;

            while ((line = reader.readLine()) != null) {
                // Split by comma, ignoring commas within quotes
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                if (headers == null) {
                    headers = values; // First row is headers
                } else {
                    Map<String, String> rowMap = new HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        String value = (i < values.length) ? values[i].replace("\"", "").trim() : "";
                        rowMap.put(headers[i].trim(), value);
                    }
                    dataList.add(rowMap);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV test data: " + e.getMessage(), e);
        }
        
        return dataList;
    }
}
