package org.fexisaf.flexisafadvencefour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpModel {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
