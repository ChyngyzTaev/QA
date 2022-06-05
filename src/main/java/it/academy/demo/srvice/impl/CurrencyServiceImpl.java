package it.academy.demo.srvice.impl;

import it.academy.demo.entity.Currency;
import it.academy.demo.exception.BadRequestException;
import it.academy.demo.exception.NotFoundException;
import it.academy.demo.model.CreateCurrencyModel;
import it.academy.demo.model.CurrencyModel;
import it.academy.demo.repository.CurrencyRepository;
import it.academy.demo.srvice.CurrencyService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyServiceImpl implements CurrencyService {
    @Autowired
    CurrencyRepository currencyRepository;

    @Override
    public CurrencyModel createNewCurrency(CreateCurrencyModel createCurrencyModel) {
        checkCreateCurrencyFieldsForNull(createCurrencyModel);
        checkRate(createCurrencyModel.getRate());
        Currency currency = createCurrencyModel.toCurrency();
        currencyRepository.save(currency);
        return currency.toModel();
    }

    @Override
    public CurrencyModel updateCurrency(CurrencyModel currencyModel) {
        Long id = currencyModel.getId();
        checkIdForNull(id);
        checkUpdateCurrencyFields(currencyModel);
        Currency currency = currencyRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Идентификатор валюты: " + id + "не найден"));
        setUpdateFields(currency, currencyModel);
        currencyRepository.save(currency);
        return currencyModel;
    }

    @Override
    public CurrencyModel getById(Long id) {
        checkIdForNull(id);
        Currency currency = currencyRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Идентификатор валюты: " + id + "не найден"));
        return currency.toModel();
    }

    @Override
    public List<CurrencyModel> getAllCurrency() {
        return currencyRepository.findAll()
                .stream()
                .map(Currency::toModel)
                .collect(Collectors.toList());
    }

    private void checkIdForNull(Long id) {
        if (id == null) {
            throw new BadRequestException("Идентификатор не может быть пустым! ");
        }
    }

    private void checkCreateCurrencyFieldsForNull(CreateCurrencyModel currencyModel) {
        String currencyName = currencyModel.getName();
        Double currencyRate = currencyModel.getRate();

        if (currencyName == null || currencyName.isEmpty())
            throw new BadRequestException("Валюта не может быть пустым! ");

        if (currencyRate == null)
            throw new BadRequestException("Курс валюты не может быть пустым! ");
    }

    private void checkUpdateCurrencyFields(CurrencyModel currencyModel) {
        String currencyName = currencyModel.getName();
        Double currencyRate = currencyModel.getRate();

        if (currencyName != null)
            if (currencyName.isEmpty())
                throw new BadRequestException("Валюта не может быть пустым! ");

        if (currencyRate != null)
            checkRate(currencyRate);
    }

    private void checkRate(Double rate) {
        if (rate < 0)
            throw new BadRequestException("Курс валюты не может быть отрицательным! ");
    }

    private void setUpdateFields(Currency currency, CurrencyModel currencyModel) {
        String currencyName = currencyModel.getName();
        Double currencyRate = currencyModel.getRate();

        if (currencyName != null)
            currency.setName(currencyName);

        if (currencyRate != null)
            currency.setRate(currencyRate);
    }
}
