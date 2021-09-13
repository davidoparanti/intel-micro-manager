package com.example.intelmicromanager.controller;

import com.example.intelmicromanager.Service.UserService;
import com.example.intelmicromanager.exception.domain.EmailExitException;
import com.example.intelmicromanager.exception.domain.UsernameExitException;
import com.example.intelmicromanager.model.User;
import com.example.intelmicromanager.exception.ExceptionHandling;
import com.example.intelmicromanager.model.UserPrincipal;
import com.example.intelmicromanager.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.example.intelmicromanager.constant.SecurityConstant.JWT_TOKEN_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/","/user"})
public class UserController extends ExceptionHandling {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeaders = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeaders, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UsernameExitException, EmailExitException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
