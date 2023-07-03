package eu.lilithmonodia.winestock.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The type Data csv.
 */
public class DataCSV {
    private final String DELIMITER = ";";
    private final String fileName;
    private final ArrayList<ArrayList<String>> tab;

    /**
     * Instantiates a new Data csv.
     *
     * @param fileName the file name
     */
    public DataCSV(String fileName) {
        this.fileName = fileName;
        this.tab = new ArrayList<>();
        this.load();
    }

    /**
     * Load.
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

    @Override
    public String toString() {
        return "DataCSV{" +
                "DELIMITER='" + DELIMITER + '\'' +
                ", fileName='" + fileName + '\'' +
                ", tab=" + tab +
                '}';
    }

    /**
     * Gets tab.
     *
     * @return the tab
     */
    public ArrayList<ArrayList<String>> getTab() {
        return tab;
    }
}
