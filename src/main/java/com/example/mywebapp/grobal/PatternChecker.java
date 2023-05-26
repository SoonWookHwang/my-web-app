package com.example.mywebapp.grobal;

import java.util.regex.Pattern;

public final class PatternChecker {

  // 비밀번호 패턴 체크를 위한 정규표현식 => 최소 1개의 영어 소문자, 특수문자, 숫자를 포함 && 한글과 영어대문자 사용불가 다섯글자 이상
  private final static String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?!.*[A-Z]).{5,}.*$";
  // 유저네임 패턴 체크를 위한 정규표현식 => 8글자 이하의 영어 대소문자, 한글, 숫자로만 가능
  private final static String usernamePattern = "^[a-zA-Z0-9가-힣]{1,8}$";

  public static boolean checkPasswordPattern(String password) {

    // 비밀번호와 정규표현식을 비교하여 매칭 여부 반환
    boolean isMatch = Pattern.matches(PatternChecker.passwordPattern, password);

    // 중국어 포함 여부 체크
    boolean hasChineseCharacters = containsChineseCharacters(password);

    return isMatch && !hasChineseCharacters;
  }

  public static boolean checkUsernamePattern(String username) {
    // 유저네임과 정규표현식을 비교하여 매칭 여부 반환
    return Pattern.matches(PatternChecker.usernamePattern, username);
  }

  public static boolean containsChineseCharacters(String str) {
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN) {
        return true; // 중국어 문자 존재
      }
    }
    return false; // 중국어 문자 미존재
  }

}
