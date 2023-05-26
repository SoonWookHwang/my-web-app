package com.example.mywebapp.grobal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{

  CustomErrorCode errorCode;

}
