package org.fexisaf.flexisafadvencefour.service;

import jakarta.mail.MessagingException;
import org.fexisaf.flexisafadvencefour.entity.TokenEntity;
import org.fexisaf.flexisafadvencefour.error.ErrorException;
import org.fexisaf.flexisafadvencefour.model.LoginModel;
import org.fexisaf.flexisafadvencefour.model.SignUpModel;
import org.fexisaf.flexisafadvencefour.model.TokenModel;

public interface AuthService {
    TokenEntity signUp(SignUpModel sign);

    void sendEmail(String email, String subject, String token) throws MessagingException;

    void verify(String token) throws ErrorException;

    TokenModel login(LoginModel loginModel);
}
