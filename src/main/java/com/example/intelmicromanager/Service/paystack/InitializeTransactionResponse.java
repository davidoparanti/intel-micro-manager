package com.example.intelmicromanager.Service.paystack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitializeTransactionResponse {

    private boolean status;
    private String message;
    private Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Data {

        private String authorization_url;

        private String access_code;

        private String reference;

    }


}

