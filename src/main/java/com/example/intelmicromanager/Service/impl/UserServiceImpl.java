package com.example.intelmicromanager.Service.impl;

import com.example.intelmicromanager.Service.EmailService;
import com.example.intelmicromanager.Service.LoginAttemptService;
import com.example.intelmicromanager.eumeration.Role;
import com.example.intelmicromanager.exception.domain.EmailExitException;
import com.example.intelmicromanager.exception.domain.UsernameExitException;
import com.example.intelmicromanager.model.User;
import com.example.intelmicromanager.model.UserPrincipal;
import com.example.intelmicromanager.Service.UserService;
import com.example.intelmicromanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.example.intelmicromanager.constant.UserImplConstant.*;
import static org.apache.commons.lang3.StringUtils.*;


@Service
@Transactional
@Qualifier("UserDetailsService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLoginDateDisplay(user.getLoginDate());
            user.setLoginDate(new Date());
            userRepository.save(user);

            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);

            return userPrincipal;
        }
    }

    private void validateLoginAttempt(User user)  {
        if (user.isNotLocked()) {
            if (loginAttemptService.hasExceededMaxAttempt(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }

        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UsernameExitException, EmailExitException, MessagingException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTempProfileImage());

        userRepository.save(user);
        LOGGER.info("New user Password " + password);
//        emailService.sendNewPasswordEmail(username, password, email);
        return user;
    }

    private String getTempProfileImage() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UsernameExitException, EmailExitException {
        User userByUsername = findUserByUsername(newUsername);
        User userByEmail = findUserByEmail(newEmail);

        if(isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByUsername != null && !currentUser.getId().equals(userByUsername.getId())) {
                throw new UsernameExitException(USERNAME_ALREADY_EXIT);
            }
            if (userByEmail != null && !currentUser.getId().equals(userByEmail.getId())) {
                throw new EmailExitException(EMAIL_ALREADY_EXIT);
            }
            return currentUser;

        } else {
            if (userByUsername != null) {
                throw new UsernameExitException(USERNAME_ALREADY_EXIT);
            }
            if (userByEmail != null) {
                throw new EmailExitException(EMAIL_ALREADY_EXIT);
            }
            return null;
        }
    }


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) {
        return null;
    }

    @Override
    public User updateUser(String currentUsername, String newFirstName, String NewLastName, String NewUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) {
        return null;
    }

    @Override
    public void deleteUser(long id) {

    }

    @Override
    public void resetPassword(String email) {

    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) {
        return null;
    }
}
