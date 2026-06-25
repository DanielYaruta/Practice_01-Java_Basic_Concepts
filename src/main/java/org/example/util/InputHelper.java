package org.example.util;

import java.io.Closeable;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Console input utility with automatic retry on invalid input.
 *
 * <p>UNLIMITED (-1) means the loop runs until valid input is received.
 * A non-negative maxRetries means the user gets maxRetries+1 total attempts
 * (1 initial + maxRetries additional); after that, InputHelperException is thrown.
 */
public class InputHelper implements Closeable {

    public static final int UNLIMITED = -1;

    private final Scanner     scanner;
    private final PrintStream out;
    private final int         maxRetries;

    public InputHelper() {
        this(System.in, System.out, UNLIMITED);
    }

    public InputHelper(InputStream in, PrintStream out, int maxRetries) {
        this.scanner    = new Scanner(Objects.requireNonNull(in,  "in must not be null"));
        this.out        = Objects.requireNonNull(out, "out must not be null");
        if (maxRetries != UNLIMITED && maxRetries < 0) {
            throw new IllegalArgumentException("maxRetries must be >= 0 or UNLIMITED (-1), was: " + maxRetries);
        }
        this.maxRetries = maxRetries;
    }

    // -------------------------------------------------------------------------
    // int
    // -------------------------------------------------------------------------

    public int readInt(String prompt) {
        return readInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public int readInt(String prompt, int min, int max) {
        requireValidRange(min, max);
        for (int attempt = 0; ; attempt++) {
            out.print(prompt);
            String raw = nextLine(prompt, attempt);
            try {
                int value = Integer.parseInt(raw);
                if (value < min || value > max) {
                    printError("value must be between " + min + " and " + max + ", got: " + value);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                printError("'" + raw + "' is not a valid integer");
            }
            checkRetries(prompt, attempt);
        }
    }

    // -------------------------------------------------------------------------
    // double
    // -------------------------------------------------------------------------

    public double readDouble(String prompt) {
        return readDouble(prompt, -Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public double readDouble(String prompt, double min, double max) {
        requireValidRange(min, max);
        for (int attempt = 0; ; attempt++) {
            out.print(prompt);
            String raw = nextLine(prompt, attempt);
            try {
                double value = Double.parseDouble(raw);
                if (value < min || value > max) {
                    printError("value must be between " + min + " and " + max + ", got: " + value);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                printError("'" + raw + "' is not a valid number");
            }
            checkRetries(prompt, attempt);
        }
    }

    // -------------------------------------------------------------------------
    // String
    // -------------------------------------------------------------------------

    /** Reads any line including blank. Never retries. */
    public String readLine(String prompt) {
        out.print(prompt);
        return nextLine(prompt, 0);
    }

    /** Reads a non-blank trimmed string; retries on blank input. */
    public String readNonBlank(String prompt) {
        for (int attempt = 0; ; attempt++) {
            out.print(prompt);
            String line = nextLine(prompt, attempt).trim();
            if (!line.isBlank()) return line;
            printError("input must not be blank");
            checkRetries(prompt, attempt);
        }
    }

    // -------------------------------------------------------------------------
    // boolean
    // -------------------------------------------------------------------------

    /** Prompts "(y/n)" and accepts y/yes/n/no (case-insensitive). */
    public boolean readBoolean(String prompt) {
        for (int attempt = 0; ; attempt++) {
            out.print(prompt + " (y/n): ");
            String raw = nextLine(prompt, attempt).trim().toLowerCase();
            if (raw.equals("y") || raw.equals("yes")) return true;
            if (raw.equals("n") || raw.equals("no"))  return false;
            printError("please enter 'y' or 'n'");
            checkRetries(prompt, attempt);
        }
    }

    // -------------------------------------------------------------------------
    // enum
    // -------------------------------------------------------------------------

    /**
     * Displays numbered list of enum constants and asks the user to pick one.
     * Uses readInt internally, inheriting the same retry behaviour.
     */
    public <E extends Enum<E>> E readEnum(String prompt, Class<E> enumClass) {
        E[] values = enumClass.getEnumConstants();
        out.println(prompt);
        for (int i = 0; i < values.length; i++) {
            out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = readInt("Select (1-" + values.length + "): ", 1, values.length);
        return values[choice - 1];
    }

    // -------------------------------------------------------------------------
    // Closeable
    // -------------------------------------------------------------------------

    @Override
    public void close() {
        scanner.close();
    }

    // -------------------------------------------------------------------------
    // private helpers
    // -------------------------------------------------------------------------

    private String nextLine(String prompt, int attempt) {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            throw new InputHelperException(prompt, attempt, e);
        }
    }

    private void checkRetries(String prompt, int attempt) {
        if (maxRetries != UNLIMITED && attempt >= maxRetries) {
            throw new InputHelperException(prompt, maxRetries);
        }
    }

    private void printError(String message) {
        out.println("  Error: " + message + ".");
    }

    private static void requireValidRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min (" + min + ") must be <= max (" + max + ")");
        }
    }

    private static void requireValidRange(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min (" + min + ") must be <= max (" + max + ")");
        }
    }
}
