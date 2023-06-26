package eu.lilithmonodia.winestock.app;

import eu.lilithmonodia.winestock.exceptions.WrongYearException;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Assortment {
    private final List<Wine> wineList;
    private Year year;

    public Assortment() {
        this.wineList = new ArrayList<>();
    }

    public boolean add (Wine wine) {
        try {
            if (this.year != null && !wine.getYear().equals(this.year)) throw new WrongYearException();
            this.year = wine.getYear();
            return this.wineList.add(wine);
        } catch (WrongYearException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public boolean remove (Wine wine) {
        return this.wineList.remove(wine);
    }

    public Wine getWine(int idx) {
        return this.wineList.get(idx);
    }

    public List<Wine> getWineList() {
        return wineList;
    }
}
