package org.fexisaf.flexisafadvencefour.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.fexisaf.flexisafadvencefour.entity.Role;
import org.fexisaf.flexisafadvencefour.entity.TokenEntity;
import org.fexisaf.flexisafadvencefour.entity.UserEntity;
import org.fexisaf.flexisafadvencefour.error.ErrorException;
import org.fexisaf.flexisafadvencefour.model.LoginModel;
import org.fexisaf.flexisafadvencefour.model.SignUpModel;
import org.fexisaf.flexisafadvencefour.model.TokenModel;
import org.fexisaf.flexisafadvencefour.repository.TokenRepository;
import org.fexisaf.flexisafadvencefour.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JavaMailSender javaMailSender;
    private final AuthenticationManager authenticationManager;

     @Value("${Myapplication.email}")
    private String name;


    @Override
    public TokenEntity signUp(SignUpModel sign) {
        UserEntity user = UserEntity.builder()
                .email(sign.getEmail())
                .firstName(sign.getFirstName())
                .lastName(sign.getLastName())
                .password(passwordEncoder.encode(sign.getPassword()))
                .role(Role.USER)
                .build();
        UserEntity savedUser = userRepository.save(user);

        TokenEntity tokenEntity = getToken(savedUser);
        return tokenRepository.save(tokenEntity);
    }

    private TokenEntity getToken(UserEntity savedUser) {
        final String token = jwtService.generateToken(savedUser);
        TokenEntity tokenEntity = TokenEntity.builder()
                .token(token)
                .revoked(false)
                .user(savedUser)
                .build();
        return tokenEntity;
    }

    @Transactional
    @Override
    public void sendEmail(String email, String subject, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(name);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(token);
        javaMailSender.send(message);
    }

    @Override
    public void verify(String token) throws ErrorException {
        String email = jwtService.extractUserName(token);
        UserEntity user = userRepository.findByEmail(email).orElseThrow();
        if(!jwtService.isTokenValid(user,token)) throw new ErrorException("something went wrong...");
        user.setLocked(true);
        userRepository.save(user);

    }

    @Override
    public TokenModel login(LoginModel loginModel) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginModel.getEmail(),
                        loginModel.getPassword()
                        )
        );
        UserEntity user = userRepository.findByEmail(loginModel.getEmail()).orElseThrow();
        var token = getToken(user);
        var listOfToken = tokenRepository.findAllById(user.getId());
        if(!listOfToken.isEmpty()) {
            listOfToken.forEach(e -> e.setRevoked(true));
        }
        tokenRepository.save(token);

        return  TokenModel.builder().token(token.getToken()).build();
    }


}
