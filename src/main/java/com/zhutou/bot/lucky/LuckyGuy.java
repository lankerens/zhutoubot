package com.zhutou.bot.lucky;

import com.zhutou.bot.node.CheckNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
     *  签到的用户信息
     */
    static class CheckUser implements Serializable {
        LocalDate lastCheckInTime ;
        Integer score ;

        public CheckUser(){
            lastCheckInTime = LocalDate.now().plusDays(-30);
            score = 0;
        }

        public LocalDate getLastCheckInTime() {
            return lastCheckInTime;
        }

        public void setLastCheckInTime(LocalDate lastCheckInTime) {
            this.lastCheckInTime = lastCheckInTime;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "CheckUser{" +
                    "lastCheckInTime=" + lastCheckInTime +
                    ", score=" + score +
                    '}';
        }
    }
    /**
     *  存放积分情况
     */
    public static ConcurrentHashMap<Integer, CheckUser> scoreMap = new ConcurrentHashMap<>();
    /**
     *  是否已经初始化过一次了
     */
    public static Boolean canCheckNow = false;

    /**
     * 签到
     * @param chatUserId
     * @param userName
     * @return
     */
    // 1850530463
    public static String checkIn(Integer chatUserId, String userName){
        StringBuffer sb = new StringBuffer();
        CheckUser user = getUser(chatUserId);

        LocalDate now = LocalDate.now();
        if((user.getLastCheckInTime().plusDays(1).compareTo(now)) <= 0) {
            user.setScore(user.getScore() + 10);
            user.setLastCheckInTime(now);
            scoreMap.put(chatUserId, user) ;
            sb.append("签到成功, ").append(userName).append(" 获得了 10 积分 ! [开发版]");
        } else {
            sb.append(userName).append(" 大佬已经签过到了,再点机器人要罢工了..") ;
        }

        return sb.toString();
    }

    /**
     *  查询积分
     * @param chatUserId
     * @param userName
     * @return
     */
    public static String getUserScore(Integer chatUserId, String userName){
        StringBuffer sb = new StringBuffer();
        CheckUser user = getUser(chatUserId);
        return userName + " 您现在的积分：" + user.getScore();
    }


    /**
     *  检测节点中转在线情况
     * @param nodeName
     * @return
     */
    public static String checkNode(String nodeName){
        if(!canCheckNow) return "我忘记执行到哪儿了，请20分钟左右后再试..";
        String key = null;
        nodeName = nodeName.trim();
        for (String name: CheckNode.nodeYinShenMap.keySet()) {
            if(name.contains(nodeName)){
                key = name;
                break;
            }
        }
        if(key == null) return "你查询的节点名字没写对吧..[仅能查询中转节点]";
        String typeId = CheckNode.nodeYinShenMap.get(key);
        try {
            boolean b =  ping(CheckNode.nodeRecordMap.get(typeId).getHost());
            if(b) {
                return nodeName + " 估计是没挂，都ping通了";
            }
        } catch (Exception e) {
            System.out.println("checkNode 这里报错了竟然：" + e.getMessage());
        }
        return "我只能说 " + nodeName + " 大概率挂了";
    }

    /**
     * 测ping推荐使用
     * @param ipAddress
     * @return
     * @throws Exception
     */
    public static boolean ping(String ipAddress) throws Exception {
        return 0 == Runtime.getRuntime().exec("ping -c 1 "+ipAddress).waitFor();
    }

    /**
     *  转盘
     * @param chatUserId
     * @param userName
     * @return
     */
    public static String lucky(Integer chatUserId, String userName){
        StringBuffer sb = new StringBuffer();
        CheckUser user = getUser(chatUserId);
        // 30%  15%  10%  5%  1%
        int sub = 1 ;
        if(user.getScore() < sub) {
            sb.append("你的积分不足 ").append(sub).append(" 分呀,获取积分再来吧.");
        } else {
            int all = 60000, weightA = 18000, weightB = 9000, weightC = 6000, weightD = 3000, weightE = 600, weightF = 23400;
            int num = new Random().nextInt(60001);

            if(num <= weightA) {
                // 30 %
                sb.append(userName).append(" 🎉真棒, 积分+8[开发版]");
                user.setScore(user.getScore() + 8);
            } else if(num <= weightA + weightB) {
                sb.append(userName).append(" 🎉不错啊, 积分+80[开发版]");
                user.setScore(user.getScore() + 80);
            } else if(num <= weightA + weightB + weightC){
                sb.append(userName).append(" 🎉这也可以, 套̶餐̶流̶量̶+̶1̶G̶,̶私̶@̶z̶h̶u̶t̶o̶u̶c̶y̶o̶u̶领̶奖̶[不完善]");
            } else if(num <= weightA + weightB + weightC + weightD){
                sb.append(userName).append(" 🎉你这什么运气啊, 套̶餐̶天̶数̶+̶1̶,̶私̶@̶z̶h̶u̶t̶o̶u̶c̶y̶o̶u̶领̶奖̶[不完善]");
            } else if(num <= weightA + weightB + weightC + weightD + weightE){
                sb.append(userName).append(" 🎉我去,金色传说,这么难都被你抽到了，佩服，私@zhutoucyou领奖");
            } else {
                // 未中奖
                String[] lucky = {" 🎉恭喜你, 什么都没抽到", " 🎉抽不中啊，换个姿势吧", " 🎉再给你一次机会",
                        " 🎉恭喜你, 抽到了空气", " 🎉你这."};
                sb.append(userName).append(lucky[num % 5]);
            }
            user.setScore(user.getScore() - sub);
            scoreMap.put(chatUserId, user);
        }
        return " 消耗了"+ sub +"积分\r\n" + sb.toString();
    }

    public static void main(String[] args) {
        try {



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *  载入到内存
     */
    static {
//        File file = new File("/Users/apple/Documents/lankeren/JavaWorkPlace/zhutoubot/src/main/resources/static/checkUser.txt");
        File file = new File("/zhutou/checkUser.txt");
        if(file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                scoreMap = (ConcurrentHashMap<Integer, CheckUser>) ois.readObject();

                ois.close();
                System.out.println("载入积分文件成功～");
            } catch (IOException e) {
                System.out.println("载入文件的过程中出现了问题：" + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("载入文件的过程中读取文件出现了问题：" + e.getMessage());
            }
        }
    }

    /**
     *  写入到文件
     *  每两天 3.35 的时候持久化一次
     */
//    @Scheduled(fixedRate = 60000)
//    @Scheduled(cron = "0 35 3 1/2 * ?")
    public static String outputFiles(){
        boolean b = true;
        try {
//            File file = new File("/Users/apple/Documents/lankeren/JavaWorkPlace/zhutoubot/src/main/resources/static/checkUser.txt");
            File file = new File("/zhutou/checkUser.txt");
            if(!file.exists()) {
                file.createNewFile();
            }
            // false 覆盖 - true 追加
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false));
            oos.writeObject(scoreMap);

            // 清空缓冲区
            oos.flush();
            oos.close();
            System.out.println("时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "--------- 积分信息已经写入文件");
        } catch (IOException e) {
            System.out.println("写入文件的过程中出现了问题：" + e.getMessage());
            b = false;
        }
        return String.valueOf(b) + "\r\n" + scoreMap.toString();
    }

    /**
     * 增加积分
     * @param chatUserId
     * @param tail
     * @return
     */
    public static String addScore(Integer chatUserId, Integer tail){
        CheckUser user = scoreMap.get(chatUserId);
        user.setScore(user.getScore() + tail);
        scoreMap.put(chatUserId, user);
        return "加完了,目前：" + user.getScore();
    }

    /**
     *  获取user
     * @param chatUserId
     * @return
     */
    private static CheckUser getUser(Integer chatUserId){
        CheckUser user = null;
        if((user = scoreMap.get(chatUserId)) == null) {
            scoreMap.put(chatUserId, new CheckUser());
        }
        user = scoreMap.get(chatUserId);
        return user;
    }

}
