package org.fexisaf.flexisafadvencefour.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginModel {
    private String email;
    private String password;

    public  LoginModel(String email, String password){
        this.email = email;
        this.password = password;
    }
}
