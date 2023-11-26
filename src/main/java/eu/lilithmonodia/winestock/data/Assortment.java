package eu.lilithmonodia.winestock.data;

import eu.lilithmonodia.winestock.exceptions.WineAlreadyInAssortmentException;
import eu.lilithmonodia.winestock.exceptions.WineNotInAssortmentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.time.Year;
import java.util.*;
import java.util.function.Consumer;

/**
 * The Assortment class represents a collection of Wine objects.
 * <p>
 * It allows for various operations including addition, removal, and retrieval of wines.
 * The class maintains the properties such as ID, total price, and wine names in the Assortment.
 */
public class Assortment<W extends Wine> implements Collection<W> {
    private static final Logger LOGGER = LogManager.getLogger(Assortment.class);
    private final List<W> wineList;
    private int id;
    private Year year;
    private double totalPrice;
    private String wineNames;

    /**
     * Default constructor for the Assortment class.
     * <p>
     * Initializes an Assortment with a default ID of -1, an empty Wine list,
     * null year, total price of zero, and an empty wine names string.
     */
    public Assortment() {
        this(-1);
    }

    /**
     * Overloaded constructor for the Assortment class.
     * <p>
     * Initializes an Assortment with the given ID, an empty Wine list,
     * null year, total price of zero, and an empty wine names string.
     *
     * @param id The ID for the Assortment
     */
    public Assortment(int id) {
        this.id = id;
        wineList = new ArrayList<>();
        year = null;
        totalPrice = 0;
        wineNames = "";
    }

    /**
     * Returns the ID of the Assortment.
     *
     * @return the ID of the Assortment
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID for the Assortment.
     *
     * @param id the new ID of the Assortment
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the list of Wine objects in the Assortment.
     *
     * @return List of Wine objects in the Assortment
     */
    public List<W> getWineList() {
        return wineList;
    }

    /**
     * Gets the total price of all Wine objects in the Assortment.
     *
     * @return total price of all Wine objects in the Assortment
     */
    public double getTotalPrice() {
        return this.totalPrice;
    }

    /**
     * Retrieves the names of all wine objects in the assortment.
     *
     * @return a string containing the names of all wine objects in the assortment
     */
    public String getWineNames() {
        return wineNames;
    }

    /**
     * Retrieves the year of this Assortment.
     *
     * @return the year of this Assortment
     */
    public Year getYear() {
        return year;
    }

    /**
     * Resets the `wineNames` attribute according to the current `wineList`.
     */
    private void resetWineNames() {
        StringBuilder wineNamesBuilder = new StringBuilder();
        for (W wine : wineList) {
            if (!wineNamesBuilder.isEmpty()) {
                wineNamesBuilder.append(", ");
            }
            wineNamesBuilder.append(wine.getName());
        }
        this.wineNames = wineNamesBuilder.toString();
    }

