package org.example.util;

public class InputHelperException extends RuntimeException {

    private final String prompt;
    private final int    maxRetries;

    public InputHelperException(String prompt, int maxRetries) {
        super("Max retries (" + maxRetries + ") exceeded for prompt: \"" + prompt + "\"");
        this.prompt     = prompt;
        this.maxRetries = maxRetries;
    }

    InputHelperException(String prompt, int retriesSoFar, Throwable cause) {
        super("Input exhausted after " + retriesSoFar + " attempt(s) for prompt: \"" + prompt + "\"", cause);
        this.prompt     = prompt;
        this.maxRetries = retriesSoFar;
    }

    public String getPrompt()     { return prompt; }
    public int    getMaxRetries() { return maxRetries; }
}
