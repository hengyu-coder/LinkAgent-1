package com.pamirs.attach.plugin.common.datasource.redisserver;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.factory
 * @Date 2020/11/26 11:43 上午
 */
public class RedisClientMediator<T> {
    protected final static Logger logger = LoggerFactory.getLogger(RedisClientMediator.class);

    protected T performanceRedisClient;

    protected T businessRedisClient;

    /**
     * true:影子库
     * false:影子表
     */
    private boolean useKey = false;

    public RedisClientMediator(T businessRedisClient, T performanceRedisClient) {
        this.performanceRedisClient = performanceRedisClient;
        this.businessRedisClient = businessRedisClient;
        this.useKey = isShadowDb();
    }

    public RedisClientMediator(T performanceRedisClient, T businessRedisClient, boolean useKey) {
        this.performanceRedisClient = performanceRedisClient;
        this.businessRedisClient = businessRedisClient;
        this.useKey = useKey;
    }

    public T getClient() {
        if (Pradar.isClusterTest()) {
            if (!useKey) {
                if (businessRedisClient == null) {
                    throw new PressureMeasureError("Business dataSource is null.");
                }
                return businessRedisClient;
            } else {
                if (performanceRedisClient == null) {
                    throw new PressureMeasureError("Performance dataSource is null.");
                }
                return performanceRedisClient;
            }
        }

        if (Pradar.isClusterTest()) {
            throw new PressureMeasureError("get connection failed by RedisClientMediator. get a pressurement request in business datasource.");
        }
        return businessRedisClient;
    }

    public static boolean isShadowDb() {
        return GlobalConfig.getInstance().isShadowDbRedisServer();
    }

    public T getPerformanceRedisClient() {
        return performanceRedisClient;
    }

    public void destroy() {
    }
}
