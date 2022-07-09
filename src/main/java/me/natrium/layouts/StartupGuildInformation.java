package me.natrium.layouts;

import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class StartupGuildInformation {

  private final Member guildOwner;
  private final List<Member> guildStaffers;
  private final List<Member> guildBots;

  public Member getGuildOwner() {
    return guildOwner;
  }

  public List<Member> getGuildStaffers() {
    return guildStaffers;
  }

  public List<Member> getGuildBots() {
    return guildBots;
  }

  public static class Builder {

    private Member guildOwner;
    private List<Member> guildStaffers;
    private List<Member> guildBots;

    public Builder setGuildOwner(Member guildOwner) {
      this.guildOwner = guildOwner;
      return this;
    }

    public Builder setGuildStaffers(List<Member> guildStaffers) {
      this.guildStaffers = guildStaffers;
      return this;
    }

    public Builder setGuildBots(List<Member> guildBots) {
      this.guildBots = guildBots;
      return this;
    }

    public StartupGuildInformation build() {
      return new StartupGuildInformation(this);
    }

  }

  public StartupGuildInformation(Builder b) {
    this.guildOwner = b.guildOwner;
    this.guildStaffers = b.guildStaffers;
    this.guildBots = b.guildBots;
  }

}
