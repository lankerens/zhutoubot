package com.zhutou.bot.constant;

/**
 * @program: zhutoubot
 * @description: 常量类
 * @author: zhutoucyou
 * @create: 2021-09-27 00:20
 **/
public class ExampleConstant {
    public static final String token = "12345678:xxxxxxxxxxxxxxxxxxxxxx";
    public static final String Domain = "zhutou";
    public static final String chat_id = "-12345789";
    public static final String email = "admin@admin.com";
    public static final String psw = "123456";
    public static final String botName = "bongzhu_bot";



    public static final String loginDomain = "https://"+ Domain +"/api/v1/passport/auth/login";
    public static final String nodeInfoDomain = "https://"+ Domain +"/api/v1/admin/server/manage/getNodes";
    public static final String usageDomain = "https://" + Domain + "/api/v1/admin/stat/getServerLastRank";
}
