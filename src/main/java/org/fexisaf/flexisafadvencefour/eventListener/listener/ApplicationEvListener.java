package org.fexisaf.flexisafadvencefour.eventListener.listener;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.fexisaf.flexisafadvencefour.eventListener.ApplicationEventList;
import org.fexisaf.flexisafadvencefour.service.AuthService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEvListener implements ApplicationListener<ApplicationEventList> {
   private final AuthService authService;

    @Override
    public void onApplicationEvent(ApplicationEventList event) {
        String email = event.getTokenEntity().getUser().getEmail();
        String token = event.getTokenEntity().getToken();
        String url = event.getUrl();
//        Random random = new Random();
//        int otp = random.nextInt() * 1000000;
        String msg = """
                this is from Olanrewaju Qudus application kindly disregards
                this mail if you did not carry out any activity on the site
                kindly click on the link to validate yourself as you will
                not be allowed to login if validation is not done.
                """;
        String uri = msg + url+"/verify?token="+token;
        System.out.println(uri);
        try {
            authService.sendEmail(email, "VERIFICATION TOKEN", uri);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
