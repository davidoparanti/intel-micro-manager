package com.example.intelmicromanager;

import com.example.intelmicromanager.constant.FileConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

@SpringBootApplication
public class IntelMicroManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelMicroManagerApplication.class, args);
        new File(FileConstant.USER_FOLDER).mkdirs();
    }



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
