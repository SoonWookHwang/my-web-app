package com.example.mywebapp.dto.request.contents;

import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

  private String comments;

  public boolean containsSpecialCharacter(CommentRequestDto dto) {
    String targetComments = dto.getComments();
    String pattern = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s]*$";
    return !Pattern.matches(pattern, targetComments);
  }

}
