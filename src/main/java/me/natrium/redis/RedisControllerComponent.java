package me.natrium.redis;

import redis.clients.jedis.Jedis;

public interface RedisControllerComponent {
  void dispose();
  Jedis getJedis();
}
