package ru.nsu.fit.tsukanov.calculator.core;

import ru.nsu.fit.tsukanov.calculator.core.Exceptions.CalculatorException;

public interface Calculator<T> {
    /**
     * Change line for calculating.
     *
     * @param input line for parsing
     * @return old input
     */
    String newLine(String input);

    /**
     * Calculates result.
     *
     * @return result
     * @throws CalculatorException if there is problem
     */
    T getResult() throws CalculatorException;

    /**
     * Parse and calculates line
     *
     * @param line line for parsing
     * @return result
     * @throws CalculatorException if there is problem
     */
    T getResult(String line) throws CalculatorException;

    /**
     * Get info about state of calculator.
     *
     * @return information
     */
    String getInformation();
}
