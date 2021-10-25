package com.zhutou.bot.utils;

import com.zhutou.bot.mapper.CheckUserMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: zhutoubot
 * @description: 解决SpringBoot中自己new出来的对象不能自动注入对象和属性的问题
 *
 * 使用方法: private AreaInvadeMapper areaInvadeMapper = GetBeanUtil.getBean(AreaInvadeMapper.class);
 *
 * @author: zhutoucyou
 * @create: 2021-10-09 17:22
 **/
@Getter
@Component
public class GetBeanUtil {

    @Autowired
    private CheckUserMapper checkUserMapper;

    public static GetBeanUtil getBeanUtil;

    @PostConstruct
    public void init() {
        getBeanUtil = this;
        getBeanUtil.checkUserMapper = this.checkUserMapper;
    }

}
