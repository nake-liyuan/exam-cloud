package com.liyuan.controller.cache;

import com.liyuan.StringUtils.SysCache;
import com.liyuan.api.Result;
import com.liyuan.constant.AuthConstant;
import com.liyuan.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author liyuan
 * @date 2023/1/6
 * @project exam-cloud
 */
@RestController
@RequestMapping("cache")
@RequiredArgsConstructor
@Api(tags = "redis缓存接口")
public class CacheController {

    private final RedisTemplate<String,String> redisTemplate;
    private final RedisService redisService;

    private final static List<SysCache> caches = new ArrayList<SysCache>();
    {
        caches.add(new SysCache(AuthConstant.RESOURCE_ROLES_MAP_KEY.substring(0,AuthConstant.RESOURCE_ROLES_MAP_KEY.indexOf(":")), "权限规则"));
        caches.add(new SysCache(AuthConstant.USER_SUBJECT_CODE_PREFIX.substring(0,AuthConstant.USER_SUBJECT_CODE_PREFIX.indexOf(":")), "用户学科资源"));
        caches.add(new SysCache(AuthConstant.TOKEN_BLACKLIST_PREFIX.substring(0,AuthConstant.TOKEN_BLACKLIST_PREFIX.indexOf(":")), "黑名单token"));
        caches.add(new SysCache(AuthConstant.Email_verification_code.substring(0,AuthConstant.Email_verification_code.indexOf(":")), "邮箱验证码"));
        caches.add(new SysCache(AuthConstant.SMS_CODE_PREFIX.substring(0,AuthConstant.SMS_CODE_PREFIX.indexOf(":")), "短信验证码"));
        caches.add(new SysCache(AuthConstant.VALIDATION_CODE_KEY_PREFIX.substring(0,AuthConstant.VALIDATION_CODE_KEY_PREFIX.indexOf(":")), "验证码"));
    }

    @ApiOperation("redis信息查询")
    @GetMapping("/info")
    public Result getCacheInfo(){
        // 获取redis缓存完整信息
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        // 获取redis缓存命令统计信息
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        // 获取redis缓存中可用键Key的总数
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        assert commandStats != null;
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);

        return Result.success(result);
    }

    @ApiOperation("缓存名查询")
    @GetMapping("/getNames")
    public Result<List<SysCache>> cache()
    {
        return Result.success(caches);
    }

    @ApiOperation("缓存键查询（根据缓存名）")
    @GetMapping("/getKeys/{cacheName}")
    public Result<Set<String>> getCacheKeys(@PathVariable String cacheName)
    {
        Set<String> cacheKeys = redisTemplate.keys(cacheName+"*");
        return Result.success(cacheKeys);
    }

    @ApiOperation("缓存值查询（根据缓存名、缓存值）")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public Result<SysCache> getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey)
    {
        Object cacheValue = null;
        if(cacheName.equals(AuthConstant.USER_SUBJECT_CODE_PREFIX)){
            cacheValue=redisService.sMembers(cacheKey);
        }
        if(cacheKey.equals(AuthConstant.RESOURCE_ROLES_MAP_KEY)){
            cacheValue=redisService.hGetAll(cacheKey);
        }if(cacheKey.equals(AuthConstant.USER_CURRENT_SUBJECT_CODE_PREFIX)){
            cacheValue=redisService.hGetAll(cacheKey);
        }
        if(cacheName.contains("_")&& "code:".equals(cacheName.split("_")[1])){
            cacheValue=redisService.get(cacheKey);
        }

        SysCache sysCache = new SysCache(cacheName, cacheKey, cacheValue);
        return Result.success(sysCache);
    }

    @ApiOperation("清除缓存（按值）")
    @DeleteMapping("/clearCacheName/{cacheName}")
    public Result<Object> clearCacheName(@PathVariable String cacheName)
    {
        Collection<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        assert cacheKeys != null;
        redisTemplate.delete(cacheKeys);
        return Result.success();
    }

    @ApiOperation("清除缓存（按键）")
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public Result<Boolean> clearCacheKey(@PathVariable String cacheKey)
    {
        Boolean delete = redisTemplate.delete(cacheKey);
        return Result.judge(Boolean.TRUE.equals(delete));
    }

    @ApiOperation("清除所有缓存信息")
    @DeleteMapping("/clearCacheAll")
    public Result<Collection<String>> clearCacheAll()
    {
        Collection<String> cacheKeys = redisTemplate.keys("*");
        assert cacheKeys != null;
        redisTemplate.delete(cacheKeys);
        return Result.success();
    }
}
