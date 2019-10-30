package com.redislabs.demo;

import java.util.logging.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;


public class JedisTest {

    private static final Logger LOGGER = Logger.getLogger(JedisTest.class.getName());

    private static String host = "localhost";

    private static int port = 6379;

    private JedisPoolConfig config;

    private JedisPool pool;

    public static void main(String[] args) {
        JedisTest test = new JedisTest();
        test.setup(host, port);
        if (test.isConnected()) {
            LOGGER.info("Jedis is connected.");
            test.test1();
            test.test2();
        }
        test.shutdown();
    }

    public void setup(String host, int port) {
        config = new JedisPoolConfig();
        pool = new JedisPool(config, host, port);
    }

    public boolean isConnected() {
        Jedis jedis = pool.getResource();
        try {
            if (jedis.isConnected()) {
                return true;
            }
        } catch (JedisConnectionException e) {
            LOGGER.warning(e.getMessage());
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    public void test1() {
        Jedis jedis = pool.getResource();
        LOGGER.info(jedis.set("foo", "1"));
        jedis.close();
    }

    public void test2() {
        Jedis jedis = pool.getResource();
        jedis.incr("foo");
        LOGGER.info(jedis.get("foo"));
        jedis.close();
    }

    public void shutdown() {
        pool.close();
    }
}
