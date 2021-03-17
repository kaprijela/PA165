package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import cz.muni.fi.pa165.currency.CurrencyConvertorImpl;
import cz.muni.fi.pa165.currency.ExchangeRateTable;
import cz.muni.fi.pa165.currency.ExchangeRateTableImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.math.BigDecimal;
import java.util.Currency;

@Configuration
@EnableAspectJAutoProxy
public class MainJavaConfig {
    @Bean
    public ExchangeRateTable getExchangeRateTable() {
        return new ExchangeRateTableImpl();
    }
    @Bean
    public CurrencyConvertor getCurrencyConvertorImpl() {
        return new CurrencyConvertorImpl(getExchangeRateTable());
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainJavaConfig.class);
        CurrencyConvertor cc = applicationContext.getBean(CurrencyConvertorImpl.class);

        System.out.println(cc.convert(
                Currency.getInstance("EUR"), Currency.getInstance("CZK"), BigDecimal.valueOf(1)
        ));
    }
}
