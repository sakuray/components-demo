package com.demo.dark.database;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.history.HistoryLevel;

/**
 * @author Administrator
 * 初始化数据库，相关配置修改src/test/resources下的activiti.cfg.xml
 */
public class InitDB {

    public static void main(String[] args) {
        ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResourceDefault()
            .setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_CREATE)
//            .setDbIdentityUsed(false) // 不创建identity相关的表
//            .setHistoryLevel(HistoryLevel.NONE)   // 不创建history相关的表
            .buildProcessEngine();
    }
}
