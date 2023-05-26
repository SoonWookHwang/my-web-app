package com.example.mywebapp.controller;

import com.example.mywebapp.dto.request.contents.ContentsRequestDto;
import com.example.mywebapp.dto.response.ContentsResponseEntity;
import com.example.mywebapp.service.ContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContentsController {

  private final ContentsService contentsService;

  //Create
  @PostMapping("/contents")
  public ContentsResponseEntity<?> createContents(@RequestBody ContentsRequestDto dto) {
    try {
      return ContentsResponseEntity.success(contentsService.createContents(dto));
    } catch (Exception e) {
      return ContentsResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  //Read
  @GetMapping("/contents/my")
  public ContentsResponseEntity<?> getAllMyContents() {
    return ContentsResponseEntity.success(contentsService.getAllMyContents());
  }

  @GetMapping("/contents/{contentsId}")
  public ContentsResponseEntity<?> readContent(@PathVariable Long contentsId) {
    try {
      return ContentsResponseEntity.success(contentsService.readContent(contentsId));
    } catch (Exception e) {
      return ContentsResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @GetMapping("/contents/find/like/most3")
  public ContentsResponseEntity<?> mostThreeLiked() {
    return ContentsResponseEntity.success(contentsService.mostThreeLiked());
  }

  @GetMapping("/contents/find/hate/most3")
  public ContentsResponseEntity<?> mostThreeHated() {
    return ContentsResponseEntity.success(contentsService.mostThreeHated());
  }

  //Put
  @PutMapping("/contents/status/isFree/{contentsId}")
  public ContentsResponseEntity<?> isFreeContents(@RequestParam String isFree,
      @PathVariable Long contentsId) {
    try {
      return ContentsResponseEntity.success(contentsService.isFreeContents(isFree, contentsId));
    } catch (Exception e) {
      return ContentsResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @PutMapping("/contents/status/coin/{contentsId}")
  public ContentsResponseEntity<?> setCoin(@RequestParam int setCoin,
      @PathVariable Long contentsId) {
    try {
      return ContentsResponseEntity.success(contentsService.setCoin(setCoin, contentsId));
    } catch (Exception e) {
      return ContentsResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
