package eu.lilithmonodia.winestock.data;

import eu.lilithmonodia.winestock.app.Wine;

import java.util.ArrayList;
import java.util.List;

/**
 * `WineDataCSV` extends `DataCSV` to represent a Wine data CSV Reader. This class is
 * used to load data from a wine-specific CSV file into `Wine` objects, and then storing
 * them in a List of `Wine` objects. The CSV file is assumed to have the following order
 * of columns: Name, Vintage Year, Value, Type of wine, Bottle Count and Country of Origin.
 * <p>
 * Each row of the CSV file corresponds to a `Wine` object in the List. This class is
 * marked as deprecated, which means it's not recommended to use it in new code.
 */
@Deprecated
public class WineDataCSV extends DataCSV {
    /**
     * Constructs a new `WineDataCSV` object.
     *
     * @param fileName the CSV file name to load wine data from.
     */
    public WineDataCSV(String fileName) {
        super(fileName);
    }

    /**
     * Converts each row in the CSV file into a `Wine` object and adds it to a List.
     * It performs parsing and conversion for each value in a row. Specifically, it
     * converts vintage year, value, and bottle count into integers.
     * Then returns the List of `Wine` objects.
     *
     * @return the List of `Wine` objects loaded from the CSV file.
     */
    public List<Wine> toWine() {
        List<Wine> wineList = new ArrayList<>();
        for (ArrayList<String> wine : this.getTab()) {
            wineList.add(new Wine(
                    wine.get(0),
                    Integer.parseInt(wine.get(1)),
                    Integer.parseInt(wine.get(2)),
                    wine.get(3),
                    Integer.parseInt(wine.get(4)),
                    wine.get(5)
            ));
        }
        return wineList;
    }
}
