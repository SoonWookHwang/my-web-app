package com.example.mywebapp.controller;


import com.example.mywebapp.service.HateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HateController {

  private final HateService hateService;

  @PostMapping("/hate")
  public String addHate(@RequestParam Long contentsId){
    return hateService.hateContents(contentsId);
  }

}
