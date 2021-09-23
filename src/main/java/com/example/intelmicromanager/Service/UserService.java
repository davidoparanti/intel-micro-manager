package com.example.intelmicromanager.Service;

import com.example.intelmicromanager.exception.domain.EmailExitException;
import com.example.intelmicromanager.exception.domain.UsernameExitException;
import com.example.intelmicromanager.model.User;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email )
            throws UsernameExitException, EmailExitException, MessagingException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User addNewUser(String firstName, String lastName,
                    String username, String email, String role,
                    boolean isNonLocked, boolean isActive, MultipartFile profileImage);

    User updateUser(String currentUsername, String newFirstName, String NewLastName,
                    String NewUsername, String newEmail, String role,
                    boolean isNonLocked, boolean isActive, MultipartFile profileImage);

    void deleteUser(long id);

    void resetPassword(String email);

    User updateProfileImage(String username, MultipartFile profileImage);
}
