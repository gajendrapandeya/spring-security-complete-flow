package com.codermonks.spring.security.client.service;

import com.codermonks.spring.security.client.entity.User;
import com.codermonks.spring.security.client.entity.VerificationToken;
import com.codermonks.spring.security.client.model.UserModel;
import com.codermonks.spring.security.client.repository.UserRepository;
import com.codermonks.spring.security.client.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserModel model) {
        var user = new User();
        user.setEmail(model.getEmail());
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(model.getPassword()));
        userRepository.save(user);
        return user;

    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        var verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }
}
