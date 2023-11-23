package eu.lilithmonodia.winestock.data;

import eu.lilithmonodia.winestock.exceptions.WineAlreadyInAssortmentException;
import eu.lilithmonodia.winestock.exceptions.WineNotInAssortmentException;
import org.jetbrains.annotations.NotNull;

import java.time.Year;
import java.util.*;
import java.util.function.Consumer;

/**
 * The Assortment class represents a collection of wines.
 * <p>
 * It provides methods to add and remove wines, get the total price of the assortment, and retrieve the list of wines.
 */
public class Assortment implements Collection<Wine>, Iterable<Wine> {
    private final List<Wine> wineList;
    private int id;
    private Year year;
    private double totalPrice;
    private String wineNames;

    /**
     * Default constructor for Assortment class.
     * Initializes an empty Assortment with id set to -1, the year set to null,
     * totalPrice set to 0, and wineNames set to an empty string.
     */
    public Assortment() {
        this(-1);
    }

    /**
     * Constructor for Assortment class with id parameter.
     * Initializes an empty Assortment with the given id, year set to null, totalPrice set to 0, and wineNames set to an empty string.
     *
     * @param id The id to set for the Assortment.
     */
    public Assortment(int id) {
        this.id = id;
        wineList = new ArrayList<>();
        year = null;
        totalPrice = 0;
        wineNames = "";
    }

    /**
     * Returns the ID of the object.
     * <p>
     * The getId method returns the ID of the object. The ID is an integer value that uniquely identifies the object.
     * <p>
     * Example usage:
     * <pre>{@code
     * Wine wine = new Wine("Chardonnay", 2019, 29.99);
     * int id = wine.getId();
     * }</pre>
     *
     * @return the ID of the object.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the list of Wine objects in the Assortment collection.
     * <p>
     * This method retrieves the list of Wine objects currently stored in the
     * Assortment.<br/>
     * The list is returned as a java.util.List&lt;Wine&gt; .
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
     * Sets the ID of the object.
     * <p>
     * The setId method sets the ID of the object to the specified integer value.
     *
     * @param id the new ID of the object.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Adds a Wine object to the Assortment.
     *
     * @param wine The Wine object to be added to the Assortment.
     *
     * @return {@code true} if the Wine object is successfully added to the Assortment, {@code false} otherwise.
     */
    public boolean add(Wine wine) {
        try {
            if ((year != null && !wine.getYear().equals(year)) || wine.isInAssortment()) {
                throw new WineAlreadyInAssortmentException("Wine is already in the assortment or the year of the wine is not matching with the assortment's year");
            }
            this.year = wine.getYear();
            wine.setInAssortment(true);
            // Update totalPrice and wineNames
            this.totalPrice += wine.getPrice();
            this.wineNames += (this.wineNames.isEmpty() ? "" : ", ") + wine.getName();
            return wineList.add(wine);
        } catch (WineAlreadyInAssortmentException e) {
            return false;
        }

    }

    /**
     * Removes a Wine object from the Assortment.
     * <p>
     * The remove method removes a Wine object from the Assortment. The Wine object will be removed from
     * the wineList ArrayList if it exists in the Assortment.
     * <p>
     * If the Wine object is successfully removed from the Assortment, the Wine object's isInAssortment flag will be set
     * to false.
     * <p>
     * The totalPrice of the Assortment will be updated by subtracting the price of the removed Wine object.
     * <p>
     * The wineNames of the Assortment will be updated by removing the name of the removed Wine object if it exists,
     * and removing any duplicate separators (", ,"). The resulting wineNames will be trimmed. If the resulting wineNames
     * ends with a comma, the comma will be removed.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment();
     * Wine wine = new Wine("Chardonnay", 2019, 29.99);
     * boolean removed = assortment.remove(wine);
     * }</pre>
     *
     * @param wine The Wine object to be removed from the Assortment.
     *
     * @return true if the Wine object is successfully removed from the Assortment, false otherwise.
     */
    public boolean remove(Wine wine) {
        try {
            if (!wineList.contains(wine))
                throw new WineNotInAssortmentException("Wine is not in the assortment");
            else {
                wine.setInAssortment(false);
                // Update totalPrice and wineNames
                this.totalPrice -= wine.getPrice();
                this.wineNames = this.wineNames.replace(wine.getName(), "").replace(", ,", ", ").trim();
                if (this.wineNames.endsWith(",")) {
                    this.wineNames = this.wineNames.substring(0, this.wineNames.length() - 1);
                }
                return wineList.remove(wine);
            }
        } catch (WineNotInAssortmentException e) {
            return false;
        }
    }

