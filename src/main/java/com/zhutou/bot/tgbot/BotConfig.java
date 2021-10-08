package com.zhutou.bot.tgbot;

import com.zhutou.bot.constant.Constant;
import com.zhutou.bot.lucky.LuckyGuy;
import com.zhutou.bot.node.CountNode;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.util.Objects;

/**
 *  机器人命令
 */
public class BotConfig extends TelegramLongPollingBot {

    public CountNode cn = new CountNode();


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = null;
            boolean isSuperGroupMessage = update.getMessage().isSuperGroupMessage();
            String commandText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            // User{id=1850530463, firstName='现在只想躺平的zhutou', isBot=false, lastName='null', userName='zhutoucyou', languageCode='zh-hans'}
            User groupUser = update.getMessage().getFrom();
            System.out.println("用户发送命令" + commandText + "  现在时间：" + LocalTime.now().getHour() + ": " + LocalTime.now().getMinute());


            /**
             *  help 帮助命令
             */
            if(isSuperGroupMessage && commandText.contains("/help")) {
                message = new SendMessage()
                        .setChatId(chatId)
                        .setText("/nodestatus - 查看节点情况 \r\n" +
                                "/usage - 昨天消耗的总流量 \r\n" +
                                "/checkin - 签到[可私聊] \r\n" +
                                "/myscore - 我的积分[可私聊] \r\n" +
                                "/lucky - 转盘[可私聊] \r\n" +
                                "/checknode  - 检测中转节点在线情况 \r\n" +
                                "/ping  - 测通一下你觉得可能挂掉的中转..\r\n" +
                                "");
            } else if(isSuperGroupMessage && commandText.contains("/nodestatus")){
                /**
                 *  nodestatus 节点情况命令
                 */
                try {
                    String content = "出错了.";
                    if(LocalTime.now().getHour() == 0 && LocalTime.now().getMinute() < 30) content = "抱歉，机器人休息时间0:00 - 0:30";
                    else content = cn.countNode();
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(content);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if(isSuperGroupMessage && commandText.contains("/usage")){
                /**
                 *  usage 流量消耗命令
                 */
                try {
                    String content = "出错了.";
                    if(LocalTime.now().getHour() == 0 && LocalTime.now().getMinute() < 30) content = "抱歉，机器人休息时间0:00 - 0:30";
                    else content = cn.usage();
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(content);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if( commandText.contains("/checkin") ){
                /**
                 *  签到
                 */
                String content = "出错了.";
                try {
                    content = LuckyGuy.checkIn(groupUser.getId(), groupUser.getFirstName());
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(content);
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if( commandText.contains("/myscore") ){
                /**
                 *  查询我的积分
                 */
                String content = "出错了.";
                try {
                    content = LuckyGuy.getUserScore(groupUser.getId(), groupUser.getFirstName());
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(content);
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else if( commandText.contains("/lucky") ){
                /**
                 *  转盘抽奖
                 */
                String content = "出错了.";
                try {
                    content = LuckyGuy.lucky(groupUser.getId(), groupUser.getFirstName());
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(content);
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if(isSuperGroupMessage && commandText.contains("/checknode")){
                /**
                 *  检测节点中转在线情况
                 */
                String content = "出错了.";
                String nodeName = commandText.substring(10);
                if(StringUtils.isBlank(nodeName)) content = "请加上中转的节点名称-例如 checknode 香港01";
                else if(LocalTime.now().getHour() == 0 && LocalTime.now().getMinute() < 30) content = "抱歉，机器人休息时间0:00 - 0:30";
                else content = LuckyGuy.checkNode(nodeName);
                try {
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(content);
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if(isSuperGroupMessage && commandText.contains("/ping")){
                /**
                 *  ping
                 */
                String ipAddress = commandText.substring(5);
                String content = "出错了.";
                try {
                    if(StringUtils.isBlank(ipAddress)) content = "你好像没有加上要ping的地址-例如 ping www.baidu.com";
                    else content = LuckyGuy.ping(ipAddress) ? (ipAddress + " ping通了") : (ipAddress + " ping不通呀") ;
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(content);
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if(update.getMessage().isUserMessage() && (1850530463 == chatId) && Objects.equals(commandText, "/opf")){
                /**
                 *  持久化
                 */
                try {
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(LuckyGuy.outputFiles());
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if(update.getMessage().isUserMessage() && (1850530463 == chatId) && Objects.equals(commandText, "/addscore")){
                /**
                 *  增加积分
                 */
                try {
                    String[] split = commandText.split(" ");
                    message = new SendMessage()
                            .setChatId(chatId)
                            .setText(LuckyGuy.addScore(Integer.valueOf(split[1]), Integer.valueOf(split[2])));
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if(update.getMessage().isUserMessage()){
                /**
                 *  私人消息
                 */
                message = new SendMessage()
                        .setChatId(chatId)
                        .setText("抱歉暂不支持其他的不支持私人消息");
            } else {
                String[] answer = {"大佬，惹不起..", "发的什么玩意,bongzhu_bot没有这个指令", "每次跟你打王者 我总希望对面有人来偷水晶 你会说我们家被偷了 这时我就很开心 原来我可以跟你有个家。",
                        "我真的没有偷懒.", "今天她终于叫了我的名字 虽然叫错了，但是没关系 我马上就去改名","他朋友圈屏蔽我了，我陷入了沉思，大都是屏蔽家人，原来他把我当做她家人了，他好细节啊，我更爱他了",
                        "今天晚上有点冷，本来以为街上没有人，结果刚刚偷电瓶的时候被抓，本来想反抗，结果警察说了一句老实点别动，我立刻就放弃了抵抗，因为我记得你说过你喜欢老实人。", "今天又是日收入0..", "可恶", "发生甚么事了.？", "叫你不要点了, 你看程序出错了吧",
                        "我今天坐在窗边给你发了99条消息，你终于回我了，你说“发你妈啊”。我一下就哭了，原来努力真的是有用的，你已经开始考虑见我妈妈了，你也一定是喜欢我的吧。", "你的头像是一个女孩子左手边牵着一条秋田犬，犬=狗，而我是一条舔狗。是不是代表你的小手在牵着我呢？", "刚刚程序执行到哪来着..", "拜托，你很弱诶",
                        "阿伟, 不要打电动啦，去学会习好不","你终于喊我双排了 让我拿胜率最高的英雄，我一般都不玩安琪拉的，因为胜率太高怕掉，可是为了你我还是选了，你好像很开心，给我发了句fw，我懂了，你是想夸夸我，说我法王，你真好，我越来越喜欢你了",
                        "你说憧憬过以后的生活，可你也没有说，憧憬过离开我的生活","别的妹妹叫你打游戏 你让人家语音给你发了句哥哥 你就陪她打一天 我叫你打游戏 你回了我一句 70/h","你回他消息吧！他开心了你就会理我了。","我刚刚鼓起勇气给他发了句晚安，他回了一句，“再发tm拉黑你”我赶紧说，“求你别，我不说话了……”然后退了微信界面，壁纸是我ps的一张我俩的合照。",
                        "今天考了一天的试，很疲惫但是看见你的朋友圈，原来你去吃了肯德基，真想和你一起去吃一次。","我给你发了99条消息你终于肯回我了你说“有病，别烦我”我一下子就哭了，原来努力真的有用，你已经开始在意我的身体健康了，有病当然要去看医生的，你为了让我去医院煞费苦心。只有我能看穿你故作高冷又倔强的心，其实你也是很关心我的。"
                        };
                int index = (int) (Math.random() * 1000);
                message = new SendMessage()
                        .setChatId(chatId)
                        .setText(answer[index%(answer.length + 1)]);
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
