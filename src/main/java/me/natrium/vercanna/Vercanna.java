package me.natrium.vercanna;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.gson.Gson;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.AccessLevel;
import lombok.Getter;
import me.natrium.redis.RedisController;
import me.natrium.redis.layouts.RedisServerLayout;
import me.natrium.spotify.SpotifyController;
import me.natrium.storage.SQLStorage;
import me.natrium.vercanna.config.LocalConfig;
import me.natrium.vercanna.config.LocalConfigController;
import me.natrium.vercanna.controllers.CommandController;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Vercanna {

  @Getter(AccessLevel.PUBLIC)
  static JDA jda;
  @Getter(AccessLevel.PUBLIC)
  static SpotifyController spotifyController;

  @Getter(AccessLevel.PUBLIC)
  static RedisController redisController;

  static LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
  static Logger logger = context.getLogger(SpotifyController.class);
  static Logger driver = context.getLogger(HikariPool.class);
  static Logger commands = context.getLogger(CommandController.class);

  static List<Permission> staff_permissions = new ArrayList<>();

  public static void main(String[] args) throws LoginException, InterruptedException {
    driver.setLevel(Level.INFO);

    SQLStorage storage = new SQLStorage(driver); // Database

    LocalConfig config = new LocalConfigController() // Config
        .createConfigurationFolder()
        .createConfigurationFile()
        .getConfiguration();

    redisController = new RedisController(config.getRedisServerLayout());

    spotifyController = new SpotifyController(redisController.getJedis(),logger);

    for (int i = config.getStaffPermissions().size() - 1; i >= 0; i--) {
      staff_permissions.add(Permission.valueOf(config.getStaffPermissions().get(i)));
    }

    storage.createConnection();

    // Command Controller
    CommandController controller = new CommandController(
        commands,
        redisController,
        spotifyController,
        storage.getConnection()
    );

    // Spotify
    spotifyController.tryToAuthorize();

    jda = JDABuilder
        .createDefault(config.getToken())
        .disableCache(
            CacheFlag.ACTIVITY,
            CacheFlag.CLIENT_STATUS,
            CacheFlag.EMOTE,
            CacheFlag.ROLE_TAGS,
            CacheFlag.VOICE_STATE
        )
        .enableCache(CacheFlag.MEMBER_OVERRIDES)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_MESSAGES)
        .addEventListeners(controller)
        .build()
        .awaitReady();

    jda.getPresence()
            .setPresence(
                OnlineStatus.DO_NOT_DISTURB,
                Activity.playing("COD : Vanguard"),
                false);

    for (int i = jda.getGuilds().size() - 1; i >= 0; i--) {
      Guild guild = jda.getGuildById(jda.getGuilds().get(i).getIdLong());
      guild.loadMembers()
          .onError(System.out::println)
          .onSuccess(o -> updateGuildRemote().accept(guild,storage))
          .get();
    }
  }

  static BiConsumer<Guild, SQLStorage> updateGuildRemote() {
    return (g,c) -> {
      List<Member> o = g.getMembers();
      Member guildOwner = getGuildOwner(o);
      List<Member> staffers = getStaffers(o);
      List<Member> robots = getRobots(o);

      StringBuilder buffer = new StringBuilder();

      buffer.append("Guild = () => {\n");

      buffer.append(" GuildOwner = () => ").append(guildOwner.getUser().getAsTag()).append("\n");

      buffer.append(" GuildStaffer = () => {\n");

      for (int i = staffers.size() - 1; i >= 0; i--) {
        buffer.append("  ").append(i).append(" :: ").append(staffers.get(i).getUser().getAsTag()).append("\n");
      }

      buffer.append(" }\n");

      buffer.append(" GuildRobots = () => {\n");

      for (int i = robots.size() - 1; i >= 0; i--) {
        buffer.append("  ").append(i).append(" :: ").append(robots.get(i).getUser().getAsTag()).append("\n");
      }

      buffer.append(" }\n");
      buffer.append("}");

      long guildId = g.getIdLong();
      long owner = guildOwner.getUser().getIdLong();

      c.createGuild(
          guildId,
          owner,
          staffers,
          robots
      );
      System.out.println(buffer);
    };
  }

  static Member getGuildOwner(List<Member> members) {
    return members.stream()
        .filter(Member::isOwner)
        .toList()
        .get(0);
  }

  static List<Member> getStaffers(List<Member> members) {
    return members.stream()
        .filter(o->o.hasPermission(staff_permissions))
        .filter(o->!o.getUser().isBot())
        .collect(Collectors.toList());
  }

  static List<Member> getRobots(List<Member> members) {
    return members.stream()
        .filter(o->o.getUser().isBot())
        .toList();
  }

  static String toJSON(List<Member> extended) {
    List<String> map = extended.stream()
        .map(o -> o.getUser().getAsTag())
        .collect(Collectors.toList());
    return new Gson().toJson(map);
  }
}
