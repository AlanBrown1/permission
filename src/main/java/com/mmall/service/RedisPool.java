package com.mmall.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @author Alan Brown
 * @date 2018/6/4 20:24
 */
@Service("redisPool")
@Slf4j
public class RedisPool {

	// 这个来自配置文件redis.xml
	@Resource(name = "shardedJedisPool")
	private ShardedJedisPool shardedJedisPool;

	// 从池中获取一个连接
	public ShardedJedis instance(){
		return shardedJedisPool.getResource();
	}

	// 用完之后关闭连接
	public void safeClose(ShardedJedis shardedJedis){
		try{
			if(shardedJedis != null) shardedJedis.close();
		}catch (Exception e){
			log.error("return redis resource exception, ", e);
		}
	}

}
