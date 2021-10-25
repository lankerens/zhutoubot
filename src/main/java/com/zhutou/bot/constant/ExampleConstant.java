package com.zhutou.bot.constant;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @program: zhutoubot
 * @description: 获得配置文件的信息
 * @author: zhutoucyou
 * @create: 2021-09-27 00:20
 **/
//@Component - 需要这个注解
public class ExampleConstant implements EnvironmentAware {

    private static Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }
    public static String getString(String key) {
        return env.getProperty(key);
    }


    // 抽奖的速率
    public static final Integer MAX_SPEED = 1400;
}
