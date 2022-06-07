package com.up42.backend.coding.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FeatureNotFoundException extends RuntimeException{

  public FeatureNotFoundException(String errorMessage){
    super(errorMessage);
  }

}
