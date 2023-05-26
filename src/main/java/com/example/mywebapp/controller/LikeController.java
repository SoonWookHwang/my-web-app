package com.example.mywebapp.controller;

import com.example.mywebapp.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/like")
  public String addLike(@RequestParam Long contentsId) {
    return likeService.likeContents(contentsId);
  }

}
