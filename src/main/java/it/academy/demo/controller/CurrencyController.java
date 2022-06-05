package it.academy.demo.controller;

import it.academy.demo.exception.BadRequestException;
import it.academy.demo.exception.NotFoundException;
import it.academy.demo.model.AuthenticationRequest;
import it.academy.demo.model.AuthenticationResponse;
import it.academy.demo.model.CreateCurrencyModel;
import it.academy.demo.model.CurrencyModel;
import it.academy.demo.security.jwt.JwtUtil;
import it.academy.demo.srvice.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        if (username == null || username.isEmpty())
            return getErrorAuthorizationMessage("Заполните поле логин");

        if (password == null || password.isEmpty())
           return getErrorAuthorizationMessage("Заполните поле пароль");

        if (!username.equals("admin") || !password.equals("admin"))
             getErrorAuthorizationMessage("Неверный логин или пароль");

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

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

    private ResponseEntity getErrorAuthorizationMessage(String message) {
        return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
    }
}
