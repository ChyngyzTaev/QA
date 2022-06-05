package it.academy.demo.srvice;

import it.academy.demo.model.CreateCurrencyModel;
import it.academy.demo.model.CurrencyModel;

import java.util.List;

public interface CurrencyService {
    CurrencyModel createNewCurrency(CreateCurrencyModel createCurrencyModel);

    CurrencyModel updateCurrency(CurrencyModel currencyModel);

    CurrencyModel getById(Long id);

    List<CurrencyModel> getAllCurrency();
}
