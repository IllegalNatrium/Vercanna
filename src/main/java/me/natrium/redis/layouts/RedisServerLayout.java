package me.natrium.redis.layouts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedisServerLayout {

  @SerializedName("HOST")
  @Expose
  private String host;
  @SerializedName("PORT")
  @Expose
  private int port;

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public static class Builder {
    private String host;
    private int port;

    public Builder setHost(String host) {
      this.host = host;
      return this;
    }

    public Builder setPort(int port) {
      this.port = port;
      return this;
    }

    public RedisServerLayout build() {
      return new RedisServerLayout(this);
    }

  }

  private RedisServerLayout(Builder b) {
    this.host = b.host;
    this.port = b.port;
  }

}
