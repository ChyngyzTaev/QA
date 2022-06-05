package it.academy.demo.controller;

import it.academy.demo.exception.BadRequestException;
import it.academy.demo.exception.NotFoundException;
import it.academy.demo.model.CreateCurrencyModel;
import it.academy.demo.model.CurrencyModel;
import it.academy.demo.srvice.CurrencyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    @Autowired
    CurrencyService currencyService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody CreateCurrencyModel createCurrencyModel) {
        try {
            return new ResponseEntity(currencyService.createNewCurrency(createCurrencyModel), HttpStatus.OK);
        } catch (BadRequestException badRequestException) {
            return new ResponseEntity(badRequestException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody CurrencyModel currencyModel){
        try {
            return new ResponseEntity(currencyService.updateCurrency(currencyModel), HttpStatus.OK);
        } catch (BadRequestException bre){
            return new ResponseEntity(bre.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException nfe) {
            return new ResponseEntity(nfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        try {
            return new ResponseEntity(currencyService.getById(id), HttpStatus.OK);
        } catch (BadRequestException bre){
            return new ResponseEntity(bre.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException nfe) {
            return new ResponseEntity(nfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all")
    public List<CurrencyModel> getAll() {
        return currencyService.getAllCurrency();
    }
}
