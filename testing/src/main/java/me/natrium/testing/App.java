package me.natrium.testing;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class App {

  static HikariConfig config = new HikariConfig();
  static HikariDataSource source;

  public static void main(String[] args) throws SQLException {

    config.setDriverClassName("com.mysql.cj.jdbc.Driver");
    config.setJdbcUrl("jdbc:mysql://localhost:3306/NATRIUM?autoReconnect=true&useSSL=false");
    config.setUsername("natrium");
    config.setPassword("1986");

    source = new HikariDataSource(config);

    Connection connection = source.getConnection();
    update(connection);
  }

  static boolean update(Connection connection) {
    String date = Date.from(Instant.now()).toString();
    try(PreparedStatement state = connection.prepareStatement("UPDATE citations SET createdAt = ? WHERE createdAt = \"{{DATENOW}}\"")) {
      state.setString(1,date);
      state.execute();
    } catch (SQLException e) { }
    return true;
  }

}
