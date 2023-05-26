package com.example.mywebapp.domain.like;

import com.example.mywebapp.domain.Timestamped;
import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.member.Member;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Likes extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private boolean status;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
      CascadeType.DETACH})
  @JoinColumn(name = "member_id", nullable = false, updatable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contents_id", nullable = false)
  private Contents contents;


  public Likes(Contents contents, Member member) {
    this.contents = contents;
    this.member = member;
    this.status = true;
  }

  public void unLikeContents(Contents contents) {
    this.status = false;
    contents.setLiked(contents.getLiked() - 1);
  }
}
