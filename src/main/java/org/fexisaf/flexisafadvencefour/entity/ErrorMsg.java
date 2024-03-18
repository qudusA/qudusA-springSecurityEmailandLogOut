package org.fexisaf.flexisafadvencefour.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorMsg {

    private String msg;
    private HttpStatus request;
}
