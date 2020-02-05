package moneycalculatormvc;

import moneycalculatormvc.model.Currency;
import moneycalculatormvc.model.ExchangeRate;
import moneycalculatormvc.model.CurrencyList;
import moneycalculatormvc.model.Money;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Scanner;

public class MoneyCalculatorMVC {

    public static void main(String[] args) {
        MoneyCalculatorMVC moneyCalculatorV2 = new MoneyCalculatorMVC();
        moneyCalculatorV2.exec();
    }

    private Money money;
    private Currency currencyto;
    private ExchangeRate exchange;
    private CurrencyList currencyList;

    public MoneyCalculatorMVC() {
        currencyList=new CurrencyList();
    }
    
    
    private void exec() {
        input();
        process();
        output();
    }

    private void input() {
        System.out.println("Introduce una cantidad");
        Scanner scanner = new Scanner(System.in);
        Double amount = scanner.nextDouble();
        
        System.out.println("Introduce una divisa inicial");
        while(true){
            String code = scanner.nextLine().toUpperCase();
            Currency currency = currencyList.get(code);
            money=new Money(amount, currency);
            if(currency!=null) break;
            System.out.println("Divisa desconocida");
        }
        
        System.out.println("Introduce una divisa final");
        while(true){
            String code=scanner.nextLine().toUpperCase();
            currencyto = currencyList.get(code);
            if(currencyto!=null) break;
            System.out.println("Divisa desconocida");
        }
    }

    private void process() {
        exchange=getExchange(money.getCurrency(), currencyto);
    }

    private void output() {
        System.out.println(String.format("Son %.2f "+currencyto.getCode(),exchange.getRate()*money.getAmount()));
    }


    private ExchangeRate getExchange(Currency from, Currency to) {
        String divChain = "";
        while (true) {
            try {
                URL url = new URL("https://api.exchangeratesapi.io/latest");
                Scanner scanner1 = new Scanner(url.openStream());
                divChain = scanner1.nextLine();
                break;
            } catch (Exception ex) {
            }
        }
        double excOrigen=1;
        double excDestino=1;
        int i = divChain.indexOf(":");
        int j = divChain.indexOf(",", i);
        String exchange = divChain.substring(i + 2, j); //Par divisa-valor inicial
        String divisaPointer=exchange.substring(1,4);
        while(j!=-1){
            if(divisaPointer.equals(to.getCode())){
                String exchangeRate = exchange.substring(6);
                excDestino=Double.parseDouble(exchangeRate);
            }
            if(divisaPointer.equals(from.getCode())){
                String exchangeRate = exchange.substring(6);
                excOrigen=Double.parseDouble(exchangeRate);
            }
            i=j;
            j=divChain.indexOf(",", i+1);
            try{ 
                exchange=divChain.substring(i+1,j);
            }catch(StringIndexOutOfBoundsException eo){
                
            }
            divisaPointer=exchange.substring(1,4);
        }   
        double rate=excDestino/excOrigen; //Converts from currencyfrom to euros and from euros to currencyto
        return new ExchangeRate(from, to, LocalDate.of(2020, Month.JANUARY, 10), rate);
    }
}
