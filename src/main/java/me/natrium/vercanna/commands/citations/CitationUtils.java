package me.natrium.vercanna.commands.citations;

import me.natrium.layouts.CitationAuthorLayout;
import me.natrium.layouts.CitationLayout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CitationUtils {

  public static List<CitationLayout> getCitationsByAuthor(String author, Connection connection) {
    List<CitationLayout> layouts = new ArrayList<>();
    try(PreparedStatement state = connection.prepareStatement("SELECT * FROM NATRIUM.citations WHERE author = ?")) {
      state.setString(1,author);
      ResultSet query = state.executeQuery();
      while (query.next()) {
        CitationLayout builder = new CitationLayout.Builder()
            .setCitation(query.getString("citation"))
            .setCreatedAt(query.getString("createdAt"))
            .build();
        layouts.add(builder);
      }
    } catch (SQLException e) {}
    return layouts;
  }
  public static List<CitationAuthorLayout> getAuthors(Connection connection) {
    List<CitationAuthorLayout> layouts = new ArrayList<>();
    List<String> already = new ArrayList<>();
    try (PreparedStatement state = connection.prepareStatement("SELECT * FROM NATRIUM.citations")) {
      CitationAuthorLayout.Builder builder = new CitationAuthorLayout.Builder();

      ResultSet query = state.executeQuery();

      while(query.next()) {
        if(already.stream().anyMatch(o -> {
          try {
            return o.equals(query.getString("author"));
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        })) continue;
        builder.setName(query.getString("author"));
        builder.setManyCitations(getManyCitationsByAuthor(query.getString("author"),connection));
        already.add(query.getString("author"));
        layouts.add(builder.build());
      }
    } catch (SQLException e) {
      System.out.println(e.getLocalizedMessage());
    }
    return layouts;
  }

  public static int getManyCitationsByAuthor(String author, Connection connection) {
    int total = 0;
    try (PreparedStatement state = connection.prepareStatement("SELECT * FROM NATRIUM.citations WHERE author = ?;")) {
      state.setString(1,author);
      ResultSet query = state.executeQuery();
      while(query.next()) {
        total++;
      }
    } catch (SQLException e) { }
    return total;
  }

}
