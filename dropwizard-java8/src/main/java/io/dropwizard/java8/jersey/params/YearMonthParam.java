package io.dropwizard.java8.jersey.params;

import io.dropwizard.jersey.params.AbstractParam;

import java.time.YearMonth;

/**
 * A parameter encapsulating year and month values. All non-parsable values will return a {@code 400 Bad
 * Request} response.
 *
 * @see java.time.YearMonth
 */
public class YearMonthParam extends AbstractParam<YearMonth> {
    public YearMonthParam(final String input) {
        super(input);
    }

    @Override
    protected YearMonth parse(final String input) throws Exception {
        return YearMonth.parse(input);
    }
}
