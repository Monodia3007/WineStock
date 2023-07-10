package eu.lilithmonodia.winestock.app;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * The type Assortment.
 */
public class Assortment {
    private final List<Wine> wineList;
    private Year year;

    /**
     * Instantiates a new Assortment.
     */
    public Assortment() {
        this.wineList = new ArrayList<>();
    }

    /**
     * Instantiates a new Assortment.
     *
     * @param wineCollection the wine collection
     */
    public Assortment(Collection<Wine> wineCollection) {
        this.wineList = new ArrayList<>(wineCollection);
    }

    /**
     * Add boolean.
     *
     * @param wine the wine
     * @return the boolean
     */
    public boolean add(Wine wine) {
        if ((this.year != null && !wine.getYear().equals(this.year)) || wine.isInAssortment()) return false;
        this.year = wine.getYear();
        wine.setInAssortment(true);
        return this.wineList.add(wine);
    }

    /**
     * Remove boolean.
     *
     * @param wine the wine
     * @return the boolean
     */
    public boolean remove(Wine wine) {
        if (this.wineList.contains(wine)) {
            wine.setInAssortment(false);
            return this.wineList.remove(wine);
        }
        return false;
    }

    /**
     * Gets wine list.
     *
     * @return the wine list
     */
    public List<Wine> getWineList() {
        return wineList;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
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
