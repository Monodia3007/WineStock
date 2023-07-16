package eu.lilithmonodia.winestock.app;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Assortment class represents a collection of wines.
 * <p>
 * It provides methods to add and remove wines, get the total price of the assortment, and retrieve the list of wines.
 */
public class Assortment {
    private final List<Wine> wineList;
    private Year year;
    private double totalPrice;
    private String wineNames;

    /**
     * Constructor for creating a new Assortment object.
     * <p>
     * The Assortment class represents a collection of wines.
     * <p>
     * When a new Assortment object is created, it initializes an empty wineList ArrayList
     * and sets the year to null.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment();
     * }</pre>
     */
    public Assortment() {
        wineList = new ArrayList<>();
        year = null;
        totalPrice = 0;
        wineNames = "";
    }

    /**
     * Adds a Wine object to the Assortment.
     * <p>
     * The add method adds a Wine object to the Assortment. The Wine object will be added to
     * the wineList ArrayList if the following conditions are met:
     * 1. If the year of the Wine object is not null and is different from the year of the Assortment, or
     * 2. If the Wine object is already in the Assortment.
     * <p>
     * If the Wine object is successfully added to the Assortment, the year of the Assortment will be
     * updated to the year of the added Wine object, and the Wine object's isInAssortment flag will be set
     * to true.
     * <p>
     * The totalPrice of the Assortment will be updated by adding the price of the added Wine object.
     * <p>
     * The wineNames of the Assortment will be updated by appending the name of the added Wine object,
     * separated by a comma and a space, if the wineNames is not empty. If the wineNames is empty,
     * the name of the added Wine object will be appended without any separators.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment();
     * Wine wine = new Wine("Chardonnay", 2019, 29.99);
     * boolean added = assortment.add(wine);
     * }</pre>
     *
     * @param wine The Wine object to be added to the Assortment.
     * @return true if the Wine object is successfully added to the Assortment, false otherwise.
     */
    public boolean add(Wine wine) {
        if ((year != null && !wine.getYear().equals(year)) || wine.isInAssortment()) return false;
        this.year = wine.getYear();
        wine.setInAssortment(true);
        // Update totalPrice and wineNames
        this.totalPrice += wine.getPrice();
        this.wineNames += (this.wineNames.isEmpty() ? "" : ", ") + wine.getName();
        return wineList.add(wine);
    }

    /**
     * Removes a Wine object from the Assortment.
     * <p>
     * The remove method removes a Wine object from the Assortment. The Wine object will be removed
     * from the wineList ArrayList if it is contained in the Assortment.
     * <p>
     * If the Wine object is successfully removed from the Assortment, the Wine object's isInAssortment
     * flag will be set to false.
     * <p>
     * The totalPrice of the Assortment will be updated by subtracting the price of the removed Wine object.
     * <p>
     * The wineNames of the Assortment will be updated by removing the name of the removed Wine object.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment();
     * Wine wine = new Wine("Chardonnay", 2019, 29.99);
     * assortment.add(wine);
     * boolean removed = assortment.remove(wine);
     * }</pre>
     *
     * @param wine The Wine object to be removed from the Assortment.
     * @return true if the Wine object is successfully removed from the Assortment, false otherwise.
     */
    public boolean remove(Wine wine) {
        if (wineList.contains(wine)) {
            wine.setInAssortment(false);
            // Update totalPrice and wineNames
            this.totalPrice -= wine.getPrice();
            this.wineNames = this.wineNames.replace(wine.getName(), "").replace(", ,", ", ").trim();
            if (this.wineNames.endsWith(",")) {
                this.wineNames = this.wineNames.substring(0, this.wineNames.length() - 1);
            }
            return wineList.remove(wine);
        }
        return false;
    }

    /**
     * Returns the list of Wine objects in the Assortment collection.
     * <p>
     * This method retrieves the list of Wine objects currently stored in the
     * Assortment.<br/>
     * The list is returned as a java.util.List&lt;Wine&gt;.
     * <p>
     * The returned list contains all Wine objects that are currently part of
     * the Assortment, in the order they were added.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment(wineCollection);
     * List<Wine> wineList = assortment.getWineList();
     * for (Wine wine : wineList) {
     *     System.out.println(wine.getName());
     * }
     * }</pre>
     *
     * @return the list of Wine objects in the Assortment collection
     */
    public List<Wine> getWineList() {
        return wineList;
    }

    /**
     * Returns the total price of all Wine objects in the Assortment collection.
     * <p>
     * This method calculates the total price of all Wine objects currently stored in the
     * Assortment. The price is returned as a double value.
     * <p>
     * The total price is calculated by summing up the price of each Wine object in the Assortment.
     * If there are no Wine objects in the Assortment, the total price will be 0.0.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment(wineCollection);
     * double totalPrice = assortment.getPrice();
     * System.out.println("Total price: $" + totalPrice);
     * }</pre>
     *
     * @return the total price of all Wine objects in the Assortment collection
     */
    public double getTotalPrice() {
        return this.totalPrice;
    }

    /**
     * Returns a string containing the names of all wine objects in the assortment.
     * <p>
     * This method retrieves the names of all wine objects currently stored in the assortment
     * and returns them as a single string. If there are no wine objects in the assortment,
     * an empty string will be returned.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment(wineCollection);
     * String wineNames = assortment.getWineNames();
     * System.out.println("Wine names: " + wineNames);
     * }</pre>
     *
     * @return a string containing the names of all wine objects in the assortment
     */
    public String getWineNames() {
        return wineNames;
    }

    /**
     * Returns the year of this object.
     * <p>
     * This method retrieves the year of this object and returns it as a Year object.
     * <p>
     * Example usage:
     * <pre>{@code
     * Wine wine = new Wine("Chardonnay", 2015);
     * Year year = wine.getYear();
     * System.out.println("Year: " + year);
     * }</pre>
     *
     * @return the year of this object as a Year object
     */
    public Year getYear() {
        return year;
    }

    /**
     * Returns a string representation of the Assortment object.
     * <p>
     * This method returns a string that represents the current state of the Assortment object.
     * The string includes the name of the class, the list of Wine objects in the Assortment,
     * and their respective details.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment(wineCollection);
     * String wineNames = assortment.toString();
     * System.out.println(wineNames);
     * }</pre>
     *
     * @return a string representation of the Assortment object
     */
    @Override
    public String toString() {
        return "Assortment{" +
                "wineList=" + wineList +
                '}';
    }

    /**
     * Compares this Assortment object with the specified object for equality.
     * <p>
     * This method checks if the specified object is an instance of Assortment and has the same wineList
     * and year as this Assortment object. If both conditions are satisfied, the method returns true;
     * otherwise, it returns false.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment1 = new Assortment(wineList1, 2021);
     * Assortment assortment2 = new Assortment(wineList2, 2022);
     * boolean equalsAssortment = assortment1.equals(assortment2);
     * System.out.println(equalsAssortment);
     * }</pre>
     *
     * @param o the object to compare for equality
     * @return true if this Assortment object is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assortment that)) return false;

        if (!wineList.equals(that.wineList)) return false;
        return Objects.equals(year, that.year);
    }

    /**
     * Returns a hash code value for this Assortment object.
     * <p>
     * The hash code calculation is based on the hash codes of the wineList and year properties of this object.
     * If the year property is null, its hash code is considered as 0.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment(wineList, year);
     * int hashCode = assortment.hashCode();
     * System.out.println(hashCode);
     * }</pre>
     *
     * @return the hash code value for this Assortment object
     */
    @Override
    public int hashCode() {
        int result = wineList.hashCode();
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
    }
}