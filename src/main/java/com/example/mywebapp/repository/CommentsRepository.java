package com.example.mywebapp.repository;


import com.example.mywebapp.domain.comment.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

}
