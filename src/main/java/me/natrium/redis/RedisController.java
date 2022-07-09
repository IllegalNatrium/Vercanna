package me.natrium.redis;

import me.natrium.redis.layouts.RedisServerLayout;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisController implements RedisControllerComponent {

  private final JedisPool pool;

  public RedisController(RedisServerLayout server) {
    this.pool = new JedisPool(server.getHost(),server.getPort());
  }

  @Override
  public void dispose() {
    this.pool.destroy();
  }

  @Override
  public Jedis getJedis() {
    return this.pool.getResource();
  }
}
