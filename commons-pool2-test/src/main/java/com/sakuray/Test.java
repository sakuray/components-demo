package com.sakuray;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class Test {

    public static void main(String[] args) {
        StudentFactory studentFactory = new StudentFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        AbandonedConfig abandonedConfig = new AbandonedConfig();
        CommonObjectPool pool = new CommonObjectPool(studentFactory, config, abandonedConfig);

        Student student = null;
        try {
            student = pool.borrowObject();
            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(student != null) pool.returnObject(student);
        }
    }
}
