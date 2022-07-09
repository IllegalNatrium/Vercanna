package me.natrium.storage;

import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.util.List;

public interface SQLStorageComponent {

  void createConnection();
  Connection getConnection();

  boolean existsGuild(long guildId);
  void deleteGuild(long guildId);

  void createGuild(long guildId, long guildOwnerId, List<Member> staffers, List<Member> bots);
  void createGuildStaffers(long guildId, String guildStaffer);
  void createGuildRobots(long guildId, String guildRobot);

}
