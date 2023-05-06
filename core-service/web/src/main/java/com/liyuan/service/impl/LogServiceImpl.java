package com.liyuan.service.impl;

import cn.hutool.json.JSONObject;
import com.liyuan.api.Result;
import com.liyuan.constant.AuthConstant;
import com.liyuan.service.LogService;
import com.liyuan.utils.tokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liyuan
 * @date 2023/3/12
 * @project exam-cloud
 */
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final RestHighLevelClient client;
    private final RedisTemplate redisTemplate;

    @SneakyThrows
    @Override
    public Result logByPage(Integer pageNum, Integer pageSize,
                            String serviceName, String type, Date date) {
        String index = "exam-cloud-" +serviceName+"-"+type+"-"+ new SimpleDateFormat("yyyy.MM.dd").format(date);
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder
                .from(pageNum-1)
                .size(pageSize)
                .sort("@timestamp", SortOrder.DESC)
                .query(QueryBuilders.matchAllQuery());
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(sourceBuilder);
        // 执行查询
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        long total = searchResponse.getHits().getTotalHits().value;
        ArrayList<Object> data = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> highlightFields = hit.getSourceAsMap();
            HashMap<String, Object> logInfo = new HashMap<>(highlightFields);
            data.add(logInfo);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return Result.success(result);
    }

    @SneakyThrows
    @Override
    public Result loginLogByPage(Integer pageNum, Integer pageSize, Date date,
                                 String username, String clientIP,Date startTime,Date endTime) {
        String index = "exam-cloud-exam-auth-login-" + new SimpleDateFormat("yyyy.MM.dd").format(date);
        SearchRequest searchRequest = new SearchRequest(index);

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        if (StringUtils.hasText(username)) {
            boolBuilder.must(QueryBuilders.termQuery("username", username));
        }
        if (StringUtils.hasText(clientIP)) {
            boolBuilder.must(QueryBuilders.termQuery("clientIP", clientIP));
        }
        if(startTime!=null&&endTime!=null){
            boolBuilder.must(QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(endTime));
        }
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder
                .from(pageNum-1)
                .size(pageSize)
                .sort("@timestamp", SortOrder.DESC)
                .query(boolBuilder);
        searchRequest.source(sourceBuilder);
        // 执行查询
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        long total = searchResponse.getHits().getTotalHits().value;
        ArrayList<Object> data = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> highlightFields = hit.getSourceAsMap();
            HashMap<String, Object> logInfo = new HashMap<>(highlightFields);
            logInfo.put("id",hit.getId());
            String token = (String) logInfo.get("token");
            Boolean isBlackJwt = redisTemplate.hasKey(AuthConstant.TOKEN_BLACKLIST_PREFIX + tokenUtils.jti(token));
            if(Boolean.TRUE.equals(isBlackJwt)){
                logInfo.put("tokenState","禁用");
            }else {
                logInfo.put("tokenState",tokenUtils.expirationDetection(token));
            }
            data.add(logInfo);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("data",data);
        return Result.success(result);
    }

    @SneakyThrows
    @Override
    public void forcedOffline(String token) {
        JSONObject payload = tokenUtils.payload(token);
        // JWT唯一标识
        String jti = payload.getStr("jti");
        // JWT过期时间戳(单位：秒)
        Long expireTime = payload.getLong("exp");
        if (expireTime != null) {
            // 当前时间（单位：秒）
            long currentTime = System.currentTimeMillis() / 1000;
            // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
            if (expireTime > currentTime) {
                redisTemplate.opsForValue().set(AuthConstant.TOKEN_BLACKLIST_PREFIX + jti, null, (expireTime - currentTime), TimeUnit.SECONDS);
            }
        } else {
            // token 永不过期则永久加入黑名单
            redisTemplate.opsForValue().set(AuthConstant.TOKEN_BLACKLIST_PREFIX + jti, null);
        }
    }

}
