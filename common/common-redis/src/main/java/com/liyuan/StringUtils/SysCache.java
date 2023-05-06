package com.liyuan.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * @author liyuan
 * @date 2023/1/6
 * @project exam-cloud
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysCache {
    /** 缓存名称 */
    private String cacheName;

    /** 缓存键名 */
    private String cacheKey;

    /** 缓存内容 */
    private Object cacheValue;

    /** 备注 */
    private String remark;

    public SysCache(String cacheName, String remark)
    {
        this.cacheName = cacheName;
        this.remark = remark;
    }

    public SysCache(String cacheName, String cacheKey, Object cacheValue)
    {
        this.cacheName = StringUtils.replace(cacheName, ":", "");
        this.cacheKey = StringUtils.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }
}