    /**
     * Removes a Wine object from the Assortment.
     * <p>
     * The remove method removes a Wine object from the Assortment. The Wine object will be removed from
     * the wineList ArrayList if it exists in the Assortment.
     * <p>
     * If the Wine object is successfully removed from the Assortment, the Wine object's isInAssortment flag will be set
     * to false.
     * <p>
     * The totalPrice of the Assortment will be updated by subtracting the price of the removed Wine object.
     * <p>
     * The wineNames of the Assortment will be updated by removing the name of the removed Wine object if it exists,
     * and removing any duplicate separators (", ,"). The resulting wineNames will be trimmed. If the resulting wineNames
     * ends with a comma, the comma will be removed.
     * <p>
     * Example usage:
     * <pre>{@code
     * Assortment assortment = new Assortment();
     * Wine wine = new Wine("Chardonnay", 2019, 29.99);
     * boolean removed = assortment.remove(wine);
     * }</pre>
     *
     * @param o The Wine object or object of its subclass to be removed from the Assortment.
     *
     * @return true if the Wine object is successfully removed from the Assortment, false otherwise.
     */
    @Override
    public boolean remove(Object o) {
        return o instanceof Wine wine && remove(wine);
    }

    /**
     * Returns the number of wines in the `wineList`.
     *
     * @return the number of wines in the `wineList`
     */
    @Override
    public int size() {
        return wineList.size();
    }

    /**
     * Returns true if the `wineList` is empty, false otherwise.
     *
     * @return true if the `wineList` is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return wineList.isEmpty();
    }

    /**
     * Returns true if the `wineList` contains the specified element, false otherwise.
     *
     * @param o the element whose presence in the `wineList` is to be tested
     * @return true if the `wineList` contains the specified element, false otherwise
     */
    @Override
    public boolean contains(Object o) {
        return wineList.contains(o);
    }

    /**
     * Returns an array containing all the elements in the `wineList` in a proper sequence (from first to last element).
     * The returned array will be "safe" as any modification to the array will not affect the original `wineList`.
     *
     * @return an array containing all the elements in the `wineList` in a proper sequence
     */
    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return wineList.toArray();
    }

    /**
     * Returns an array containing all the elements in the `wineList` in a proper sequence (from first to last element).
     * The returned array will be "safe" as any modification to the array will not affect the original `wineList`.
     *
     * @return an array containing all the elements in the `wineList` in a proper sequence
     * @param a the array into which the elements of the `wineList` are to be stored, if it is big enough; otherwise, a new array of the same runtime type is allocated for this purpose.
     */
    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return wineList.toArray(a);
    }

    /**
     * Returns true if all the elements in the `wineList` are contained in the specified collection (`c`).
     *
     * @param c the collection to be checked for containment in the `wineList`
     * @return true if all the elements in the `wineList` are contained in the specified collection (`c`), false otherwise.
     */
    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(wineList).containsAll(c);
    }

    /**
     * Adds all the elements in the specified collection (`c`) to the `wineList`.
     *
     * @param c the collection containing elements to be added to the `wineList`
     * @return true if the `wineList` changed as a result of the operation, false otherwise
     */
    @Override
    public boolean addAll(@NotNull Collection<? extends Wine> c) {
        return wineList.addAll(c);
    }

    /**
     * Removes all elements from the `wineList` that are present in the specified collection (`c`).
     *
     * @param c the collection containing elements to be removed from the `wineList`
     * @return true if the `wineList` changed as a result of the operation, false otherwise
     */
    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return wineList.removeAll(c);
    }

    /**
     * Retains only the elements in the `wineList` that are contained in the specified collection (`c`).
     * In other words, removes from the `wineList` all elements that are not present in `c`.
     *
     * @param c the collection containing elements to be retained in the `wineList`
     * @return true if the `wineList` changed as a result of the operation, false otherwise
     */
    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return wineList.retainAll(c);
    }

    /**
     * Removes all elements from the `wineList`.
     * The `wineList` will be empty after this method is called.
     */
    @Override
    public void clear() {
        wineList.clear();
    }

    /**
     * Returns an iterator over the elements in the `wineList`.
     * The iterator will return the wines in the order they were added.
     *
     * @return an Iterator over the wines in the `wineList`
     */
    @NotNull
    @Override
    public Iterator<Wine> iterator() {
        return wineList.iterator();
    }

    /**
     * Perform the provided action for each wine in the `wineList`.
     * The action is performed in the order the wines were added.
     *
     * @param action The action to be performed for each wine
     */

    @Override
    public void forEach(Consumer<? super Wine> action) {
        wineList.forEach(action);
    }

    /**
     * Returns a Spliterator over the wines in the `wineList`.
     * This can be used for operations such as parallel processing.
     *
     * @return a Spliterator over the wines in the `wineList`
     */
    @Override
    public Spliterator<Wine> spliterator() {
        return wineList.spliterator();
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
     *
     * @return true if this Assortment object is equal to the specified object, false otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assortment that)) return false;
        return getId() == that.getId() &&
                Double.compare(getTotalPrice(), that.getTotalPrice()) == 0 &&
                Objects.equals(getWineList(), that.getWineList()) &&
                Objects.equals(getYear(), that.getYear()) &&
                Objects.equals(getWineNames(), that.getWineNames());
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
        return Objects.hash(
                getWineList(),
                getId(),
                getYear(),
                getTotalPrice(),
                getWineNames()
        );
    }
}