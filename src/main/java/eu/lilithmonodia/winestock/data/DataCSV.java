package eu.lilithmonodia.winestock.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A `DataCSV` object represents a CSV (Comma-Separated Values)
 * file reader, which is used to load data from a CSV file into a
 * 2D ArrayList. Every line of the CSV file corresponds to an ArrayList of Strings
 * in the 2D ArrayList, with each column in a line of the CSV file
 * corresponding to a String in an ArrayList. This class is marked
 * as deprecated, which means it's not recommended to use it in new code.
 */
@Deprecated
public class DataCSV {
    private final String DELIMITER = ";";
    private final String fileName;
    private final ArrayList<ArrayList<String>> tab;

    /**
     * Constructs a new `DataCSV` object.
     *
     * @param fileName the CSV file name to load data from.
     */
    public DataCSV(String fileName) {
        this.fileName = fileName;
        this.tab = new ArrayList<>();
        this.load();
    }

    /**
     * Loads data from the CSV file into the 2D ArrayList.<br/>
     * Each row of the CSV file is stored as an ArrayList of Strings,
     * and each value is separated by the delimiter `;`.
     * If there is an IO error while reading the file,
     * it will print the stack trace.
     */
    public void load() {
        String line;
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
                while ((line = br.readLine()) != null) {
                    ArrayList<String> arrayLigne = new ArrayList<>();
                    String[] tabLine = line.split(DELIMITER);
                    for (int i = 0; tabLine.length > i; i++) {
                        arrayLigne.add(i, tabLine[i]);
                    }
                    this.tab.add(arrayLigne);
                }
                this.tab.remove(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a string representation of this `DataCSV` object,
     * which includes the delimiter, file name, and the 2D ArrayList.
     *
     * @return a String representation of this `DataCSV` object.
     */
    @Override
    public String toString() {
        return "DataCSV{" +
                "DELIMITER='" + DELIMITER + '\'' +
                ", fileName='" + fileName + '\'' +
                ", tab=" + tab +
                '}';
    }

    /**
     * Returns the 2D ArrayList of Strings loaded from the CSV file.
     *
     * @return the 2D ArrayList of Strings.
     */
    public ArrayList<ArrayList<String>> getTab() {
        return tab;
    }
}