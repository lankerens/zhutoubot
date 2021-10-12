package com.zhutou.bot.utils;

import com.zhutou.bot.bean.CheckUser;
import com.zhutou.bot.lucky.LuckyGuy;
import com.zhutou.bot.mapper.CheckUserMapper;
import com.zhutou.bot.tgbot.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * @program: zhutoubot
 * @description: 开机自启的机器人注册
 * @author: zhutoucyou
 * @create: 2021-10-08 21:52
 **/
@Component
public class BotTask implements ApplicationRunner {

    @Autowired
    private CheckUserMapper checkUserMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        startBot();
        loadAll();
    }

    public void startBot(){
        // 初始化Api上下文
        ApiContextInitializer.init();
        // 实例化Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            // 注册我们的机器人
            botsApi.registerBot(new BotConfig());
            System.out.println("注册机器人完毕，准备载入数据..");
        } catch (TelegramApiException e) {
            System.out.println("注册机器人出现错误: " + e.getMessage());
        }
    }

    /**
     *  载入积分数据
     */
    public void loadAll(){
        try {
            List<CheckUser> usersList = checkUserMapper.loadAll();
            for (CheckUser user: usersList) {
                LuckyGuy.scoreMap.put(user.getUserId(), user);
            }
            System.out.println("载入积分数据 成功");
        }catch (Exception e) {
            System.out.println("载入积分数据 失败了： " + e.getMessage());
        }
    }

}
