package com.example.mywebapp.domain.content;


import com.example.mywebapp.domain.Timestamped;
import com.example.mywebapp.domain.history.ContentsHistory;
import com.example.mywebapp.domain.like.Likes;
import com.example.mywebapp.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Contents extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String title;

  @Column
  private String author;

  @Column
  private String isFree;

  @Column
  private String isAdult;

  @Column
  private int coin;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "member_id", nullable = false, updatable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private Member member;

  @OneToMany(mappedBy = "contents", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Likes> likes;

  @OneToMany(mappedBy = "contents", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<ContentsHistory> contentsHistories;

  @Column(nullable = false)
  private int liked; // 추천 수

  @Column(nullable = false)
  private int hated;

  @Builder
  public Contents(String title, String author, String isFree, String isAdult, Member member, int liked, int hated) {
    this.title = title;
    this.author = author;
    this.isFree = isFree;
    this.isAdult = isAdult;
    this.member = member;
    this.liked = liked;
    this.hated = hated;
  }

  public void isFree(Contents contents, ContentsType isFree) {
    contents.isFree = String.valueOf(isFree.status);
    if (isFree == ContentsType.FREE) {
      contents.coin = 0;
    }
  }

  public void setCoin(Contents contents, int setCoin) {
    contents.coin = setCoin;
    contents.isFree = ContentsType.PAYED.status;
  }
}
