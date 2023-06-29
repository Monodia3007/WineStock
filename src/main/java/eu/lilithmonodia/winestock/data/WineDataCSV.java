package eu.lilithmonodia.winestock.data;

import eu.lilithmonodia.winestock.app.Wine;

import java.util.ArrayList;
import java.util.List;

public class WineDataCSV extends DataCSV {
    public WineDataCSV(String fileName) {
        super(fileName);
    }

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
