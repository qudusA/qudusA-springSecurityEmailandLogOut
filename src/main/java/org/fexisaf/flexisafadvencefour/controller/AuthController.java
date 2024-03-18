package org.fexisaf.flexisafadvencefour.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.fexisaf.flexisafadvencefour.entity.TokenEntity;
import org.fexisaf.flexisafadvencefour.error.ErrorException;
import org.fexisaf.flexisafadvencefour.eventListener.ApplicationEventList;
import org.fexisaf.flexisafadvencefour.model.LoginModel;
import org.fexisaf.flexisafadvencefour.model.SignUpModel;
import org.fexisaf.flexisafadvencefour.model.TokenModel;
import org.fexisaf.flexisafadvencefour.service.AuthService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ApplicationEventPublisher publisher;


    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpModel sign, HttpServletRequest request){
        TokenEntity tokenEntity = authService.signUp(sign);

        publisher.publishEvent(new ApplicationEventList(tokenEntity, appUrl(request)));
//       return new RedirectView("/smsoremail", true);
        return ResponseEntity.ok("user creation successful...");
    }

    private String appUrl(HttpServletRequest request) {
        return "http://"+
                request.getServerName() +":"
                +request.getServerPort()
                +request.getContextPath();
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("token") String token) throws ErrorException {
        authService.verify(token);
       return ResponseEntity.ok("verification successful...");
    }

    @PostMapping("/login")
    public TokenModel login(@RequestBody LoginModel loginModel){
        return authService.login(loginModel);

    }

    @GetMapping("/home")
    public String home(){
        return "welcome home...";
    }
}
