package eu.lilithmonodia.winestock.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class AssortmentTest {

    private Assortment<Wine> assortment;
    private Wine wine1;
    private Wine wine2;

    @BeforeEach
    void setUp() {
        assortment = new Assortment<>();
        wine1 = new Wine.Builder("Wine1", 2020, 75, "ROUGE", 50.0).build();
        wine2 = new Wine.Builder("Wine2", 2020, 75, "BLANC", 100.0).build();
    }

    @Test
    void testAddWine() {
        assertDoesNotThrow(() -> {
            assertTrue(assortment.add(wine1));
            assertEquals(1, assortment.size());
            assertTrue(wine1.isInAssortment());
        });
    }

    @Test
    void testRemoveWine() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assertTrue(assortment.remove(wine1));
            assertEquals(0, assortment.size());
            assertFalse(wine1.isInAssortment());
        });
    }

    @Test
    void testGetIdSetId() {
        int newId = 10;
        assertDoesNotThrow(() -> {
            assortment.setId(newId);
            assertEquals(newId, assortment.getId());
        });
    }

    @Test
    void testGetPrice() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assortment.add(wine2);
            assertEquals(150.0, assortment.getTotalPrice(), 0.001);
        });
    }

    @Test
    void testGetWineNames() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assortment.add(wine2);

            String wineNames = assortment.getWineNames();

            assertTrue(wineNames.contains(wine1.getName()));
            assertTrue(wineNames.contains(wine2.getName()));
        });
    }

    @Test
    void testGetYear() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);

            assertEquals(wine1.getYear(), assortment.getYear());
        });
    }

    @Test
    void testToString() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);

            assertNotNull(assortment.toString());
            assertTrue(assortment.toString().contains(wine1.getName()));
        });
    }

    @Test
    void testAddWineAlreadyInAssortment() {
        assertTrue(assortment.add(wine1));
        assertFalse(assortment.add(wine1));
    }

    @Test
    void testRemoveWineNotInAssortment() {
        assertFalse(assortment.remove(wine1));
    }

    @Test
    void testEqualsObject() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);

            Assortment<Wine> otherAssortment = new Assortment<>();
            assertNotEquals(assortment, otherAssortment);

            Wine wine1InOtherAssortment = new Wine.Builder("Wine1", 2020, 75,
                    "ROUGE", 50.0).build();
            otherAssortment.add(wine1InOtherAssortment);

            assertEquals(assortment, otherAssortment);

            Wine wine2InOtherAssortment = new Wine.Builder("Wine2", 2020, 75,
                    "BLANC", 100.0).build();
            otherAssortment.add(wine2InOtherAssortment);
            assertNotEquals(assortment, otherAssortment);
        });
    }

    @Test
    void testNotEqualToNull() {
        assertNotEquals(null, assortment);  // Shouldn't be equal to null
    }

    @Test
    void testNotEqualToDifferentObject() {
        Object obj = new Object();
        assertNotEquals(assortment, obj);  // Shouldn't be equal to another type of object
    }

    @Test
    void testHashCode() {
        Assortment<Wine> assortment2 = new Assortment<>();
        assertEquals(assortment.hashCode(), assortment2.hashCode());
    }

    @Test
    void testNotEqualHashCode() {
        assortment.setId(1);
        Assortment<Wine> assortment2 = new Assortment<>();
        assertNotEquals(assortment.hashCode(), assortment2.hashCode());
    }

    @Test
    void testIterator() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assortment.add(wine2);

            Iterator<Wine> iterator = assortment.iterator();
            assertTrue(iterator.hasNext());
            assertEquals(wine1, iterator.next());
            assertEquals(wine2, iterator.next());
            assertFalse(iterator.hasNext());
        });
    }

    @Test
    void testForEach() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assortment.add(wine2);

            List<Wine> wines = new ArrayList<>();

            Consumer<Wine> action = wines::add;

            assortment.forEach(action);

            assertEquals(wines, assortment.stream().toList()); // checks whether forEach worked successfully
        });
    }

    @Test
    void testSpliterator() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assortment.add(wine2);

            Spliterator<Wine> spliterator = assortment.spliterator();
            assertNotNull(spliterator);

            assertTrue(spliterator.hasCharacteristics(Spliterator.ORDERED));
            assertEquals(2, spliterator.estimateSize());

            List<Wine> list = new ArrayList<>();
            spliterator.forEachRemaining(list::add);

            assertTrue(list.contains(wine1));
            assertTrue(list.contains(wine2));
        });
    }

    @Test
    void testConstructorWithId() {
        int id = 12345;
        assortment = new Assortment<>(id);

        assertEquals(id, assortment.getId()); // test setId
        assertEquals(0, assortment.getTotalPrice(), 0.01); // test setting totalPrice to 0
        assertEquals("", assortment.getWineNames()); // test setting wineNames to an empty string
        assertEquals(0, assortment.size()); // test that wineList is initialised and empty
        assertNull(assortment.getYear());  // test that year is null
    }

    @Test
    void testRemoveWineAndWineNames() {
        assertDoesNotThrow(() -> {
            assortment.add(wine1);
            assortment.add(wine2);
            String wineNamesBeforeRemoving = assortment.getWineNames();
            assertTrue(wineNamesBeforeRemoving.endsWith(wine2.getName()));

            // remove wine2
            assortment.remove(wine2);
            String wineNamesAfterRemoving = assortment.getWineNames();

            assertFalse(wineNamesAfterRemoving.endsWith(",")); // test that last character is not a comma
            assertFalse(wineNamesAfterRemoving.contains(wine2.getName())); // test removed wine's name is not in wineNames
        });
    }

    @Test
    void testClearFunctionality() {
        assortment.add(wine1);
        assortment.add(wine2);

        // Check that our assortment is not empty
        assertFalse(assortment.isEmpty());

        assortment.clear();

        // Check that our assortment is empty
        assertTrue(assortment.isEmpty());

        // Check that totalPrice is reset
        assertEquals(0, assortment.getTotalPrice(), 0.001);

        // Check that wineNames is reset
        assertEquals("", assortment.getWineNames());
    }

    @Test
    void testAddAllFunctionality() {
        List<Wine> wines = Arrays.asList(wine1, wine2);

        assortment.addAll(wines);

        // Check that our assortment has all the added wines
        assertTrue(assortment.containsAll(wines));
    }

    @Test
    void testRemoveAllFunctionality() {
        List<Wine> wines = Arrays.asList(wine1, wine2);

        assortment.addAll(wines);

        // Check that our assortment has all the added wines
        assertTrue(assortment.containsAll(wines));

        assortment.removeAll(wines);

        // Check that our assortment no longer has the removed wines
        assertFalse(assortment.containsAll(wines));
    }

    @Test
    void testRemoveObject() {
        assortment.add(wine1);
        assortment.remove(wine1);

        assertFalse(assortment.contains(wine1));
    }

    //Testing size() method
    @Test
    void testSize() {
        assertEquals(0, assortment.size()); //Assortment should be initially empty
        assortment.add(wine1);
        assertEquals(1, assortment.size());
    }

    //Testing contains(Object o) method
    @Test
    void testContains() {
        assertFalse(assortment.contains(wine1)); // Doesn't contain wine1 yet
        assortment.add(wine1);
        assertTrue(assortment.contains(wine1));
    }

    //Testing toArray() method
    @Test
    void testToArray() {
        assortment.add(wine1);
        Object[] array = assortment.toArray();
        assertEquals(1, array.length);
        assertSame(array[0], wine1); // Comparing references here, not the actual objects (can use equals(..) if applicable)
    }

    //Testing generic toArray(T[] a) method
    @Test
    void testToArrayGeneric() {
        assortment.add(wine1);
        Wine[] array = new Wine[1];
        array = assortment.toArray(array);
        assertEquals(1, array.length);
        assertSame(array[0], wine1); // Comparing references here, not the actual objects (can use equals(..) if applicable)
    }

    //Testing retainAll(Collection<?> c)
    @Test
    void testRetainAll() {
        assortment.add(wine1);
        assortment.add(wine2);

        List<Wine> list2 = new ArrayList<>();
        list2.add(wine2);

        assortment.retainAll(list2);

        assertFalse(assortment.contains(wine1)); // wine1 should be removed after retainAll
        assertTrue(assortment.contains(wine2));  // wine2 should still exist in the assortment
    }

    @Test
    void testGet() {
        assortment.add(wine1);
        assortment.add(wine2);
        assertEquals(wine1, assortment.get(0));
        assertEquals(wine2, assortment.get(1));
    }

    @Test
    void testSet() {
        assortment.add(wine1);
        assortment.set(0, wine2);
        assertEquals(wine2, assortment.get(0));
    }

    @Test
    void testAddAtIndex() {
        assortment.add(0, wine1);
        assertEquals(wine1, assortment.get(0));
    }

    @Test
    void testRemove() {
        assortment.add(wine1);
        assortment.remove(0);
        assertEquals(0, assortment.size());
    }

    @Test
    void testIndexOf() {
        assortment.add(wine1);
        assortment.add(wine2);
        assertEquals(0, assortment.indexOf(wine1));
        assertEquals(1, assortment.indexOf(wine2));
    }

    @Test
    void testLastIndexOf() {
        assortment.add(wine1);
        assortment.add(wine2);
        assertEquals(0, assortment.lastIndexOf(wine1));
    }

    @Test
    void testListIterator() {
        assortment.add(wine1);
        assortment.add(wine2);
        ListIterator<Wine> iterator = assortment.listIterator();
        assertTrue(iterator.hasNext());
        assertEquals(wine1, iterator.next());
    }

    @Test
    void testSort() {
        assortment.add(wine2); // Assume: price of wine2 > price of wine1
        assortment.add(wine1);
        wine1.setId(0);
        wine2.setId(1);

        assortment.sort();

        assertEquals(wine1, assortment.get(0));
        assertEquals(wine2, assortment.get(1));
    }

    @Test
    void testListIteratorAtIndex() {
        assortment.add(wine1);
        assortment.add(wine2);
        ListIterator<Wine> iterator = assortment.listIterator(1);
        assertTrue(iterator.hasNext());
        assertEquals(wine2, iterator.next());
    }

    @Test
    void testSubList() {
        assortment.add(wine1);
        assortment.add(wine2);
        assertEquals(Collections.singletonList(wine1), assortment.subList(0, 1));
    }

    @Test
    void testAddAllAtIndex() {
        assortment.addAll(0, Collections.singletonList(wine1));

        assertEquals(wine1, assortment.get(0));
    }

    @Test
    void testConstructorWithIdAndYear() {
        int id = 12345;
        Year year = Year.of(2024);
        assortment = new Assortment<>(id, year);

        assertEquals(id, assortment.getId()); // test setId
        assertEquals(year, assortment.getYear()); // test setYear
        assertEquals(0, assortment.getTotalPrice(), 0.01); // test setting totalPrice to 0
        assertEquals("", assortment.getWineNames()); // test setting wineNames to an empty string
        assertEquals(0, assortment.size()); // test that wineList is initialised and empty
    }

    @Test
    void testConstructorWithYear() {
        Year year = Year.of(2024);
        assortment = new Assortment<>(year);

        assertEquals(-1, assortment.getId()); // test default setId
        assertEquals(year, assortment.getYear()); // test setYear
        assertEquals(0, assortment.getTotalPrice(), 0.01); // test setting totalPrice to 0
        assertEquals("", assortment.getWineNames()); // test setting wineNames to an empty string
        assertEquals(0, assortment.size()); // test that wineList is initialised and empty
    }
}
