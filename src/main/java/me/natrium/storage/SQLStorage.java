package me.natrium.storage;

import ch.qos.logback.classic.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.natrium.storage.layouts.SQLStorageLayout;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SQLStorage implements SQLStorageComponent {

  Connection connection;
  Logger logger;
  SQLStorageLayout config;

  public SQLStorage(Logger logger, SQLStorageLayout config) {
    this.logger = logger;
    this.config = config;
  }

  @Override
  public void createConnection() {
    try {
      HikariConfig config = new HikariConfig();

      String jdbc = "jdbc:mysql://" +
          this.config.getHost() + ":" + this.config.getPort() + "/" +
          this.config.getDatabase() + this.config.getParams();

      config.setJdbcUrl(jdbc);
      config.setUsername(this.config.getUsername());
      config.setPassword(this.config.getPassword());
      config.setDriverClassName("com.mysql.cj.jdbc.Driver");

      HikariDataSource source = new HikariDataSource(config);

      this.connection = source.getConnection();
      this.logger.info("Connection success");
    } catch (SQLException e) {
      this.logger.error("Connection failed");
    }
  }

  @Override
  public Connection getConnection() {
    return this.connection;
  }
}
