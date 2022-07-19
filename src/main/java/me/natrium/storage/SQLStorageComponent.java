package me.natrium.storage;

import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.util.List;

public interface SQLStorageComponent {

  void createConnection();
  Connection getConnection();

}
