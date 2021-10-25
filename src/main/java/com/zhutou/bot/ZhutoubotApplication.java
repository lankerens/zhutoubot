package com.zhutou.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 主类
 * @author zhutou
 */
@SpringBootApplication
@EnableScheduling
public class ZhutoubotApplication {


    public static void main(String[] args) {
        SpringApplication.run(ZhutoubotApplication.class, args);

    }

}
