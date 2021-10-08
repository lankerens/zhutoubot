package com.zhutou.bot;


import com.zhutou.bot.constant.Constant;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

public class botTest extends TelegramLongPollingBot {

    public static void main(String[] args) {
        // 初始化Api上下文
        ApiContextInitializer.init();
        // 实例化Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            // 注册我们的机器人
            botsApi.registerBot(new botTest());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = null;

            if(update.getMessage().isSuperGroupMessage() && Objects.equals(update.getMessage().getText(), "/test@bongzhu_bot")) {
                message = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(update.getMessage().getChatId()+"");
            }
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getBotUsername() {
        return Constant.botName;
    }

    @Override
    public String getBotToken() {
        return Constant.token;
    }


}
