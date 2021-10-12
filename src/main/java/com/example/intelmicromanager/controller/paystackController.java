package com.example.intelmicromanager.controller;

import com.example.intelmicromanager.Service.paystack.InitializeTransaction;
import com.example.intelmicromanager.Service.paystack.InitializeTransactionRequest;
import com.example.intelmicromanager.Service.paystack.InitializeTransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paystack")
public class paystackController {

    private final InitializeTransaction initializeTransaction;

    public paystackController(InitializeTransaction initializeTransaction) {
        this.initializeTransaction = initializeTransaction;
    }

    @PostMapping
    public ResponseEntity<InitializeTransactionResponse> initializePayment(@RequestBody InitializeTransactionRequest request) throws Exception {
        InitializeTransactionResponse response = initializeTransaction.initTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
