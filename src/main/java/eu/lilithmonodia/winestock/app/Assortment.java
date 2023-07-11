package eu.lilithmonodia.winestock.app;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This class represents an assortment of wine. Each assortment contains a list
 * of wines (`Wine` objects) and a year. An assortment can be instantiated with
 * an empty list or a provided collection of wines. Wines may be added or
 * removed from the assortment.
 */
public class Assortment {
    private final List<Wine> wineList;
    private Year year;

    /**
     * Constructs a new `Assortment` with an empty list of `Wine` objects.
     */
    public Assortment() {
        this.wineList = new ArrayList<>();
    }

    /**
     * Constructs a new `Assortment` with a given collection of wines.
     *
     * @param wineCollection the collection of `Wine` objects with which to initialize the wine list.
     */
    public Assortment(Collection<Wine> wineCollection) {
        this.wineList = new ArrayList<>(wineCollection);
    }

    /**
     * Adds a wine to the assortment.<br/>
     * The function checks appropriateness of the year and `inAssortment` flag
     * of the wine before adding.
     *
     * @param wine the `Wine` object to add to the assortment
     * @return `true` if the wine was added successfully; `false` otherwise.
     */
    public boolean add(Wine wine) {
        if ((this.year != null && !wine.getYear().equals(this.year)) || wine.isInAssortment()) return false;
        this.year = wine.getYear();
        wine.setInAssortment(true);
        return this.wineList.add(wine);
    }

    /**
     * Removes a wine from the assortment.<br/>
     * If the wine is present in the assortment, it is removed and its
     * `inAssortment` flag is set to false.
     *
     * @param wine the `Wine` object to be removed from the assortment.
     * @return `true` if the wine was successfully removed; `false` otherwise.
     */
    public boolean remove(Wine wine) {
        if (this.wineList.contains(wine)) {
            wine.setInAssortment(false);
            return this.wineList.remove(wine);
        }
        return false;
    }

    /**
     * Gets the list of wines contained in the assortment.
     *
     * @return the list of `Wine` objects in the assortment.
     */
    public List<Wine> getWineList() {
        return wineList;
    }

    /**
     * Calculates the total price of all the wines in the assortment.
     *
     * @return the total price of the `Wine` objects in the assortment.
     */
    public double getPrice() {
        double price = 0;
        for (Wine wine : wineList) {
            price += wine.getPrice();
        }
        return price;
    }

    /**
     * Returns a string representation of the `Assortment` object.<br/>
     * The returned string includes the class name (`Assortment`), and a list of all
     * the wine objects within this assortment. Each wine's information is enclosed
     * within curly braces ({}) and separated by commas.
     *
     * @return a string representation of the `Assortment` object.
     */
    @Override
    public String toString() {
        return "Assortment{" +
                "wineList=" + wineList +
                '}';
    }

    /**
     * Checks whether this `Assortment` instance is equal to the provided `Object`.<br/>
     * The function checks for the equality of the wine lists and the year of production.
     *
     * @param o the object to be compared for equality with `this`.
     * @return `true` if the specified object is equal to this `Assortment` object; `false` otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assortment that)) return false;

        if (!getWineList().equals(that.getWineList())) return false;
        return Objects.equals(year, that.year);
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * `java.util.HashMap`.<br/>
     * This function takes the wine list and the year into consideration when calculating the hash code.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = getWineList().hashCode();
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
    }
}
