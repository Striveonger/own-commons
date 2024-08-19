package com.striveonger.common.leaf.knife4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-14 10:55
 */
@Configuration
public class OpenAPIConfig {
    private final Logger log = LoggerFactory.getLogger(OpenAPIConfig.class);

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("own-commons-web")
                        .version("V1.0.0")
                        .description("own-commons-web"));
    }

}
