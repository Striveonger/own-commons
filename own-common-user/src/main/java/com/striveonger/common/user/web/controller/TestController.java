package com.striveonger.common.user.web.controller;

import com.striveonger.common.core.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-03 23:16
 */
@Tag(name = "Test")
@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    /**
     * hi
     */
    @GetMapping("/hi")
    public Result hello(String name) {
        log.info("Hi, {}~", name);
        return Result.success(String.format("Hi, %s~", name));
    }

}