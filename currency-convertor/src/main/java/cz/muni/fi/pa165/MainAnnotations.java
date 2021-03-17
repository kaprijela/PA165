package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertorImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainAnnotations {
    public static void main(String[] args) {
        /*
        Some ways to instantiate AnnotationConfigApplicationContext (see documentation):
          - manually registering classes afterwards
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
            applicationContext.register(ExchangeRateTableImpl.class);
            applicationContext.register(CurrencyConvertorImpl.class);
            applicationContext.refresh();

          - specifying component classes directly
            AnnotationConfigApplicationContext applicationContext =
                    new AnnotationConfigApplicationContext(ExchangeRateTableImpl.class, CurrencyConvertorImpl.class);

          - specifying base package to search for components
            AnnotationConfigApplicationContext applicationContext =
                    new AnnotationConfigApplicationContext("cz.muni.fi.pa165.currency");
         */
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("cz.muni.fi.pa165.currency");
        CurrencyConvertorImpl currencyConvertor = applicationContext.getBean(CurrencyConvertorImpl.class);

        System.out.println(currencyConvertor.convert(
                Currency.getInstance("EUR"), Currency.getInstance("CZK"), BigDecimal.valueOf(1)
        ));
    }
}
