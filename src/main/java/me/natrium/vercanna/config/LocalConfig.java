package me.natrium.vercanna.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import me.natrium.storage.layouts.SQLStorageLayout;

import java.util.List;

public class LocalConfig {

  @Expose
  @SerializedName("TOKEN")
  @Getter
  private String token;

  @Expose
  @SerializedName("STAFF_PERMISSIONS")
  @Getter
  private List<String> staffPermissions;

  @Expose
  @SerializedName("MYSQL")
  @Getter
  private SQLStorageLayout sqlStorage;

}
