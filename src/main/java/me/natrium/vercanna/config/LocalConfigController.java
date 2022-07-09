package me.natrium.vercanna.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class LocalConfigController {

  private File dir;
  private File config;

  public LocalConfig getConfiguration() {
    try {
      FileReader reader = new FileReader(this.config);
      Type type = new TypeToken<LocalConfig>() {
      }.getType();
      return new Gson().fromJson(reader,type);
    } catch (IOException e) { }
    return null;
  }

  public LocalConfigController createConfigurationFolder() {
    this.dir = new File("config");
    if (!this.dir.exists())
      this.dir.mkdir();
    return this;
  }

  public LocalConfigController createConfigurationFile() {
    try {
      this.config = new File(this.dir,"config.json");
      if(!this.config.exists()) {
        this.config.createNewFile();
        FileUtils.copyToFile(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("config_stream.json"),
            this.config
        );
        return this;
      }
    } catch (IOException e) { }
    return this;
  }

}
