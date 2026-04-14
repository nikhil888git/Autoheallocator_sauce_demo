package com.demo.utils;

import java.io.*;
import java.util.*;

public class CSVReaderUtil {

    public static List<String[]> readCSV(String path) throws Exception {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        }
        return data;
    }
}