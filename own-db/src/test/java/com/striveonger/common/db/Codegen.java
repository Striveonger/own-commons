package com.striveonger.common.db;

import com.striveonger.common.db.entity.BaseEntity;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @since 2023-12-13 17:12
 */
public class Codegen {
    private final Logger log = LoggerFactory.getLogger(Codegen.class);

    public static void main(String[] args) {
        // 1.配置数据源
        HikariDataSource source = new HikariDataSource();
        source.setJdbcUrl("jdbc:postgresql://192.168.10.32:5432/omm?currentSchema=omm_system_config");
        source.setUsername("postgres");
        source.setPassword("A123456a");

        // 2.创建配置内容
        GlobalConfig config = new GlobalConfig();
        // 2.1设置路径与根包
        config.getPackageConfig().setSourceDir("D:/tmp").setBasePackage("com.cecbrain.omm.system");
        // 2.2设置表前缀和只生成哪些表
        config.setGenerateTable("system_config");
        // 2.3设置要生成内容
        config.setEntityGenerateEnable(true);
        config.setMapperGenerateEnable(true);
        config.setServiceGenerateEnable(true);
        config.setServiceImplGenerateEnable(true);
        config.setControllerGenerateEnable(true);
        // 2.4设置Javadoc
        config.getJavadocConfig().setAuthor("Mr.Lee").setSince("1.0.0");
        // 2.5设置父类
        config.setEntitySuperClass(BaseEntity.class);

        // 2.6设置模版路径&自定义模版
        // "/Users/striveonger/temp/flex/templates/controller-Solon.tpl"
        // "/Users/striveonger/temp/flex/templates/controller.tpl"
        // "/Users/striveonger/temp/flex/templates/entity.tpl"
        // "/Users/striveonger/temp/flex/templates/mapper.tpl"
        // "/Users/striveonger/temp/flex/templates/mapperXml.tpl"
        // "/Users/striveonger/temp/flex/templates/package-info.tpl"
        // "/Users/striveonger/temp/flex/templates/service.tpl"
        // "/Users/striveonger/temp/flex/templates/serviceImpl-Solon.tpl"
        // "/Users/striveonger/temp/flex/templates/serviceImpl.tpl"
        // "/Users/striveonger/temp/flex/templates/tableDef.tpl"
        // config.getTemplateConfig()
        //         .setEntity("/Users/striveonger/temp/flex/templates/entity.tpl")
        //         .setMapper("/Users/striveonger/temp/flex/templates/mapper.tpl")
        //         .setService("/Users/striveonger/temp/flex/templates/service.tpl")
        //         .setServiceImpl("/Users/striveonger/temp/flex/templates/serviceImpl.tpl")
        //         .setController("/Users/striveonger/temp/flex/templates/controller.tpl")
        // ;

        // 绕过 v1.7.1 bug
        config.setCustomConfig("k", "v");

        // 3.创建代码生成器
        Generator generator = new Generator(source, config);

        //生成代码
        generator.generate();
    }

}
