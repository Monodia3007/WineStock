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
public class Assortment<W extends Wine> implements List<W> {
    private static final Logger LOGGER = LogManager.getLogger(Assortment.class);
    private static final String WINE_IS_NOT_IN_THE_ASSORTMENT = "Wine is not in the assortment";
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

    private void resetTotalPrice() {
        this.totalPrice = 0;
        for (W wine : wineList) {
            this.totalPrice += wine.getPrice();
        }
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
            wineList.add(wine);
            this.resetTotalPrice();
            this.resetWineNames();
            return true;
        } catch (WineAlreadyInAssortmentException e) {
            LOGGER.error(new StringBuilder().append(e.getMessage()).append("{}"), e);
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
                throw new WineNotInAssortmentException(WINE_IS_NOT_IN_THE_ASSORTMENT);
            else {
                return wineListRemoveActions(wine);
            }
        } catch (WineNotInAssortmentException e) {
            LOGGER.error(new StringBuilder().append(e.getMessage()).append("{}"), e);
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

            boolean isRemoved = wineList.removeIf(w -> w.equals(o));

            if (!isRemoved) {
                throw new WineNotInAssortmentException(WINE_IS_NOT_IN_THE_ASSORTMENT);
            }

            // The unchecked warning here cannot be removed because of type erasure in Java generics.
            // It is safe as we have already verified that o instanceof Wine.
            @SuppressWarnings("unchecked")
            W wine = (W) o;

            return wineListRemoveActions(wine);
        } catch (WineNotInAssortmentException e) {
            LOGGER.error(new StringBuilder().append(e.getMessage()).append("{}"), e);
            return false;
        }
    }

    /**
     * Execute wine removing actions
     *
     * @param wine The Wine object to be updated before removed.
     *
     * @return true if the Wine object is removed, false otherwise.
     */
    private boolean wineListRemoveActions(@NotNull W wine) {
        wine.setInAssortment(false);
        boolean wasRemoved = wineList.remove(wine);
        this.resetTotalPrice();
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
        for (W wine : c) {
            add(wine);
        }
        return true;
    }

    /**
     * Inserts all the elements in the specified collection into this list at the specified position
     * (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right
     * (increases their indices).
     * The new elements will appear in this list in the order
     * that they are returned by the specified collection's iterator.
     * The behavior of this operation is undefined
     * if the specified collection is modified while the operation is in progress.
     * (Note that this will occur if the specified collection is this list, and it's nonempty.)
     * This method also updates the total price and wine names of the assortment.
     *
     * @param index index at which to insert the first element from the specified collection
     * @param c     collection containing elements to be added to this list
     *
     * @return true if this list changed as a result of the call
     */
    @Override
    public boolean addAll(int index, @NotNull Collection<? extends W> c) {
        boolean result = true;
        for (W wine : c) {
            if (wineList.contains(wine)) {
                LOGGER.error("Wine is already in the assortment or the year of the wine is not matching with the assortment's year");
                result = false;
                continue;
            }
            this.add(index++, wine);
        }
        return result;
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
        boolean result = true;
        for (Object obj: c) {
            result &= remove(obj);
        }
        return result;
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

        while (iterator.hasNext()) {
            Wine wine = iterator.next();
            if (!c.contains(wine)) {
                wine.setInAssortment(false);
                iterator.remove();
                isChanged = true;
            }
        }

        // assign the updated wine names and total price
        this.resetTotalPrice();
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
        this.resetTotalPrice();
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

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     *
     * @return the element at the specified position in this list
     */
    @Override
    public W get(int index) {
        return this.wineList.get(index);
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * The element previously at the specified position is returned.
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     *
     * @return the element previously at the specified position
     */
    @Override
    public W set(int index, W element) {
        return this.wineList.set(index, element);
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent elements to the right
     * (adds one to their indices).
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, W element) {
        try {
            if (this.wineList.contains(element)) {
                throw new WineAlreadyInAssortmentException("Wine is already in the assortment");
            }
            this.wineList.add(index, element);
        } catch (WineAlreadyInAssortmentException e) {
            LOGGER.error(new StringBuilder().append(e.getMessage()).append("{}"), e);
        }

    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     * Returns the element removed from the list.
     *
     * @param index the index of the element to be removed
     *
     * @return the element previously at the specified position
     */
    @Override
    public W remove(int index) {
        return this.wineList.remove(index);
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list,
     * or -1 if this list does not contain the element.
     * More formally,
     * returns the lowest index i such that
     * (o==null ? get(i)==null: o.equals(get(i))), or -1 if there is no such index.
     *
     * @param o element to search for
     *
     * @return the index of the first occurrence of the specified element in this list,
     * or -1 if this list does not contain the element
     */
    @Override
    public int indexOf(Object o) {
        return this.wineList.indexOf(o);
    }

    /**
     * Returns the index of the last occurrence of the specified element in this list,
     * or -1 if this list does not contain the element.
     * More formally,
     * returns the highest index i such that
     * (o==null ? get(i)==null: o.equals(get(i))), or -1 if there is no such index.
     *
     * @param o element to search for
     *
     * @return the index of the last occurrence of the specified element in this list, or -1 if this list does not contain the element
     */
    @Override
    public int lastIndexOf(Object o) {
        return this.wineList.lastIndexOf(o);
    }

    /**
     * Returns a list iterator over the elements in this list (in a proper sequence).
     * The returned list iterator is backed by this list,
     * so non-structural changes in the returned list iterator are reflected in this list, and vice-versa.
     * The list iterator has no current element;
     * its cursor position always lies between the element that would be returned by a call to previous()
     * and the element that would be returned by a call to next().
     * An initial call to next would return the first element in the list;
     * an initial call to previous would return the last element in the list.
     *
     * @return a list iterator over the elements in this list (in a proper sequence)
     */
    @NotNull
    @Override
    public ListIterator<W> listIterator() {
        return this.wineList.listIterator();
    }

    /**
     * Returns a list iterator over the elements in this list (in a proper sequence),
     * starting at the specified position in the list.
     * The specified index indicates the first element that would be returned by an initial call to next.
     * An initial call to previous would return the element with the specified index minus one.
     * The returned list iterator is backed by this list,
     * so non-structural changes in the returned list iterator are reflected in this list, and vice-versa.
     *
     * @param index index of the first element to be returned from the list iterator (by a call to next)
     *
     * @return a list iterator over the elements in this list (in a proper sequence), starting at the specified position in the list
     */
    @NotNull
    @Override
    public ListIterator<W> listIterator(int index) {
        return this.wineList.listIterator(index);
    }

    /**
     * Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive.
     * The returned list is backed by this list,
     * so non-structural changes in the returned list are reflected in this list, and vice-versa.
     * This method eliminates the need for explicit range operations (of the sort that commonly exist for arrays).
     * Any operation that expects a list can be used as a range operation
     * by passing a subList view instead of a whole list.
     *
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex   high endpoint (exclusive) of the subList
     *
     * @return a view of the specified range within this list
     */
    @NotNull
    @Override
    public List<W> subList(int fromIndex, int toIndex) {
        return this.wineList.subList(fromIndex, toIndex);
    }
}