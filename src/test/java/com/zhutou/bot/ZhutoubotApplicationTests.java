package com.zhutou.bot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZhutoubotApplicationTests {




    @Test
    void contextLoads() {


    }

}


//两种方式构建post请求体，携带对应的参数
//        RequestBody requestBody = new FormBody.Builder().add("email", email).add("password", "baipiao123").build();
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody requestBody = RequestBody.create(new Message(text).toString(), JSON);



//        StringBuffer transitSb = new StringBuffer("🔍以下中转地址发生了改变, 请留意是否需要更新订阅 \r\n");
//        // 掉线
//        StringBuffer offlineSb = new StringBuffer("🔍以下节点被妖怪拔网线了, 马上抢救 \r\n");
//        // 恢复
//        StringBuffer onlineSb = new StringBuffer("🔍以下节点恢复了, 下班 ! \r\n");
//        System.out.println(transitSb.length());
//        System.out.println( offlineSb.length());
//        System.out.println(onlineSb.length());
//        System.out.println(transitSb.length() + offlineSb.length() + onlineSb.length());