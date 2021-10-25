package com.zhutou.bot.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: zhutoubot
 * @description: 描述
 * @author: zhutoucyou
 * @create: 2021-09-27 19:49
 **/
@Component
public class Constant {


    /**
     *
     */
    public static  String token ;
    public static  String Domain ;
    public static  String chat_id ;
    public static  String email;
    public static  String password;
    public static  String botName;
    public static  String MY_SELF_ID;
    public static  Long MY_SELF_ID_LONG;
    public static  Integer MY_SELF_ID_INT;
    public static  String CONTACT_ME;

    @Value("${botconstant.token}")
    private String a ;
    @Value("${botconstant.domain}")
    private String b ;
    @Value("${botconstant.chat_id}")
    private String c ;
    @Value("${botconstant.email}")
    private String d;
    @Value("${botconstant.password}")
    private String e;
    @Value("${botconstant.botName}")
    private String f;
    @Value("${botconstant.MY_SELF_ID}")
    private String g;
    @Value("${botconstant.MY_SELF_ID}")
    private Long h;
    @Value("${botconstant.MY_SELF_ID}")
    private Integer i;
    @Value("${botconstant.CONTACT_ME}")
    private String j;


    public static String loginDomain ;
    public static String nodeInfoDomain ;
    public static String usageDomain ;
    // 抽奖的速率
    public static final Integer MAX_SPEED = 1460;


    @PostConstruct
    public void init(){
        Constant.token = a;
        Constant.Domain = b;
        Constant.chat_id = c;
        Constant.email = d;
        Constant.password = e;
        Constant.botName = f;
        Constant.MY_SELF_ID = g;
        Constant.MY_SELF_ID_LONG = h;
        Constant.MY_SELF_ID_INT = i;
        Constant.CONTACT_ME = j;
        Constant.loginDomain = "https://"+ b +"/api/v1/passport/auth/login";
        Constant.nodeInfoDomain = "https://"+ b +"/api/v1/admin/server/manage/getNodes";
        Constant.usageDomain = "https://" + b + "/api/v1/admin/stat/getServerLastRank";
    }


}
