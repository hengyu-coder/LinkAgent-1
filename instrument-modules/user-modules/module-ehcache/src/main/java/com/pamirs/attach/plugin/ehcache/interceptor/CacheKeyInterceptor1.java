/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.ehcache.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.cache.ClusterTestCacheWrapperKey;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.ProcessController;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import net.sf.ehcache.Element;
import org.apache.commons.lang.ArrayUtils;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/19 7:50 下午
 */
public class CacheKeyInterceptor1 extends AroundInterceptor {
    @Override
    public void doBefore(Advice advice) throws Throwable {
        if (!Pradar.isClusterTest()) {
            return;
        }
        Object[] args = advice.getParameterArray();
        if (ArrayUtils.isEmpty(args)) {
            return;
        }

        Object key = advice.getParameterArray()[0];
        if (!(key instanceof ClusterTestCacheWrapperKey)) {
            advice.changeParameter(0, new ClusterTestCacheWrapperKey(key));
        }
    }

    @Override
    public void doAfter(Advice advice) throws Throwable {
        Object result = advice.getReturnObj();
        if (!(result instanceof Element)) {
            return;
        }
        Element element = (Element) result;
        if (element.getObjectKey() instanceof ClusterTestCacheWrapperKey) {
            Element ele = new Element(((ClusterTestCacheWrapperKey) element.getObjectKey()).getKey()
                    , element.getObjectValue()
                    , element.getVersion()
                    , element.getCreationTime()
                    , element.getLastAccessTime()
                    , element.getHitCount()
                    , element.usesCacheDefaultLifespan()
                    , element.getTimeToLive()
                    , element.getTimeToIdle()
                    , element.getLastUpdateTime());
            ProcessController.returnImmediately(ele);
        }
    }
}
