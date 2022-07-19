package me.natrium.storage.layouts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import javax.annotation.Nullable;

public class SQLStorageLayout {

  @SerializedName("HOST")
  @Getter
  @Expose
  private String host;
  @SerializedName("PORT")
  @Getter
  @Expose
  private String port;
  @SerializedName("USERNAME")
  @Getter
  @Expose
  private String username;
  @SerializedName("PASSWORD")
  @Getter
  @Expose
  private String password;
  @SerializedName("DATABASE")
  @Getter
  @Expose
  @Nullable
  private String database;
  @SerializedName("PARAMS")
  @Getter
  @Expose
  @Nullable
  private String params;

}
