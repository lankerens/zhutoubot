package com.zhutou.bot.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @program: zhutoubot
 * @description: 签到玩家类
 * @author: zhutoucyou
 * @create: 2021-10-08 20:45
 **/
@Setter
@Getter
@ToString
@Component
public class CheckUser implements Serializable {
    Integer id;
    /**
     * 账号id
     */
    Integer userId;
    /**
     * 最近的一次lucky命令的时间 - 控制抽奖速率
     */
    Long commandInTime;
    /**
     * 最近一次签到的时间 （一天只能签一次）
     */
    String lastCheckInTime;
    /**
     * 获得的积分和
     */
    Integer score;
    /**
     * 抽到的流量和
     */
    Integer flow;
    /**
     * 抽到的天数和
     */
    Double days;
    /**
     *  终极大奖的兑换码
     */
    Integer bigLuck;

    public CheckUser() {
        lastCheckInTime = LocalDate.now().plusDays(-30).toString();
        commandInTime = LocalDateTime.now().plusNanos(-10000).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        score = 0;
        flow = 0;
        days = 0.00d;
        bigLuck = 0;
    }

    public CheckUser(Integer userId) {
        lastCheckInTime = LocalDate.now().plusDays(-30).toString();
        commandInTime = LocalDateTime.now().plusNanos(-10000).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        score = 0;
        flow = 0;
        days = 0.00d;
        bigLuck = 0;
        this.userId = userId;
    }

}
