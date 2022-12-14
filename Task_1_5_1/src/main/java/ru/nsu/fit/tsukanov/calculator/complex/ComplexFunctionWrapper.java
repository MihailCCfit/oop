package ru.nsu.fit.tsukanov.calculator.complex;

import ru.nsu.fit.tsukanov.calculator.core.Calculator;
import ru.nsu.fit.tsukanov.calculator.core.Exceptions.CalculatorException;
import ru.nsu.fit.tsukanov.calculator.core.Exceptions.NotEnoughArguments;
import ru.nsu.fit.tsukanov.calculator.core.functions.Function;
import ru.nsu.fit.tsukanov.calculator.template.stack.FunctionWrapperStack;

import java.util.LinkedList;

public class ComplexFunctionWrapper extends FunctionWrapperStack<ComplexNumber> {
    private final LinkedList<ComplexNumber> arguments = new LinkedList<>();

    /**
     * Adds number (argument) to the end of arguments list .
     *
     * @param complexNumber the number that will be added to the list of arguments.
     * @throws CalculatorException if there is problem with argument
     */
    public void addArg(ComplexNumber complexNumber) throws CalculatorException {
        if (complexNumber == null) {
            throw new NullPointerException("Complex number is null");
        }
        if (arguments.size() >= getArity()) {
            throw new CalculatorException("Too much arguments");
        }
        arguments.addLast(complexNumber);
    }

    public ComplexFunctionWrapper(Function<ComplexNumber> function, Calculator<ComplexNumber> calculator) {
        super(function, calculator);
    }

    /**
     * Verifies that a function can be applied.
     *
     * @return true if function can be applied
     */
    public boolean available() {
        return arguments.size() == getArity();
    }

    /**
     * Apply function, depends on (or not) arguments.
     *
     * @return number
     */
    @Override
    public ComplexNumber apply() throws CalculatorException {
        if (arguments.size() < getArity()) {
            throw new NotEnoughArguments("For function: {" + function.representation() + "} Needs:" + getArity()
                    + "| Current:" + arguments.size() + " args: " + arguments);
        }
        return function.apply(arguments);
    }


    @Override
    public String toString() {
        return representation();
    }
}
