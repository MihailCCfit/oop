package ru.nsu.fit.tsukanov.calculator.core.Exceptions;

public class FunctionException extends CalculatorException {
    public FunctionException() {
        super("Some function exception");
    }

    public FunctionException(String message) {
        super(message);
    }

}
