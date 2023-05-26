package com.example.mywebapp.domain.member;

import com.example.mywebapp.domain.Timestamped;
import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.member.enums.Adult;
import com.example.mywebapp.domain.member.enums.Dormant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Timestamped implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  @JsonProperty(access = Access.WRITE_ONLY)
  private String password;
  @Column
  @JsonProperty(access = Access.WRITE_ONLY)
  private String isDormant;
  @Column
  @JsonProperty(access = Access.WRITE_ONLY)
  private String isAdult;

  @Column
  private int adultViewCnt;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Contents> contentsList;

  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "memberDetails_id")
  private MemberInfo memberInfo;


  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  @JsonProperty(access = Access.WRITE_ONLY)
  private List<String> roles = new ArrayList<>();


  public void changeDormantStat(Member member, Dormant dormant) {
    member.isDormant = dormant.getDormant();
  }

  public void changeAdultStat(Member member, Adult adult) {
    member.isAdult = adult.getAdult();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public String getUsername() {
    return this.username;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isEnabled() {
    return true;
  }
}
