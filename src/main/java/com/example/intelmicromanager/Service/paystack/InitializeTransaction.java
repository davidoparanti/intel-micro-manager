package com.example.intelmicromanager.Service.paystack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class InitializeTransaction {

    public static final String PAYSTACK_BEARER_TOKEN = "Bearer ";
    public static final String PAYSTACK_TRANSACTION_INITIALIZE_URL = "https://api.paystack.co/transaction/initialize";

    public  InitializeTransactionResponse initTransaction(InitializeTransactionRequest request){

        InitializeTransactionResponse initializeTransactionResponse = null;

        try {
            // convert transaction to json then use it as a body to post json
            Gson gson = new Gson();

            StringEntity postingString = new StringEntity(gson.toJson(request));

            CloseableHttpClient client = HttpClientBuilder.create().build();

            HttpPost post = new HttpPost(PAYSTACK_TRANSACTION_INITIALIZE_URL);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", PAYSTACK_BEARER_TOKEN);

            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

            } else {
                throw new Exception(response.toString());
            }

            ObjectMapper mapper = new ObjectMapper();

            initializeTransactionResponse = mapper.readValue(result.toString(), InitializeTransactionResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return initializeTransactionResponse;
    }
}

