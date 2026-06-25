package org.example.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class InputHelperTest {

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private static InputHelper helper(String input, int maxRetries) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        return new InputHelper(
                new ByteArrayInputStream(bytes),
                new PrintStream(new ByteArrayOutputStream()),
                maxRetries
        );
    }

    /** Default: 3 retries allowed. */
    private static InputHelper h(String input) {
        return helper(input, 3);
    }

    /** Single-attempt helper (0 retries). */
    private static InputHelper h1(String input) {
        return helper(input, 0);
    }

    // -------------------------------------------------------------------------
    // constructor validation
    // -------------------------------------------------------------------------

    @Nested
    class ConstructorTest {

        @Test
        void nullIn_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                    () -> new InputHelper(null, System.out, InputHelper.UNLIMITED));
        }

        @Test
        void nullOut_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                    () -> new InputHelper(System.in, null, InputHelper.UNLIMITED));
        }

        @Test
        void negativeMaxRetriesNotMinusOne_throwsIllegalArgument() {
            assertThrows(IllegalArgumentException.class,
                    () -> helper("", -2));
        }

        @Test
        void zeroMaxRetries_valid() {
            assertDoesNotThrow(() -> h1("42\n"));
        }

        @Test
        void unlimitedConstant_isMinusOne() {
            assertEquals(-1, InputHelper.UNLIMITED);
        }
    }

    // -------------------------------------------------------------------------
    // readInt
    // -------------------------------------------------------------------------

    @Nested
    class ReadIntTest {

        @Test
        void validInput_returnsValue() {
            try (InputHelper h = h("42\n")) {
                assertEquals(42, h.readInt("Enter: "));
            }
        }

        @Test
        void negativeValue_returnsValue() {
            try (InputHelper h = h("-7\n")) {
                assertEquals(-7, h.readInt("Enter: "));
            }
        }

        @Test
        void invalidThenValid_retriesAndReturns() {
            try (InputHelper h = h("abc\n10\n")) {
                assertEquals(10, h.readInt("Enter: "));
            }
        }

        @Test
        void outOfRangeThenValid_retriesAndReturns() {
            try (InputHelper h = h("0\n5\n")) {
                assertEquals(5, h.readInt("Enter: ", 1, 10));
            }
        }

        @Test
        void maxRetriesExceeded_throwsInputHelperException() {
            try (InputHelper h = h1("abc\n")) {
                assertThrows(InputHelperException.class, () -> h.readInt("Enter: "));
            }
        }

        @Test
        void multipleInvalidThenExceeded_throwsInputHelperException() {
            // 0 retries — first failure throws
            try (InputHelper h = h1("not-a-number\n")) {
                assertThrows(InputHelperException.class, () -> h.readInt("Enter: "));
            }
        }

        @Test
        void minEqualsMax_validBoundary() {
            try (InputHelper h = h("5\n")) {
                assertEquals(5, h.readInt("Enter: ", 5, 5));
            }
        }

        @Test
        void minGreaterThanMax_throwsIllegalArgument() {
            try (InputHelper h = h("1\n")) {
                assertThrows(IllegalArgumentException.class, () -> h.readInt("Enter: ", 10, 1));
            }
        }

        @Test
        void minGreaterThanMax_errorMessageUsesIntFormat() {
            try (InputHelper h = h("1\n")) {
                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                        () -> h.readInt("Enter: ", 10, 1));
                assertTrue(ex.getMessage().contains("10"), "message should show 10, not 10.0");
                assertTrue(ex.getMessage().contains("1"),  "message should show 1, not 1.0");
                assertFalse(ex.getMessage().contains("10.0"), "message must not show double format");
            }
        }

        @Test
        void exhaustedInput_throwsInputHelperException() {
            // scanner runs out before valid input
            try (InputHelper h = helper("", InputHelper.UNLIMITED)) {
                assertThrows(InputHelperException.class, () -> h.readInt("Enter: "));
            }
        }
    }

    // -------------------------------------------------------------------------
    // readDouble
    // -------------------------------------------------------------------------

    @Nested
    class ReadDoubleTest {

        @Test
        void validInput_returnsValue() {
            try (InputHelper h = h("3.14\n")) {
                assertEquals(3.14, h.readDouble("Enter: "), 1e-9);
            }
        }

        @Test
        void integerInput_parsedAsDouble() {
            try (InputHelper h = h("7\n")) {
                assertEquals(7.0, h.readDouble("Enter: "), 1e-9);
            }
        }

        @Test
        void invalidThenValid_retriesAndReturns() {
            try (InputHelper h = h("hello\n2.5\n")) {
                assertEquals(2.5, h.readDouble("Enter: "), 1e-9);
            }
        }

        @Test
        void belowMin_retriesAndReturns() {
            try (InputHelper h = h("-1.0\n0.5\n")) {
                assertEquals(0.5, h.readDouble("Enter: ", 0.0, 10.0), 1e-9);
            }
        }

        @Test
        void maxRetriesExceeded_throwsInputHelperException() {
            try (InputHelper h = h1("xyz\n")) {
                assertThrows(InputHelperException.class, () -> h.readDouble("Enter: "));
            }
        }

        @Test
        void minGreaterThanMax_throwsIllegalArgument() {
            try (InputHelper h = h("1.0\n")) {
                assertThrows(IllegalArgumentException.class,
                        () -> h.readDouble("Enter: ", 5.0, 1.0));
            }
        }
    }

    // -------------------------------------------------------------------------
    // readLine
    // -------------------------------------------------------------------------

    @Nested
    class ReadLineTest {

        @Test
        void anyInput_returnsRaw() {
            try (InputHelper h = h("  hello world  \n")) {
                assertEquals("  hello world  ", h.readLine("Enter: "));
            }
        }

        @Test
        void blankInput_returnedAsIs() {
            try (InputHelper h = h("   \n")) {
                assertEquals("   ", h.readLine("Enter: "));
            }
        }

        @Test
        void emptyInput_returnsEmptyString() {
            try (InputHelper h = h("\n")) {
                assertEquals("", h.readLine("Enter: "));
            }
        }
    }

    // -------------------------------------------------------------------------
    // readNonBlank
    // -------------------------------------------------------------------------

    @Nested
    class ReadNonBlankTest {

        @Test
        void validInput_returnsTrimmed() {
            try (InputHelper h = h("  Alice  \n")) {
                assertEquals("Alice", h.readNonBlank("Name: "));
            }
        }

        @Test
        void blankThenValid_retriesAndReturns() {
            try (InputHelper h = h("   \nAlice\n")) {
                assertEquals("Alice", h.readNonBlank("Name: "));
            }
        }

        @Test
        void emptyThenValid_retriesAndReturns() {
            try (InputHelper h = h("\nBob\n")) {
                assertEquals("Bob", h.readNonBlank("Name: "));
            }
        }

        @Test
        void maxRetriesExceeded_throwsInputHelperException() {
            try (InputHelper h = h1("\n")) {
                assertThrows(InputHelperException.class, () -> h.readNonBlank("Name: "));
            }
        }
    }

    // -------------------------------------------------------------------------
    // readBoolean
    // -------------------------------------------------------------------------

    @Nested
    class ReadBooleanTest {

        @Test
        void y_returnsTrue() {
            try (InputHelper h = h("y\n")) { assertTrue(h.readBoolean("Confirm")); }
        }

        @Test
        void yes_returnsTrue() {
            try (InputHelper h = h("yes\n")) { assertTrue(h.readBoolean("Confirm")); }
        }

        @Test
        void YES_caseInsensitive_returnsTrue() {
            try (InputHelper h = h("YES\n")) { assertTrue(h.readBoolean("Confirm")); }
        }

        @Test
        void n_returnsFalse() {
            try (InputHelper h = h("n\n")) { assertFalse(h.readBoolean("Confirm")); }
        }

        @Test
        void no_returnsFalse() {
            try (InputHelper h = h("no\n")) { assertFalse(h.readBoolean("Confirm")); }
        }

        @Test
        void NO_caseInsensitive_returnsFalse() {
            try (InputHelper h = h("NO\n")) { assertFalse(h.readBoolean("Confirm")); }
        }

        @Test
        void invalidThenValid_retriesAndReturns() {
            try (InputHelper h = h("maybe\ny\n")) { assertTrue(h.readBoolean("Confirm")); }
        }

        @Test
        void maxRetriesExceeded_throwsInputHelperException() {
            try (InputHelper h = h1("maybe\n")) {
                assertThrows(InputHelperException.class, () -> h.readBoolean("Confirm"));
            }
        }
    }

    // -------------------------------------------------------------------------
    // readEnum
    // -------------------------------------------------------------------------

    enum Color { RED, GREEN, BLUE }

    @Nested
    class ReadEnumTest {

        @Test
        void selectFirst_returnsFirstConstant() {
            try (InputHelper h = h("1\n")) {
                assertEquals(Color.RED, h.readEnum("Pick color:", Color.class));
            }
        }

        @Test
        void selectLast_returnsLastConstant() {
            try (InputHelper h = h("3\n")) {
                assertEquals(Color.BLUE, h.readEnum("Pick color:", Color.class));
            }
        }

        @Test
        void selectMiddle_returnsMiddleConstant() {
            try (InputHelper h = h("2\n")) {
                assertEquals(Color.GREEN, h.readEnum("Pick color:", Color.class));
            }
        }

        @Test
        void outOfRangeThenValid_retriesAndReturns() {
            try (InputHelper h = h("5\n1\n")) {
                assertEquals(Color.RED, h.readEnum("Pick color:", Color.class));
            }
        }

        @Test
        void invalidInputThenValid_retriesAndReturns() {
            try (InputHelper h = h("abc\n2\n")) {
                assertEquals(Color.GREEN, h.readEnum("Pick color:", Color.class));
            }
        }
    }

    // -------------------------------------------------------------------------
    // InputHelperException
    // -------------------------------------------------------------------------

    @Nested
    class InputHelperExceptionTest {

        @Test
        void exceptionCarriesPrompt() {
            try (InputHelper h = h1("bad\n")) {
                InputHelperException ex = assertThrows(InputHelperException.class,
                        () -> h.readInt("Enter age: "));
                assertEquals("Enter age: ", ex.getPrompt());
            }
        }

        @Test
        void exceptionCarriesMaxRetries() {
            try (InputHelper h = h1("bad\n")) {
                InputHelperException ex = assertThrows(InputHelperException.class,
                        () -> h.readInt("Enter age: "));
                assertEquals(0, ex.getMaxRetries());
            }
        }

        @Test
        void exceptionMessageMentionsPrompt() {
            try (InputHelper h = h1("bad\n")) {
                InputHelperException ex = assertThrows(InputHelperException.class,
                        () -> h.readInt("Enter age: "));
                assertTrue(ex.getMessage().contains("Enter age: "));
            }
        }

        @Test
        void exhaustedInput_exceptionHasCause() {
            try (InputHelper h = helper("", InputHelper.UNLIMITED)) {
                InputHelperException ex = assertThrows(InputHelperException.class,
                        () -> h.readInt("Enter: "));
                assertNotNull(ex.getCause());
            }
        }
    }
}
