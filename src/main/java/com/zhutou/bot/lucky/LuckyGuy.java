package com.zhutou.bot.lucky;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: zhutoubot
 * @description: 抽奖类
 * @author: zhutoucyou
 * @create: 2021-09-26 13:10
 **/
public class LuckyGuy {

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        if(LocalTime.now().getHour() == 0 && LocalTime.now().getMinute() < 30) {
            System.out.println("xx");
        } else {
            System.out.println("yyyy");
        }
    }

}
