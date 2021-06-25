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
package com.pamirs.attach.plugin.apache.kafka.header.impl;

import com.pamirs.attach.plugin.apache.kafka.header.HeaderGetter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/10 7:54 下午
 */
public class DefaultHeaderGetter implements HeaderGetter {
    @Override
    public Map<String, String> getHeaders(ConsumerRecord consumerRecord) {
        Headers headers = consumerRecord.headers();
        Header[] headersArr = headers.toArray();
        if (headersArr == null || headersArr.length == 0) {
            return Collections.EMPTY_MAP;
        }
        Map<String, String> ctx = new HashMap<String, String>();
        for (Header header : headersArr) {
            if (!TRACE_HEADERS.contains(header.key())) {
                continue;
            }
            ctx.put(header.key(), new String(header.value()));
        }

        return ctx;
    }
}
