package com.example.intelmicromanager.Service.impl;

import com.example.intelmicromanager.model.User;
import com.example.intelmicromanager.model.UserPrincipal;
import com.example.intelmicromanager.Service.UserService;
import com.example.intelmicromanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
@Qualifier("UserDetailsService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private  Logger LOGGER = LoggerFactory.getLogger(getClass());


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            LOGGER.error("User not found by username " + username);
            throw new UsernameNotFoundException("User not found by username " + username);
        } else {
            user.setLoginDateDisplay(user.getLoginDate());
            user.setLoginDate(new Date());
            userRepository.save(user);

            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info("Returning found user from user by username " + username);

            return userPrincipal;
        }
    }
}
