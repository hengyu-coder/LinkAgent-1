package com.pamirs.attach.plugin.mongodb.interceptor.mongo2_14_3;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.ReadPreference;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/05/20 4:18 下午
 */
public class DBCollectionDistinctInterceptor extends AbstractDBCollectionInterceptor {

    @Override
    protected boolean check(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (args == null) {
            return false;
        }
        return args.length == 3;
    }

    @Override
    protected boolean isRead() {
        return false;
    }

    @Override
    protected Object cutoffShadow(DBCollection ptDbCollection, Advice advice) {
        Object[] args = advice.getParameterArray();
        return ptDbCollection.distinct((String)args[0], (DBObject)args[1], (ReadPreference)args[2]);
    }

}
