package org.fexisaf.flexisafadvencefour.error;

import org.fexisaf.flexisafadvencefour.entity.ErrorMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorMsg> errorMsg(ErrorException e){
       ErrorMsg errorMsg = new ErrorMsg(e.getMessage(), HttpStatus.FORBIDDEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMsg);
    }

}
