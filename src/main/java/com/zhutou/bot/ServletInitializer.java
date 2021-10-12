package com.zhutou.bot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @program: zhutoubot
 * @description: 初始化
 * @author: zhutoucyou
 * @create: 2021-10-08 20:58
 **/
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ZhutoubotApplication.class);
    }

}
