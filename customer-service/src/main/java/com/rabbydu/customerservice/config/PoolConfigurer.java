package com.rabbydu.customerservice.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class PoolConfigurer {

	private JedisPool jedisPool;
	
	public void configure(String password, String redisMaster, int port)
	{

		JedisPoolConfig poolConfig = new JedisPoolConfig();

		poolConfig.setMaxTotal(200);

		poolConfig.setMaxIdle(200);

		poolConfig.setMinIdle(50);

		/*
		 * Sets whether objects borrowed from the pool will be validated before being returned. If the object fails to validate, it will be
		 * removed from the pool and destroyed, and a new attempt will be made to
		 * borrow an object from the pool.
		 */
		poolConfig.setTestOnBorrow(true); 

		/*
		 * Sets whether objects borrowed from the pool will be validated when
		 * they are returned to the pool.
		 * Returning objects that fail validation
		 * are destroyed rather then being returned the pool.
		 */
		poolConfig.setTestOnReturn(true);



		/*
		 * Returns whether objects sitting idle in the pool will be validated by the
		 * idle object evictor. If the object fails to validate, it will be removed from
		 * the pool and destroyed. Setting this property has no effect
		 * unless the idle object evictor is enabled by setting
		 * <code>timeBetweenEvictionRunsMillis</code> to a positive value.
		 */
		poolConfig.setTestWhileIdle(true);

		/*
		 * Sets the maximum number of objects to examine during each run (if any)
		 * of the idle object evictor thread.
		 */
		poolConfig.setNumTestsPerEvictionRun(100);

		/*
		 * Returns the number of milliseconds to sleep between runs of the idle
		 * object evictor thread. When non-positive, no idle object evictor thread
		 * will be run.
		 */
		poolConfig.setTimeBetweenEvictionRunsMillis(2000); //60000 is the default value

		jedisPool = new JedisPool(poolConfig, redisMaster, port, 6000, password);
	}

	public Jedis getResource(){
		return jedisPool.getResource();
	}

	public void closeJedis(Jedis jedis){
		jedis.close();
//		jedisPool.returnResourceObject(jedis);
	}

	public void returnInstance(Jedis jedis) {
		jedis.close();
//		jedisPool.returnResourceObject(jedis);
	}

	public int getNumActive(){
		return jedisPool.getNumActive();
	}

	public int getNumIdle(){
		return jedisPool.getNumIdle();
	}

	public int getNumWaiters(){

		return jedisPool.getNumWaiters();
	}

	public void destroyPool(){
		jedisPool.destroy();
	}

}
