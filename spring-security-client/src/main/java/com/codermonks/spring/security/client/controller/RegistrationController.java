package com.codermonks.spring.security.client.controller;

import com.codermonks.spring.security.client.entity.User;
import com.codermonks.spring.security.client.entity.VerificationToken;
import com.codermonks.spring.security.client.event.RegistrationCompleteEvent;
import com.codermonks.spring.security.client.model.UserModel;
import com.codermonks.spring.security.client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel model, final HttpServletRequest request) {
        var user = userService.registerUser(model);
        publisher.publishEvent(new RegistrationCompleteEvent(user, getApplicationUrl(request)));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public  String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")) {
            return "User Verified Successfully";
        }
        return  "Bad User";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        var verificationToken  = userService.generateNewVerificationToken(oldToken);
        var user = verificationToken.getUser();
        resendVerificationTokenMail(user, getApplicationUrl(request), verificationToken);
        return "Verification Link Sent";
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        //send mail to user
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();

        //sendVerificationEmail
        log.info("Click the link to verify your account: {}", url);
    }

    private String getApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
