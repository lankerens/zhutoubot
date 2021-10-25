package com.zhutou.bot.utils;

import com.zhutou.bot.bean.CheckUser;
import com.zhutou.bot.lucky.LuckyGuy;
import com.zhutou.bot.mapper.CheckUserMapper;
import com.zhutou.bot.tgbot.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

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


    @Value("${botconstant.Authorization}")
    private String Authorization;
    private Map<String, String> map = new HashMap<String, String>() {
        {
            put("AA26B06E4CFB53223050313ED84EE0C2","zhutou");
            put("BC743799882F93F7506CB4DAD62AE98B","桃子");
            put("C97C34E862EAF6CFE031ED5FF19F8FF1","@Line9110");
            put("DB14A8D97B6AAA4EE58C0C2B66CA13A2","授权2");
            put("F9C299DD51ED8726AA8F01A44C29462D","授权3");
            put("26A57E0515CF28827D0DDE5625BBEA3B","授权4");
            put("80B8ED4F429F1752AC3352283D7B91AE","授权5");
            put("21E44AA7591B9C7252AFBED7643B4721","授权6");
        }
    };

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(map.get(Authorization) != null) {
            startBot();
            loadAll();
        } else {
            System.out.println("你搞错了吧老板，我这没你这授权码..");
        }
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
