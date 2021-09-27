package com.zhutou.bot;

import com.zhutou.bot.node.CheckNode;
import com.zhutou.bot.tgbot.BotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
@EnableScheduling
public class ZhutoubotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhutoubotApplication.class, args);

        // 初始化Api上下文
        ApiContextInitializer.init();
        // 实例化Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            // 注册我们的机器人
            botsApi.registerBot(new BotConfig());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
