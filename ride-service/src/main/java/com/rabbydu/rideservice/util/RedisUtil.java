package com.rabbydu.rideservice.util;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbydu.rideservice.config.PoolConfigurer;

import redis.clients.jedis.Jedis;

@Component
public class RedisUtil {

	@Autowired
	PoolConfigurer jedisPool;

	private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	private static int cacheTimeout = 1;
	
	public Long sadd(final String key, final String... members) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			long ret = jedis.sadd(key, members);

			jedis.expire(key, cacheTimeout * 24 * 60 * 60);

			return ret;
		} catch (Exception ex) {
			logger.error("Exception caught in hgetAll", ex);
		} finally {
			this.releaseResource(jedis);
		}
		return null;
	}

	public Long srem(final String key, final String... members) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			long ret = jedis.srem(key, members);

			jedis.expire(key, cacheTimeout * 24 * 60 * 60);

			return ret;
		} catch (Exception ex) {
			logger.error("Exception caught in srem", ex);
		} finally {
			this.releaseResource(jedis);
		}
		return null;
	}

	public Set<String> smembers(final String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> members = jedis.smembers(key);

			jedis.expire(key, cacheTimeout * 24 * 60 * 60);

			return members;
		} catch (Exception ex) {
			logger.error("Exception caught in smembers", ex);
		} finally {
			this.releaseResource(jedis);
		}
		return new HashSet<String>();
	}

	public long hset(final String key, String field, String value) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			long ret = jedis.hset(key, field, value);

			jedis.expire(key, cacheTimeout * 24 * 60 * 60);
			return ret;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception caught in hmset", ex);
		} finally {
			this.releaseResource(jedis);
		}
		return 0L;
	}

	public String get(String key) {
		String uuid = "";
		Jedis jedis = null;

		try {
			jedis = jedisPool.getResource();
			uuid = jedis.get(key);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("error", e);
		} finally {
			this.releaseResource(jedis);
		}
		return uuid;
	}

	public void set(String key, String value) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("error", e);
		} finally {
			this.releaseResource(jedis);
		}
	}

	public void del(String key) {
		Jedis jedis = null;

		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("error", e);
		} finally {
			if (jedis != null) {
				try {
					jedisPool.closeJedis(jedis);
				} catch (Exception e) {
					logger.error("error", e);
				}
			}
		}
	}

	public void releaseResource(Jedis jedis) {
		try {
			if (jedis != null)
				jedis.close();
		} catch (Exception ex) {
			logger.error("error to release redis connection from pool," + ex);
		}
	}

}
