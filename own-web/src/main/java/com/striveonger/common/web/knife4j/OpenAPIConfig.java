package com.striveonger.common.web.knife4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * @author Mr.Lee
 * @since 2024-07-14 10:55
 */
@Configuration
public class OpenAPIConfig {
    private final Logger log = LoggerFactory.getLogger(OpenAPIConfig.class);

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("own-web")
                        .version("1.0.0")
                        .description("own-web"));
    }
}