    /**
     * Adds a Wine object to the Assortment.
     *
     * @param wine The Wine object to be added to the Assortment.
     *
     * @return {@code true} if the Wine object is successfully added to the Assortment, {@code false} otherwise.
     */
    public boolean add(W wine) {
        try {
            if ((year != null && !wine.getYear().equals(year)) || wine.isInAssortment()) {
                throw new WineAlreadyInAssortmentException("Wine is already in the assortment or the year of the wine is not matching with the assortment's year");
            }
            this.year = wine.getYear();
            wine.setInAssortment(true);
            // Update totalPrice and wineNames
            this.totalPrice += wine.getPrice();
            wineList.add(wine);
            this.resetWineNames();
            return true;
        } catch (WineAlreadyInAssortmentException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     * Removes a Wine object from the Assortment.
     *
     * @param wine The Wine object to be removed from the Assortment.
     *
     * @return true if the Wine object is successfully removed from the Assortment, false otherwise.
     */
    public boolean remove(W wine) {
        try {
            if (!wineList.contains(wine))
                throw new WineNotInAssortmentException("Wine is not in the assortment");
            else {
                return wineListRemoveActions(wine);
            }
        } catch (WineNotInAssortmentException e) {
            return false;
        }
    }

    /**
     * Removes an Object from the Assortment.
     *
     * @param o The Object to be removed from the Assortment.
     *
     * @return true if the Wine object is successfully removed from the Assortment, false otherwise.
     */
    @Override
    public boolean remove(Object o) {
        try {
            if (!(o instanceof Wine)) {
                return false;
            }
            W wine = (W) o;
            if (!wineList.contains(wine)) {
                throw new WineNotInAssortmentException("Wine is not in the assortment");
            }
            return wineListRemoveActions(wine);
        } catch (WineNotInAssortmentException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     * Execute wine removing actions
     *
     * @param wine The Wine object to be updated before removed.
     * @return true if the Wine object is removed, false otherwise.
     */
    private boolean wineListRemoveActions(@NotNull W wine) {
        wine.setInAssortment(false);
        this.totalPrice -= wine.getPrice();
        boolean wasRemoved = wineList.remove(wine);
        this.resetWineNames();
        return wasRemoved;
    }

    /**
     * Checks the size of the Wine list.
     *
     * @return the number of wines in the `wineList`
     */
    @Override
    public int size() {
        return wineList.size();
    }

    /**
     * Checks if wineList is empty.
     *
     * @return `true` if wineList is empty, `false` otherwise
     */
    @Override
    public boolean isEmpty() {
        return wineList.isEmpty();
    }

    /**
     * Checks if the specified element is in the wineList.
     *
     * @param o the element to be checked
     *
     * @return `true` if the specified element is in wineList, `false` otherwise
     */
    @Override
    public boolean contains(Object o) {
        return wineList.contains(o);
    }

    /**
     * Converts the Wine list into an array.
     *
     * @return an array containing all the elements in the `wineList` in a proper sequence
     */
    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return wineList.toArray();
    }

    /**
     * Converts the Wine list into an array.
     *
     * @param a Array into which the elements of the `wineList` are to be stored
     *
     * @return an array containing all the Wines in `wineList` in a proper sequence
     */
    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return wineList.toArray(a);
    }

    /**
     * Checks if the collection is contained in the Wine list.
     *
     * @param c Collection to be checked
     *
     * @return `true` if the collection is in the Wine list, `false` otherwise
     */
    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(wineList).containsAll(c);
    }

    /**
     * Adds all elements in specified collection to the Wine list.
     *
     * @param c Collection whose elements are to be added to the Wine list
     *
     * @return `true` if the Wine list is modified as a result, `false` otherwise
     */
    @Override
    public boolean addAll(@NotNull Collection<? extends W> c) {
        boolean isAdded = false;
        for (W wine : c) {
            if (add(wine)) {
                isAdded = true;
            }
        }
        return isAdded;
    }

    /**
     * Removes from the Wine list all its elements that are also present in the specified collection.
     *
     * @param c Collection containing elements to be removed from the Wine list
     *
     * @return `true` if the Wine list is modified as a result, `false` otherwise
     */
    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean isRemoved = false;
        for (Object o : c) {
            if (o instanceof Wine wine && remove(wine)) {
                isRemoved = true;
            }
        }
        return isRemoved;
    }

    /**
     * Retains only the elements in the `wineList` that are contained in the specified collection.
     *
     * @param c the collection containing elements to be retained in the `wineList`
     *
     * @return `true` if `wineList` is modified as a result, `false` otherwise
     */
    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        boolean isChanged = false;
        Iterator<W> iterator = wineList.iterator();

        // reset totalPrice
        this.totalPrice = 0.0;

        while (iterator.hasNext()) {
            Wine wine = iterator.next();
            if (!c.contains(wine)) {
                wine.setInAssortment(false);
                iterator.remove();
                isChanged = true;
            } else {
                // update wineNames and totalPrice
                this.totalPrice += wine.getPrice();
            }
        }

        // assign the updated wine names
        this.resetWineNames();

        return isChanged;
    }

    /**
     * Removes all elements from the Wine list.
     */
    @Override
    public void clear() {
        for (Wine wine : wineList) {
            wine.setInAssortment(false);
        }
        wineList.clear();
        this.totalPrice = 0;
        this.resetWineNames();
    }

    /**
     * Sorts the Wine list according to the ID of the Wines.
     */
    public void sort() {
        Collections.sort(wineList);
        this.resetWineNames();
    }

    /**
     * Creates an Iterator for the Wine list.
     *
     * @return an Iterator for the Wine list
     */
    @NotNull
    @Override
    public Iterator<W> iterator() {
        return wineList.iterator();
    }

    /**
     * Performs the provided action for each Wine in the list.
     *
     * @param action The action to be performed for each Wine
     */
    @Override
    public void forEach(Consumer<? super W> action) {
        wineList.forEach(action);
    }

    /**
     * Creates a Spliterator over the Wines in the list.
     *
     * @return a Spliterator over the Wine list
     */
    @Override
    public Spliterator<W> spliterator() {
        return wineList.spliterator();
    }

    /**
     * Provides a string version of the Assortment.
     *
     * @return a string representation of the Assortment
     */
    @Override
    public String toString() {
        return "Assortment{" +
                "wineList=" + wineList +
                '}';
    }

    /**
     * Compares this Assortment to the specified object.
     *
     * @param o Object to be compared
     *
     * @return `true` if this Assortment is equal to the object, `false` otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assortment<?> that)) return false;
        return getId() == that.getId() &&
                Double.compare(getTotalPrice(), that.getTotalPrice()) == 0 &&
                Objects.equals(getWineList(), that.getWineList()) &&
                Objects.equals(getYear(), that.getYear()) &&
                Objects.equals(getWineNames(), that.getWineNames());
    }

    /**
     * Returns the hash code for this Assortment.
     *
     * @return the hash code for this Assortment
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                getWineList(),
                getId(),
                getYear(),
                getTotalPrice(),
                getWineNames());
    }
}