package com.example.mywebapp.domain.comment;

import com.example.mywebapp.domain.Timestamped;
import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.member.Member;
import com.example.mywebapp.dto.request.contents.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Comments extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Lob
  private String comments;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @JsonIgnore
  private Member member;

  @ManyToOne
  @JoinColumn(name = "contents_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @JsonIgnore
  private Contents contents;

  @Builder
  public Comments(final String content, final Member member, final Contents contents) {
    this.comments = content;
    this.member = member;
    this.contents = contents;
  }

  public void update(Comments updateComments, CommentRequestDto dto) {
    updateComments.comments = dto.getComments();
  }
}
