package com.example.mywebapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MemberResponseEntity<T> {

  private HttpStatus status;
  private T result;
  private Error error;

  public static <T> MemberResponseEntity<T> success(T result) {
    return new MemberResponseEntity<>(HttpStatus.OK, result, null);
  }

  public static <T> MemberResponseEntity<T> fail(HttpStatus status, String message) {
    return new MemberResponseEntity<>(status, null,
        new Error(status.value(), message));
  }

  @Getter
  @AllArgsConstructor
  static class Error {

    private int statusCode;
    private String message;
  }
}
