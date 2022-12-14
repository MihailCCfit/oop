package ru.nsu.fit.tsukanov.calculator.core.parser.numbers;

import ru.nsu.fit.tsukanov.calculator.core.Exceptions.BadTokenException;
import ru.nsu.fit.tsukanov.calculator.core.functions.Function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NumberParser<T> implements NumberParserInterface<T> {
    private final Set<NumberParserInterface<T>> numberParsers = new HashSet<>();

    public boolean putNumberParser(NumberParserInterface<T> numberParser) {
        return numberParsers.add(numberParser);
    }

    private List<T> listOfParsed(String token)  {
        List<T> results = new ArrayList<>();
        for (NumberParserInterface<T> numberParser : numberParsers) {
            if (numberParser.checkNumber(token)) {
                try {
                    results.add(numberParser.parseNumber(token));
                } catch (BadTokenException ignore){}
            }
        }
        return results;
    }

    /**
     * Parse token.
     *
     * @param token token
     * @return constant function
     */
    @Override
    public Function<T> parseToken(String token) throws BadTokenException {
        List<T> list;
        list = listOfParsed(token);
        if (list.size() != 1) {
            throw new BadTokenException("There is no valid possible number: " + list);
        }
        var num = list.get(0);
        return new Function<>() {
            @Override
            public int getArity() {
                return 0;
            }

            @Override
            public T apply(List<T> arguments) {
                return num;
            }

            @Override
            public String representation() {
                return num.toString();
            }
        };
    }

    /**
     * Parse to Number.
     *
     * @param token token
     * @return number
     */
    @Override
    public T parseNumber(String token) throws BadTokenException {
        return parseToken(token).apply(List.of());
    }

    /**
     * Check for number.
     *
     * @param token token
     * @return true if token is number.
     */
    @Override
    public boolean checkNumber(String token) {

        return listOfParsed(token).size() == 1;

    }
}
