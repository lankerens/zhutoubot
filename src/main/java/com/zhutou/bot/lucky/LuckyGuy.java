package com.zhutou.bot.lucky;

import com.zhutou.bot.bean.CheckUser;
import com.zhutou.bot.constant.Constant;
import com.zhutou.bot.node.CheckNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: zhutoubot
 * @description: 签到抽奖类
 * @author: zhutoucyou
 * @create: 2021-09-26 13:10
 **/
@Component
public class LuckyGuy {

    /**
     * 存放积分情况
     */
    public static volatile ConcurrentHashMap<Integer, CheckUser> scoreMap = new ConcurrentHashMap<>();
    /**
     * 是否已经初始化过一次了
     */
    public static Boolean canCheckNow = false;



    public static void main(String[] args) {
        try {
           StringBuffer sb = new StringBuffer();

            System.out.println(Objects.equals("", sb.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 签到
     *
     * @param chatUserId
     * @param userName
     * @return
     */
    // 1850530463
    public static String checkIn(Integer chatUserId, String userName) {
        StringBuffer sb = new StringBuffer();
        CheckUser user = getUser(chatUserId);

        LocalDate now = LocalDate.now();

        if ((LocalDate.parse(user.getLastCheckInTime()).plusDays(1).compareTo(now)) <= 0) {
            user.setScore(user.getScore() + 10);
            user.setLastCheckInTime(now.toString());
            scoreMap.put(chatUserId, user);
            sb.append(userName).append("\r\n签到成功,").append("获得了 10 积分 ! [开发版]\r\n");
        } else {
            sb.append(userName).append("\r\n大佬已经签过到了,再点机器人要罢工了..⚠️\r\n");
        }
        return sb.toString();
    }

    /**
     * 库存
     *
     * @param chatUserId
     * @param userName
     * @return
     */
    public static String getUserScore(Integer chatUserId, String userName) {
        StringBuffer sb = new StringBuffer();
        CheckUser user = getUser(chatUserId);
        sb.append("🏠 库存查询").append("\r\n");
        sb.append("———————————————\r\n");
        sb.append("用户:").append(userName).append("\r\n");
        sb.append("获得的积分: ").append(user.getScore()).append(" F \r\n");
        sb.append("获得的流量: ").append(user.getFlow()).append(" MB \r\n");
        sb.append("获得的天数: ").append(user.getDays()).append(" 天 \r\n");
        return sb.toString();
    }


    /**
     * 抽奖转盘
     *
     * @param chatUserId
     * @param userName
     * @return
     */
    public static String lucky(Integer chatUserId, String userName, Long now) {
        CheckUser user = getUser(chatUserId);
        Long commandInTime = user.getCommandInTime();
        StringBuffer sb = new StringBuffer();

        if(now - commandInTime < Constant.MAX_SPEED) {
            // 没休息 1 s
            // 遣返
            sb.append("🤺速度太快了🤺 \r\n");
            return  sb.toString();
        } else {
            user.setCommandInTime(now);
        }

        int sub = 1;
        // 30%  15%  10%  5%  1%
        if (user.getScore() < sub) {
            sb.append(userName).append(" 大佬").append("\r\n")
                    .append("您的积分不足 ").append(sub).append(" 分呀,获取积分再来吧.😢 \r\n");
        } else {
            int all = 60000,
                    weightA = 13000, weightB = 11000, weightC = 7128, weightD = 5828, weightE = 3380, weightF = 2280, weightG = 8,
                    weightH = all - weightA - weightB - weightC - weightD - weightE - weightF - weightG;
            int num = new Random().nextInt(60001);

            sb.append(userName).append(" 您花费了").append(sub).append("积分\r\n");
            if (num <= weightA) {
                // 30 %
                sb.append("真棒, 积分+").append(num % 17).append(" ✌️[开发版] \r\n");
                user.setScore(user.getScore() + (num % 17));
            } else if (num <= weightA + weightB) {
                sb.append("不错啊, 积分-").append(num % 33).append(" 💣[开发版] \r\n");
                user.setScore(user.getScore() - (num % 33));
            } else if (num <= weightA + weightB + weightC) {
                sb.append("这也可以,  积分+").append(num % 70).append(" 🍦[开发版] \r\n");
                user.setScore(user.getScore() + (num % 70));
            } else if (num <= weightA + weightB + weightC + weightD) {
                sb.append("你这什么运气啊, 流量+").append(num % 108).append(" MB 🍰[开发版] \r\n");
                user.setFlow(user.getFlow() + (num % 108));
            } else if (num <= weightA + weightB + weightC + weightD + weightE) {
                sb.append("你这什么运气啊, 流量-").append(num % 70).append(" MB 💣[开发版] \r\n");
                user.setFlow(user.getFlow() - (num % 70));
            } else if (num <= weightA + weightB + weightC + weightD + weightE + weightF) {
                // double 保留3位小数
                DecimalFormat df = new DecimalFormat("0.00");
                String format = df.format((Math.random() * 0.6));
                sb.append("我去,金色 ！！ 普通，天数+").append(format).append(" 天 🍰[开发版] \r\n");
                user.setDays(user.getDays() + Double.parseDouble(format));
            } else if (num <= weightA + weightB + weightC + weightD + weightE + weightF + weightG) {
                // vcode = [5213600, 14076774]
                int vcode = (int) (((Math.random() * 17) + 10) * 521362);
                sb.append("我去,金色传说🥚[开发版]\r\n")
                        .append("此奖品只记录一次--请及时兑换\r\n")
                        .append("凭兑换码：").append(vcode).append(" 联系 '@zhutoucyou' 领奖 \r\n");
                user.setBigLuck(vcode);
            } else {
                // 未中奖
                String[] lucky = {"恭喜你, 什么都没抽到 🎉", "抽不中啊，换个姿势吧 🎉", "再给你一次机会🎉",
                        "恭喜你, 抽到了空气🎉", "你这.. 行不行的.🎉", "太可惜了.积分-1 🎉", "积分还没输完吗？🎉", "毅力不错，希望下把你能出金🎉"};
                sb.append(lucky[num % lucky.length + 1]).append("\r\n");
            }

            user.setScore(user.getScore() - sub);
            scoreMap.put(chatUserId, user);
        }

        return  sb.toString();
    }






    static DefaultAbsSender sender = new DefaultAbsSender(new DefaultBotOptions()) {
        @Override
        public String getBotToken() {
            return Constant.token;
        }
    };

    /**
     * 兑换奖品的
     * @param chatUserId
     * @param commandText
     * @return
     */
    public static String RedeemPrizes(Integer chatUserId, String commandText){
        StringBuffer sb = new StringBuffer();
        String[] split = commandText.split(" ");
        CheckUser checkUser = scoreMap.get(chatUserId);
        boolean sendToZhutou = false;
        try {
            if(split.length == 4 && Objects.equals(split[1], "m") ) {
                int i = Math.abs(Integer.parseInt(split[3]));
                Integer flow = checkUser.getFlow();
                if(flow >= i) {
                    checkUser.setFlow(flow - i);
                    sb.append("🚥已成功申请兑奖\r\n———————————————")
                            .append("\r\n用户：").append(chatUserId)
                            .append("\r\n账号：").append(split[2])
                            .append("\r\n兑换流量：").append(i).append(" MB")
                            .append("\r\n提交时间：").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .append("\r\n等待后台一天内加上.. 后续不会有通知,自行登陆官网确认.");
                    sendToZhutou = true;
                } else {
                    sb.append("🔪别闹, 你库存里的流量不够.");
                }
            } else if(split.length == 4 && Objects.equals(split[1], "d")){
                int i = Math.abs(Integer.parseInt(split[3]));
                Double days = checkUser.getDays();
                if(days >= i && i >= 1) {
                    checkUser.setDays(days - i);
                    sb.append("🚥已成功申请兑奖\r\n———————————————")
                            .append("\r\n用户：").append(chatUserId)
                            .append("\r\n账号：").append(split[2])
                            .append("\r\n兑换天数：").append(i).append(" 天")
                            .append("\r\n提交时间：").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .append("\r\n等待后台一天内加上.. 后续不会有通知,自行登陆官网确认.");
                    sendToZhutou = true;
                } else {
                    sb.append("🔪别闹, 你库存里的天数不够.");
                }
            } else {
                sb.append("流量兑换,只能整数,请注意格式如下：\r\n").append("例:/dui m 123456@gmail.com 100\r\n")
                        .append("天数兑换,只能整数, 请注意格式如下：\r\n").append("例:/dui d 123456@gmail.com 2\r\n")
                        .append("⚠️不要带少和带多空格. dui 前面务必加上斜杠/");
            }

        }catch (Exception e) {
            System.out.println("兑奖兑出错了: " + e.getMessage());
            sb = new StringBuffer();
            sb.append("确认你的数字都是整数不要搞怪, 如果你的没问题, 就是程序出错了, 那么再发送一遍. 还有问题请反馈 '@zhutoucyou' 谢谢🙏");
            sendToZhutou = false;
        }

        scoreMap.put(chatUserId, checkUser);
        if(sendToZhutou) {
            SendMessage message = new SendMessage()
                    .setChatId(Constant.MY_SELF_ID)
                    .setText(sb.toString());
            try {
                sender.execute(message);
            } catch (TelegramApiException e) {
                System.out.println("时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + "用户的中奖信息没法给我, 完犊子, 反正成功了，看看这里: " + commandText);
            }
        }

        return sb.toString();
    }



    /**
     * 检测节点中转在线情况
     *
     * @param nodeName
     * @return
     */
    public static String checkNode(String nodeName) {
        if (!canCheckNow) return "机器人刚启动,初始化过程20分钟, 20分钟左右后才开放查询..[机器人回复]";
        String key = null;
        nodeName = nodeName.trim();
        for (String name : CheckNode.nodeYinShenMap.keySet()) {
            if (name.contains(nodeName)) {
                key = name;
                break;
            }
        }
        if (key == null) return "你查询的节点名字没写对吧..[仅能查询中转节点] [机器人回复]";
        String typeId = CheckNode.nodeYinShenMap.get(key);
        try {
            boolean b = ping(CheckNode.nodeRecordMap.get(typeId).getHost());
            if (b) {
                return nodeName + " 估计是没挂❤，都ping通了 [机器人回复]";
            }
        } catch (Exception e) {
            System.out.println("checkNode 这里报错了竟然：" + e.getMessage());
        }
        return "我只能说 " + nodeName + " 大概率挂了 😅[机器人回复]";
    }

    /**
     * 测ping推荐使用
     *
     * @param ipAddress
     * @return
     * @throws Exception
     */
    public static boolean ping(String ipAddress) throws Exception {
        return 0 == Runtime.getRuntime().exec("ping -c 1 " + ipAddress).waitFor();
    }


    /**
     * 增加积分
     *
     * @param chatUserId
     * @param tail
     * @return
     */
    public static String addScore(Integer chatUserId, Integer tail) {
        CheckUser user = scoreMap.get(chatUserId);
        user.setScore(user.getScore() + tail);
        scoreMap.put(chatUserId, user);
        return "加完了,目前：" + user.getScore();
    }

    /**
     * 获取user
     *
     * @param chatUserId
     * @return
     */
    private static CheckUser getUser(Integer chatUserId) {
        CheckUser user = null;
        if ((user = scoreMap.get(chatUserId)) == null) {
            scoreMap.put(chatUserId, new CheckUser(chatUserId));
        }
        user = scoreMap.get(chatUserId);
        return user;
    }




    /**
     *  载入到内存
     */
//    static {
////        File file = new File("/Users/apple/Documents/lankeren/JavaWorkPlace/zhutoubot/src/main/resources/static/checkUser.txt");
//        File file = new File("/zhutou/checkUser.txt");
//        if (file.exists()) {
//            try {
//                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
//                scoreMap = (ConcurrentHashMap<Integer, CheckUser>) ois.readObject();
//
//                ois.close();
//                System.out.println("载入积分文件成功～");
//            } catch (IOException e) {
//                System.out.println("载入文件的过程中出现了问题：" + e.getMessage());
//            } catch (ClassNotFoundException e) {
//                System.out.println("载入文件的过程中读取文件出现了问题：" + e.getMessage());
//            }
//        }
//    }

    /**
     * 写入到文件
     * 每两天 3.35 的时候持久化一次
     */
//    @Scheduled(fixedRate = 60000)
//    @Scheduled(cron = "0 35 3 1/2 * ?")
    public static String outputFiles() {
        boolean b = true;
        try {
//            File file = new File("/Users/apple/Documents/lankeren/JavaWorkPlace/zhutoubot/src/main/resources/static/checkUser.txt");
            File file = new File("/zhutou/checkUser.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            // false 覆盖 - true 追加
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false));
            oos.writeObject(scoreMap);

            // 清空缓冲区
            oos.flush();
            oos.close();
            System.out.println("时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "--------- 积分信息已经[ 手动触发 ]写入文件");
        } catch (IOException e) {
            System.out.println("写入文件的过程中出现了问题：" + e.getMessage());
            b = false;
        }
        return String.valueOf(b) + "\r\n" + scoreMap.toString();
    }

}
