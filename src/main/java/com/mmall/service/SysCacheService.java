package com.mmall.service;

import com.google.common.base.Joiner;
import com.mmall.beans.CacheKeyConstants;
import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * @author Alan Brown
 * @date 2018/6/4 20:30
 */

// 在哪里使用这个缓存呢？
// 权限、角色、用户、以及角色用户、角色权限关系的修改不能作缓存，因为修改必须要能实时看到
// 应该在权限拦截的地方做缓存，即判断某个用户是否具有访问某个url的权限时，所以应该对当前用户已经拥有的权限集合进行缓存

@Service
@Slf4j
public class SysCacheService {

	@Resource(name = "redisPool")
	private RedisPool redisPool;

	// 保存数据到redis进行缓存起来
	// 被缓存的值，过期时间（单位秒），缓存的键
	public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix){
		saveCache(toSavedValue, timeoutSeconds, prefix);
	}

	public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys){
		if(toSavedValue == null) return;
		ShardedJedis shardedJedis = null;
		try {
			// 缓存的键
			String cacheKey = generateCacheKey(prefix, keys);
			// 拿到redis连接
			shardedJedis = redisPool.instance();
			shardedJedis.setex(cacheKey, timeoutSeconds, toSavedValue);
		}catch (Exception e){
			log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys), e);
		}finally {
			// 关闭连接
			redisPool.safeClose(shardedJedis);
		}
	}

	// 从redis中获取缓存的值
	public String getFromCache(CacheKeyConstants prefix, String... keys){
		ShardedJedis shardedJedis = null;
		// 拿到键
		String cacheKey = generateCacheKey(prefix, keys);
		try{
			// 获取一个redis连接
			shardedJedis = redisPool.instance();
			// 拿到值
			String value = shardedJedis.get(cacheKey);
			return value;
		}catch (Exception e){
			log.error("get from cache exception, prefix:{},keys:{}", prefix, JsonMapper.obj2String(keys), e);
		}finally {
			// 关闭连接
			redisPool.safeClose(shardedJedis);
		}
		return null;
	}



	// 生成该缓存对应的键
	private String generateCacheKey(CacheKeyConstants prefix, String... keys){
		String key = prefix.name(); // 前缀
		if( keys != null && keys.length > 0 ){
			// 将前缀、被缓存的key都用 _ 连接起来
			key += "_" + Joiner.on("_").join(keys);
		}
		return key;
	}
}
