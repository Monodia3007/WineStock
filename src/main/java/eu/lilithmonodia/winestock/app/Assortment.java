package eu.lilithmonodia.winestock.app;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class Assortment {
    private final List<Wine> wineList;
    private Year year;

    public Assortment() {
        this.wineList = new ArrayList<>();
    }

    public boolean add(Wine wine) {
        if (this.year != null && !wine.getYear().equals(this.year)) return false;
        this.year = wine.getYear();
        return this.wineList.add(wine);
    }

    public boolean remove(Wine wine) {
        return this.wineList.remove(wine);
    }

    public Wine getWine(int idx) {
        return this.wineList.get(idx);
    }

    public List<Wine> getWineList() {
        return wineList;
    }

    public int getPrice() {
        int price = 0;
        for (Wine wine : wineList) {
            price += wine.getPrice();
        }
        return price;
    }
}
