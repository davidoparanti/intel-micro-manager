package com.example.intelmicromanager.controller;

import com.example.intelmicromanager.exception.ExceptionHandling;
import com.example.intelmicromanager.exception.domain.EmailExitException;
import com.example.intelmicromanager.exception.domain.UserNotFoundExecution;
import com.example.intelmicromanager.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController extends ExceptionHandling {

    @GetMapping("/home")
    public User showUser() throws UserNotFoundExecution{
//        return new User();
        throw new UserNotFoundExecution("User was not found");
    }

}
