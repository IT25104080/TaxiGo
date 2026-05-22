package com.taxi.taxibookingplatform.controller;

import com.taxi.taxibookingplatform.model.User;
import com.taxi.taxibookingplatform.model.UserView;
import com.taxi.taxibookingplatform.service.SessionKeys;
import com.taxi.taxibookingplatform.service.UserFileHandler;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUser")
    public UserView currentUser(HttpSession session) throws IOException {
        Object userId = session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) {
            return null;
        }
        User user = UserFileHandler.getUserById(userId.toString());
        return user != null ? UserView.from(user) : null;
    }

    @ModelAttribute("adminLoggedIn")
    public boolean adminLoggedIn(HttpSession session) {
        Object admin = session.getAttribute("adminLoggedIn");
        return admin != null && (boolean) admin;
    }
}
