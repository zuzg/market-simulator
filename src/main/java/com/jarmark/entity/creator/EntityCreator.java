package com.jarmark.entity.creator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * helper class to read data from csv files
 */
public class EntityCreator {
    private HashMap<String, Integer> createdCounter;

    public EntityCreator() {
        this.createdCounter = new HashMap<>();
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    /**
     * parse csv file to array of strings
     * @param fileName name of csv file
     * @return array of string
     * @throws FileNotFoundException when there is no file
     */
    public List<String> readFromCsv(String fileName) throws FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        int count = createdCounter.getOrDefault(fileName, 1);
        createdCounter.put(fileName, count + 1);
        String path = "C:/Dokumenty/UNI/III/OOP/JAVAPROJECT/jarmark/src/main/java/com/jarmark/entity/creator/data/";
        try (Scanner scanner = new Scanner(new File(path + fileName));) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        }
        return records.get(count);
    }
}
