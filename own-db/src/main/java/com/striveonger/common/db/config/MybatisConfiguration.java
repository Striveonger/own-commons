package com.striveonger.common.db.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis-Flex 配置文件
 * @author Mr.Lee
 * @since 2023-10-20 16:37
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisConfiguration implements MyBatisFlexCustomizer {
    private final Logger log = LoggerFactory.getLogger(MybatisConfiguration.class);

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        // 包名的命名规则
        configurer.setBasePackage("com.striveonger.*.*.mapper");
        return configurer;
    }

    @Override
    public void customize(FlexGlobalConfig config) {
        // 开启审计功能
        AuditManager.setAuditEnable(true);
        // 设置 SQL 审计收集器
        AuditManager.setMessageCollector(message -> log.info("{}, {}ms", message.getFullSql(), message.getElapsedTime()));

        config.setLogicDeleteColumn("deleted");
        config.setNormalValueOfLogicDelete(0);
        config.setDeletedValueOfLogicDelete(1);
        config.setPrintBanner(false);
    }
}
