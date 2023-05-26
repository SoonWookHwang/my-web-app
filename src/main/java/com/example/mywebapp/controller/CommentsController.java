package com.example.mywebapp.controller;

import com.example.mywebapp.domain.comment.Comments;
import com.example.mywebapp.dto.request.contents.CommentRequestDto;
import com.example.mywebapp.dto.response.ContentsResponseEntity;
import com.example.mywebapp.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentsController {

  private final CommentsService commentsService;

  @PostMapping("/comments")
  public ContentsResponseEntity<Comments> createComments(@RequestParam Long contentsId, @RequestBody
  CommentRequestDto dto) {
    try {
      return ContentsResponseEntity.success(
          commentsService.createComments(contentsId, dto));
    } catch (Exception e) {
      return ContentsResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @PutMapping("/comments/update")
  public ContentsResponseEntity<Comments> updateComments(@RequestParam Long commentsId,
      @RequestBody CommentRequestDto dto) {
    try {
      return ContentsResponseEntity.success(commentsService.updateComments(commentsId, dto));
    } catch (Exception e) {
      return ContentsResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
