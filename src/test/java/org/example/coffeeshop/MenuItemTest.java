package org.example.coffeeshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    // MenuItem is abstract — tested through a minimal anonymous subclass
    // so these tests stay isolated from Coffee/Tea/Pastry implementations.

    private MenuItem item(String name, double price, String description) {
        return new MenuItem(name, price) {
            @Override
            public String getDescription() { return description; }
        };
    }

    // --- getName ---

    @Test
    void getName_returnsConstructorValue() {
        assertEquals("Espresso", item("Espresso", 3.00, "desc").getName());
    }

    @Test
    void getName_emptyString_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> item("", 0.0, "desc"));
    }

    // --- getPrice ---

    @Test
    void getPrice_returnsConstructorValue() {
        assertEquals(3.50, item("Latte", 3.50, "desc").getPrice(), 0.001);
    }

    @Test
    void getPrice_zero_returnedAsIs() {
        assertEquals(0.0, item("Free", 0.0, "desc").getPrice(), 0.001);
    }

    @Test
    void getPrice_highValue_returnedAsIs() {
        assertEquals(99.99, item("X", 99.99, "desc").getPrice(), 0.001);
    }

    // --- getDescription (delegated to subclass) ---

    @Test
    void getDescription_returnsSubclassValue() {
        assertEquals("my description", item("X", 1.0, "my description").getDescription());
    }

    // --- toString ---

    @Test
    void toString_containsName() {
        assertTrue(item("Croissant", 2.50, "desc").toString().contains("Croissant"));
    }

    @Test
    void toString_containsDescription() {
        assertTrue(item("X", 1.0, "tasty pastry").toString().contains("tasty pastry"));
    }

    @Test
    void toString_nameAndDescriptionBothPresent() {
        String result = item("Muffin", 3.00, "gluten-free").toString();
        assertTrue(result.contains("Muffin"));
        assertTrue(result.contains("gluten-free"));
    }

    @Test
    void toString_nameLeftPadded_descriptionFollows() {
        // format is "%-22s %s": name is left-padded to 22 chars, then a space, then description
        String result = item("A", 1.0, "B").toString();
        int nameIndex = result.indexOf("A");
        int descIndex = result.indexOf("B");
        assertTrue(nameIndex < descIndex);
    }

    // --- MenuItem is not Preparable ---

    @Test
    void plainMenuItem_doesNotImplementPreparable() {
        MenuItem m = item("X", 1.0, "desc");
        assertFalse(m instanceof Preparable);
    }
}
