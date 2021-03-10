package cz.muni.fi.pa165.currency;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

import org.mockito.*;

import java.math.BigDecimal;
import java.util.Currency;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class) // for JUnit < 5
public class CurrencyConvertorImplTest {
    @Mock ExchangeRateTable exchangeRateTable;

    private static final Currency czk = java.util.Currency.getInstance("CZK");
    private static final Currency eur = java.util.Currency.getInstance("EUR");
    private static final Currency usd = java.util.Currency.getInstance("USD");

    @Test
    public void testConvert() throws ExternalServiceFailureException {
        // Don't forget to test border values and proper rounding.
        assertNotNull(exchangeRateTable);
        Mockito.when(exchangeRateTable.getExchangeRate(eur, czk)).thenReturn(BigDecimal.valueOf(25));
        Mockito.when(exchangeRateTable.getExchangeRate(eur, usd)).thenReturn(new BigDecimal("1.185"));
        CurrencyConvertor currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);

        // test 0
        assertEquals(new BigDecimal("0.00"), currencyConvertor.convert(eur, czk, BigDecimal.valueOf(0)));

        // test 1
        assertEquals(new BigDecimal("25.00"), currencyConvertor.convert(eur, czk, BigDecimal.valueOf(1)));

        // test non-integer value
        assertEquals(new BigDecimal("62.50"), currencyConvertor.convert(eur, czk, new BigDecimal("2.5")));

        // test rounding to two decimal places and half_even
        assertEquals(new BigDecimal("1.18"), currencyConvertor.convert(eur, usd, BigDecimal.valueOf(1)));
        assertEquals(new BigDecimal("3.56"), currencyConvertor.convert(eur, usd, BigDecimal.valueOf(3)));

        // test negative value
        assertEquals(new BigDecimal("-25.00"), currencyConvertor.convert(eur, czk, BigDecimal.valueOf(-1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullSourceCurrency() {
        assertNotNull(exchangeRateTable);
        CurrencyConvertor currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);

        currencyConvertor.convert(null, czk, new BigDecimal(25));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullTargetCurrency() {
        assertNotNull(exchangeRateTable);
        CurrencyConvertor currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);

        currencyConvertor.convert(eur, null, new BigDecimal(25));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullSourceAmount() {
        assertNotNull(exchangeRateTable);
        CurrencyConvertor currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);

        currencyConvertor.convert(eur, czk, null);
    }

    @Test(expected = UnknownExchangeRateException.class)
    public void testConvertWithUnknownCurrency() {
        assertNotNull(exchangeRateTable);
        CurrencyConvertor currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);

        currencyConvertor.convert(czk, czk, BigDecimal.valueOf(1));
    }

    @Test(expected = UnknownExchangeRateException.class)
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        assertNotNull(exchangeRateTable);
        // must specify to throw since getExchangeRate is not implemented
        Mockito.when(exchangeRateTable.getExchangeRate(czk, czk)).thenThrow(ExternalServiceFailureException.class);
        CurrencyConvertor currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);

        currencyConvertor.convert(czk, czk, BigDecimal.valueOf(1));
    }

}
