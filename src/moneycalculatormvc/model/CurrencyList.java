package moneycalculatormvc.model;

import java.util.HashMap;
import java.util.Map;

public class CurrencyList {

    private final Map<String,Currency> currencies =new HashMap();

    public CurrencyList() {
        currencies.put("USD", new Currency("USD","Dolar americano","$"));
        currencies.put("EUR", new Currency("EUR","Euro","€"));        
        currencies.put("GBP", new Currency("GBP","Libra","£"));
    }

    public Currency get(String code) {
        return currencies.get(code.toUpperCase());
    }
}
