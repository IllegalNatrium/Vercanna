package me.natrium.spotify;

import se.michaelthelin.spotify.SpotifyApi;

public interface SpotifyControllerComponent {

  String authorizationCodeSync();
  void authorizeWithCode(String code);
  SpotifyApi getSpotify();
  void tryToAuthorize();

}
