package me.natrium.storage;

import ch.qos.logback.classic.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SQLStorage implements SQLStorageComponent {

  Connection connection;
  Logger logger;

  public SQLStorage(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void createConnection() {
    try {
      HikariConfig config = new HikariConfig();

      config.setJdbcUrl("jdbc:mysql://localhost:3306/Vercanna?autoReconnect=true&useSSL=false");
      config.setUsername("natrium");
      config.setPassword("1986");
      config.setDriverClassName("com.mysql.cj.jdbc.Driver");

      HikariDataSource source = new HikariDataSource(config);

      this.connection = source.getConnection();
      this.logger.info("Connection success");
    } catch (SQLException e) {
      this.logger.error("Connection failed");
    }
  }

  @Override
  public boolean existsGuild(long guildId) {
    try (PreparedStatement state = this.connection.prepareStatement("SELECT * FROM Guilds WHERE guildId = ?;")) {
      state.setLong(1,guildId);
      if (state.executeQuery().next()) {
        state.close();
        return true;
      }
    } catch (SQLException e) {
      this.logger.info(e.getLocalizedMessage());
    }
    return false;
  }

  @Override
  public void deleteGuild(long guildId) {
    try (PreparedStatement statement = this.connection.prepareStatement("DELETE FROM Guilds WHERE guildId = ?;")) {
      statement.setLong(1,guildId);
      statement.execute();
    } catch (SQLException e) {
      this.logger.info(e.getLocalizedMessage());
    }
  }

  @Override
  public void createGuild(long guildId, long guildOwnerId, List<Member> staffers, List<Member> bots) {
    try (PreparedStatement state = this.connection.prepareStatement(
        "INSERT INTO Guilds (" +
            "guildId, " +
            "guildOwnerId) VALUES (?,?);"
    )) {
      state.setLong(1,guildId);
      state.setLong(2,guildOwnerId);
      state.execute();
      for (int i = staffers.size() - 1; i >= 0; i--) {
        createGuildStaffers(guildId,staffers.get(i).getUser().getAsTag());
      }
      for (int i = bots.size() - 1; i >= 0; i--) {
        createGuildRobots(guildId,bots.get(i).getUser().getAsTag());
      }
    } catch (SQLException e) {
      this.logger.info(e.getLocalizedMessage());
    }
  }

  @Override
  public void createGuildStaffers(long guildId, String guildStaffer) {
    try (PreparedStatement state = this.connection.prepareStatement(
        "INSERT INTO GuildStaffers (guildId,username) VALUES (?,?);"
    )) {
      state.setLong(1,guildId);
      state.setString(2,guildStaffer);
      state.execute();
    } catch (SQLException e) {
      this.logger.info(e.getLocalizedMessage());
    }
  }

  @Override
  public void createGuildRobots(long guildId, String guildRobot) {
    try (PreparedStatement state = this.connection.prepareStatement(
        "INSERT INTO GuildRobots (guildId,robotName) VALUES (?,?);"
    )) {
      state.setLong(1,guildId);
      state.setString(2,guildRobot);
      state.execute();
    } catch (SQLException e) {
      this.logger.info(e.getLocalizedMessage());
    }
  }

  @Override
  public Connection getConnection() {
    return this.connection;
  }
}
