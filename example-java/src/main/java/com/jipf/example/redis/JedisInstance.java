package com.jipf.example.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JedisInstance {

    private static final JedisSentinelPool pool;

    static {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxWaitMillis(-1);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);

        int timeout = 60000;
        int database = 0;
        String password = "123456";

        Set<String> sentinels = new HashSet<>(Arrays.asList(
                "127.0.0.1:26380",
                "127.0.0.1:26381",
                "127.0.0.1:26382"
        ));

        pool = new JedisSentinelPool("mymaster", sentinels, jedisPoolConfig, timeout, password, database);
    }

    public static Jedis getJedisInstance() {

        return pool.getResource();
    }

    public static void main(String[] args) {

        Jedis jedis = getJedisInstance();

        /** start 操作*/

        /** end */

        jedis.close();
    }
}
