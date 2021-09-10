package com.example.intelmicromanager.Service;

import com.example.intelmicromanager.exception.domain.EmailExitException;
import com.example.intelmicromanager.exception.domain.UsernameExitException;
import com.example.intelmicromanager.model.User;

import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email ) throws UsernameExitException, EmailExitException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
