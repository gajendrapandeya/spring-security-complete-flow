package com.codermonks.spring.security.client.controller;

import com.codermonks.spring.security.client.entity.User;
import com.codermonks.spring.security.client.event.RegistrationCompleteEvent;
import com.codermonks.spring.security.client.model.UserModel;
import com.codermonks.spring.security.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    private String getApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
