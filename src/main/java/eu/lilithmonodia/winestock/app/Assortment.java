package eu.lilithmonodia.winestock.app;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Assortment {
    private final List<Wine> wineList;
    private Year year;

    public Assortment() {
        this.wineList = new ArrayList<>();
    }

    public Assortment(Collection<Wine> wineCollection) {
        this.wineList = new ArrayList<>(wineCollection);
    }

    public boolean add(Wine wine) {
        if (this.year != null && !wine.getYear().equals(this.year)) return false;
        this.year = wine.getYear();
        wine.setInAssortment(true);
        return this.wineList.add(wine);
    }

    public boolean remove(Wine wine) {
        if (this.wineList.contains(wine)) {
            wine.setInAssortment(false);
            return this.wineList.remove(wine);
        }
        return false;
    }

    public List<Wine> getWineList() {
        return wineList;
    }

    public double getPrice() {
        double price = 0;
        for (Wine wine : wineList) {
            price += wine.getPrice();
        }
        return price;
    }

    @Override
    public String toString() {
        return "Assortment{" +
                "wineList=" + wineList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assortment that)) return false;

        if (!getWineList().equals(that.getWineList())) return false;
        return Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        int result = getWineList().hashCode();
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
    }
}
