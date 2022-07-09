package me.natrium.spotify.layouts;

import net.dv8tion.jda.api.entities.Member;

public class VercannaSpotifyClient {

  private Member author;
  private String token;
  private String refreshToken;
  private String code;

  public Member getAuthor() {
    return author;
  }

  public String getToken() {
    return token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public String getCode() {
    return code;
  }

  public static class Builder {

    private Member author;
    private String token;
    private String refreshToken;
    private String code;

    public Builder setAuthor(Member author) {
      this.author = author;
      return this;
    }

    public Builder setToken(String token) {
      this.token = token;
      return this;
    }

    public Builder setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
      return this;
    }

    public Builder setCode(String code) {
      this.code = code;
      return this;
    }

    public VercannaSpotifyClient build() {
      return new VercannaSpotifyClient(this);
    }
  }

  private VercannaSpotifyClient(Builder b) {
    this.author = b.author;
    this.token = b.token;
    this.refreshToken = b.refreshToken;
    this.code = b.code;
  }
}
