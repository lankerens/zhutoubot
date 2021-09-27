package com.zhutou.bot.tgbot;

import com.zhutou.bot.constant.Constant;
import com.zhutou.bot.node.CountNode;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.util.Objects;

/**
 *  机器人命令
 */
public class BotConfig  extends TelegramLongPollingBot{

    public CountNode cn = new CountNode();


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = null;
            boolean isSuperGroupMessage = update.getMessage().isSuperGroupMessage();
            String commandText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            System.out.println("用户发送命令" + commandText + "现在时间：" + LocalTime.now().getHour() + ": " + LocalTime.now().getMinute());


            /**
             *  help 帮助命令
             */
            if(isSuperGroupMessage && (Objects.equals(commandText, "/help@bongzhu_bot") || (Objects.equals(commandText, "/help @bongzhu_bot")))) {
                message = new SendMessage()
                        .setChatId(chatId)
                        .setText("/nodestatus - 查看节点情况 \r\n" +
                                "/usage - 昨天消耗的总流量 \r\n" +
                                "");
            } else if(isSuperGroupMessage && (Objects.equals(commandText, "/nodestatus@bongzhu_bot") || Objects.equals(commandText, "/nodestatus"))){
                /**
                 *  nodestatus 节点情况命令
                 */
                try {
                    String content = "";
                    if(LocalTime.now().getHour() == 0 && LocalTime.now().getMinute() < 30) content = "抱歉，机器人休息时间0:00 - 0:30";
                    else content = cn.countNode();
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(content);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if(isSuperGroupMessage && (Objects.equals(commandText, "/usage@bongzhu_bot") || Objects.equals(commandText, "/usage"))){
                /**
                 *  usage 流量消耗命令
                 */
                try {
                    String content = "";
                    if(LocalTime.now().getHour() == 0 && LocalTime.now().getMinute() < 30) content = "抱歉，机器人休息时间0:00 - 0:30";
                    else content = cn.usage();
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(content);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if(update.getMessage().isUserMessage()){
                /**
                 *  私人消息
                 */
                message = new SendMessage()
                        .setChatId(chatId)
                        .setText("抱歉不支持私人消息");
            } else {
                String[] answer = {"大佬，惹不起..", "发的什么玩意,bongzhu_bot没有这个指令", "我在很认真的工作的..", "我真的没有偷懒."};
                int index = (int) (Math.random() * 4);
                message = new SendMessage()
                        .setChatId(chatId)
                        .setText(answer[index]);
            }

            try {
                if(message != null)
                    execute(message);
            } catch (TelegramApiException e) {
                System.out.println("BotConfig这里出现错误：" + e.getMessage());
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
