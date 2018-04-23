package com.sakuray;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class CommonObjectPool extends GenericObjectPool<Student> {

    public CommonObjectPool(PooledObjectFactory<Student> factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
