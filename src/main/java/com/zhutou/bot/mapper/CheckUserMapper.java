package com.zhutou.bot.mapper;

import com.zhutou.bot.bean.CheckUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @program: zhutoubot
 * @description: 签到用户持久化
 * @author: zhutoucyou
 * @create: 2021-10-08 20:43
 **/
@Mapper
public interface CheckUserMapper {

    /**
     * 查询出所有的用户
     * @return
     */
    @Select("select id, user_id as userId, score, lastCheckInTime, flow, days, big_luck from checkuser_info")
    List<CheckUser> loadAll();


    /**
     * 加积分
     * @param userId
     * @param score
     * @return
     */
    @Update("UPDATE checkuser_info " +
            "SET score = #{score} " +
            "WHERE user_id = #{userId}")
    Integer addScore(Integer userId, Integer score);


    /**
     * 判断该用户是否存在
     * @param userId
     * @return
     */
    @Select("select count(*) from checkuser_info where user_id = #{userId}")
    Integer isUserExist(Integer userId);


    /**
     * 保存用户积分数据
     * @param checkUser
     * @return
     */
    @Insert("INSERT INTO checkuser_info (user_id, score, lastCheckInTime, flow, days, big_luck) " +
            "VALUES (#{userId}, #{score}, #{lastCheckInTime}, #{flow}, #{days}, #{bigLuck})")
    Integer saveUserInfoMap(CheckUser checkUser);


    /**
     *  更新用户积分数据
     * @param checkUser
     * @return
     */
    @Update("UPDATE checkuser_info " +
            "SET score = #{score}, lastCheckInTime = #{lastCheckInTime}, flow = #{flow}, days = #{days}, big_luck = #{bigLuck} " +
            "WHERE user_id = #{userId}")
    Integer updateUserInfoMap(CheckUser checkUser);

}
