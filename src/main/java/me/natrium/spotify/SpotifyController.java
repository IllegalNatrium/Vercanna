package me.natrium.spotify;

import ch.qos.logback.classic.Logger;
import org.apache.hc.core5.http.ParseException;
import redis.clients.jedis.Jedis;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.enums.AuthorizationScope;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.io.IOException;
import java.net.URI;

public class SpotifyController implements SpotifyControllerComponent {

  SpotifyApi spotify = new SpotifyApi.Builder()
      .setClientId("46f1ff88bf8142338910ecf70ec1c67f")
      .setClientSecret("2bb33d8174f04e7cbb1b442cc12314e9")
      .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:3000/spotify&t=success"))
      .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:3000/spotify&t=failure"))
      .build();

  Jedis jedis;
  Logger logger;

  public SpotifyController(Jedis jedis, Logger logger) {
    this.jedis = jedis;
    this.logger = logger;
  }

  @Override
  public String authorizationCodeSync() {
    URI execute = this.spotify.authorizationCodeUri()
        .scope(
            AuthorizationScope.USER_READ_PRIVATE,
            AuthorizationScope.USER_READ_CURRENTLY_PLAYING,
            AuthorizationScope.USER_READ_PLAYBACK_POSITION,
            AuthorizationScope.USER_READ_PLAYBACK_STATE,
            AuthorizationScope.APP_REMOTE_CONTROL
        )
        .build().execute();
    return execute.toString();
  }

  @Override
  public void authorizeWithCode(String code) {
    this.jedis.set("SPOTIFY_CODE",code);

    AuthorizationCodeCredentials execute = null;
    try {
      execute = this.spotify.authorizationCode(code).build().execute();
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      this.logger.info(e.getLocalizedMessage());
    }

    this.jedis.set("SPOTIFY_TOKEN",execute.getAccessToken());
    this.jedis.set("SPOTIFY_REFRESH_TOKEN",execute.getRefreshToken());

    this.spotify.setAccessToken(execute.getAccessToken());
    this.spotify.setRefreshToken(execute.getRefreshToken());
  }

  @Override
  public SpotifyApi getSpotify() {
    return this.spotify;
  }

  @Override
  public void tryToAuthorize() {

    this.spotify.setRefreshToken(this.jedis.get("SPOTIFY_REFRESH_TOKEN"));

    try {
      AuthorizationCodeCredentials execute = this.spotify.authorizationCodeRefresh().build()
          .execute();

      this.spotify.setRefreshToken(execute.getRefreshToken());
      this.spotify.setAccessToken(execute.getAccessToken());

      this.jedis.set("SPOTIFY_TOKEN",execute.getAccessToken());
      this.jedis.set("SPOTIFY_REFRESH_TOKEN",execute.getRefreshToken());
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      this.logger.info(e.getLocalizedMessage());
    }

  }


}
