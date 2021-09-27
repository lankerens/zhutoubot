package com.zhutou.bot.node;

import com.alibaba.fastjson.JSONObject;
import com.zhutou.bot.bean.Node;
import com.zhutou.bot.bean.Usage;
import com.zhutou.bot.constant.Constant;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 *  检测节点掉线或者恢复
 */
@Component
public class CheckNode{
    /**
     * 登陆 cookie 是否还有效 - 两个小时有效时间
     */
    public static volatile Boolean flag = true;
    /**
     *  登陆失败次数
     */
    public static volatile Integer falseCount = 0;
    /**
     *  定时任务跳过
     */
    public static volatile Boolean task = false;


    /**
     *  cookie 值
     */
    public static String v2board_session = "";
    public static ConcurrentHashMap<String, String> offLineMap = new ConcurrentHashMap<>();
    public static Map<String, Node> nodeRecordMap = new HashMap<>();



    public static void main(String[] args) {

    }

    /**
     *  线程执行
     */
    // 1点-23点 每15分钟执行一次
    @Scheduled(cron = "0 0/15 1-23 * * ?")
    public void task() {
        if(task) {
            System.out.println("时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "失败次数过多在休眠中, 直接return");
            return ;
        }
        CheckNode nodeInfo = new CheckNode();
        try {
            if(falseCount > 8) {
                System.out.println("falseCount > 8 失败次数太多了 开始睡觉97分钟. ");
                task = true;
                // 97分钟
                Thread.sleep(60000 * 97);
                task = false;
                flag = true;
            }
            String content = nodeInfo.getNodes(false);
            if(!Objects.equals("", content)) nodeInfo.sendMessage(content);
            System.out.println("时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "---------------定时任务完整执行完一次[自动检测节点情况]--------------");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *  登陆
     * @throws Exception
     */
    public void login() throws Exception{
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder().add("email", Constant.email).add("password", Constant.psw).build();
        Request request = new Request
                .Builder()
                .url(Constant.loginDomain)
                .post(requestBody)
                .build();

        //执行请求操作
        try {
            Response response = client.newCall(request).execute();
            System.out.println("登陆请求返回响应码：" + response.code());
            if(response.isSuccessful()){
                System.out.println("登录成功：flag = false");
                flag = false;
                falseCount = 0;
                List<String> cookies = response.headers().values("Set-Cookie");
                v2board_session = cookies.get(0).substring(0, cookies.get(0).indexOf(";"));
            } else {
                falseCount++;
            }

            response.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *  自动检测节点情况
     * @return
     * @throws Exception
     */
    public String getNodes(boolean isCommand) throws Exception{
        OkHttpClient client = new OkHttpClient();
        CheckNode nodeInfo = new CheckNode();
        // 中转
        StringBuffer transitSb = new StringBuffer("🔍以下中转地址发生了改变, 请留意是否需要更新订阅 \r\n");
        // 掉线
        StringBuffer offlineSb = new StringBuffer("🔍以下节点被妖怪拔网线了, 马上抢救 \r\n");
        // 恢复
        StringBuffer onlineSb = new StringBuffer("🔍以下节点恢复了, 下班 ! \r\n");
        // 情况
        StringBuffer listSb = new StringBuffer();

        do {
            Map<String, Node> parentsMap = new HashMap<>();

            if(flag){ nodeInfo.login(); }
            Request request = new Request
                    .Builder()
                    .url(Constant.nodeInfoDomain)
                    .addHeader("cookie", v2board_session)
                    .build();
            //执行请求操作
            try {
                Response response = client.newCall(request).execute();

                if(response.isSuccessful()){
                    int offline = 0, online = 0, total = 0;
                    String res = response.body().string();
                    List<Node> nodes = JSONObject.parseArray(res.substring(8, res.lastIndexOf("]") + 1), Node.class);
                    for(Node n : nodes) {
                        // 跳过
                        if(Objects.equals("127.0.0.1", n.getHost())) continue ;

                        if(n.getShow() != 0) {
                            total++;
                            if(n.getParent_id() != null) {
                                // 展示的中转进来这个map
                                parentsMap.put(n.getType() + n.getParent_id(), n);

                                // 判断中转是否变化
                                if(nodeRecordMap.size() > 0) {
                                    Node zhongNodeRecord = nodeRecordMap.get(n.getType() + n.getId());
                                    if(zhongNodeRecord != null) {
                                        if(!zhongNodeRecord.getHost().equals(n.getHost()) || !zhongNodeRecord.getPort().equals(n.getPort())) {
                                            // 中转地址或者端口发生改变了
                                            transitSb.append(n.getName()).append("\r\n");
                                        }
                                    } else {
                                        // 新增中转
                                        transitSb.append(n.getName()).append("\r\n");
                                    }
                                    nodeRecordMap.put(n.getType() + n.getId(), n);
                                }
                            } else {
                                // 展示的直连
                                if(n.getAvailable_status() == 0 && offLineMap.get(n.getType() + n.getId()) == null) {
                                    offlineSb.append(n.getName()).append("\r\n");
                                    offLineMap.put(n.getType() + n.getId(), n.getName());
                                } else if(n.getAvailable_status() != 0 && offLineMap.get(n.getType() + n.getId()) != null) {
                                    onlineSb.append(n.getName()).append("\r\n");
                                    offLineMap.remove(n.getType() + n.getId());
                                }

                            }
                        } else {
                            // 中转的节点
                            Node node = parentsMap.get(n.getType() + n.getId());
                            //在下面
                            if(node != null && n.getAvailable_status() == 0 && offLineMap.get(n.getType() + n.getId()) == null ) {
                                offlineSb.append(node.getName()).append("\r\n");
                                // 直连的
                                offLineMap.put(n.getType() + n.getId(), node.getName());
                            } else if(node != null && node.getAvailable_status() == 0 && offLineMap.get(node.getType() + node.getId()) == null ){
                                offlineSb.append(node.getName()).append(" 中转机坏了 \r\n");
                                // 中转的
                                offLineMap.put(node.getType() + node.getId(), node.getName());
                            } else if(node != null && n.getAvailable_status() != 0 && offLineMap.get(n.getType() + n.getId()) != null ){
                                // 直连的恢复了
                                onlineSb.append(node.getName()).append("\r\n");
                                offLineMap.remove(n.getType() + n.getId());
                            } else if(node != null && node.getAvailable_status() != 0 && offLineMap.get(node.getType() + node.getId()) != null) {
                                // 中转的恢复了
                                onlineSb.append(node.getName()).append(" 中转机好了 \r\n");
                                offLineMap.remove(node.getType() + node.getId());
                            } else {
                                //TODO
                            }
                        }
                    }
                    if(nodeRecordMap.size() == 0) nodeRecordMap = parentsMap;
                    offline = offLineMap.size();
                    online = total - offline;
                    if(isCommand) {
                        listSb.append("\r\n")
                                .append("[ 这里没有显示掉线的就是没有掉线，请先更新订阅 ]").append("\r\n")
                                .append("总节点数：").append(total).append("\r\n")
                                .append("在线节点：").append(online).append("\r\n")
                                .append("离线节点：").append(offline).append("\r\n");

                        listSb.append("抢救中的节点：");
                        for (String name : offLineMap.values())
                            listSb.append(name).append(", ");
                    }
                } else {
//                    if(response.code() == 403)
                    flag = true;
                    System.out.println("cookie 过期了，马上要重新登陆 - flag被设置为了 " + flag);
                    if(falseCount > 8) {
                        if(isCommand) listSb.append("大佬，惹不起，现在网站出问题了.. \r\n");
                        // 休息两个小时

                        flag = false ;
                    }
                }

                response.close();
            } catch (Exception e) {
                System.out.println("getNodes这里发生错误" + e.getMessage());
            }

        }while (flag);

        if(isCommand) return listSb.toString();
        String res = transitSb.toString() + offlineSb.toString() + onlineSb.toString();
        if(res.length() < 72) return "";
        return res;
    }


    /**
     * 流量使用情况
     * @return
     * @throws Exception
     */
    public String usage() throws Exception {
        OkHttpClient client = new OkHttpClient();
        CheckNode nodeInfo = new CheckNode();
        StringBuffer sb = new StringBuffer();
        // double 保留3位小数
        DecimalFormat df = new DecimalFormat("0.000");

        do {
            if(flag){ nodeInfo.login(); }
            Request request = new Request
                    .Builder()
                    .url(Constant.usageDomain)
                    .addHeader("cookie", v2board_session)
                    .build();
            //执行请求操作
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    List<Usage> usages = JSONObject.parseArray(res.substring(res.indexOf("["), res.lastIndexOf("]") + 1), Usage.class);
                    double u = 0, d = 0, total = 0;
                    for (Usage usa : usages) {
                        u += Double.parseDouble(usa.getU()) / Math.pow(2, 30);
                        d += Double.parseDouble(usa.getD()) / Math.pow(2, 30);
                        total += usa.getTotal();
                    }
                    sb.append("昨天消耗的流量情况\r\n ")
                            .append("\r\n上行流量：").append(df.format(u) + " G \r\n")
                            .append("下行流量：").append(df.format(d) + " G \r\n")
                            .append("总消耗：").append(df.format(total) + " G \r\n")
                            .append("\r\n昨天流量消耗榜排名第一的竟然是：").append(usages.get(0).getServer_name().substring(0, 5));

                } else {
//                    if(response.code() == 403)
                    flag = true;

                    if(falseCount > 8) {
                        sb.append("拜托, 现在网站出问题了..等会再查 \r\n");
                        // 休息两个小时
                        flag = false ;
                    }

                    System.out.println("流量消耗查询失败 flag = " + flag);
                }
                response.close();
            }catch (Exception e) {
                System.out.println("usages这里发生错误： " + e.getMessage());
            }

        }while (flag);

        return sb.toString();
    }

    /**
     *  发送消息
     * @param content
     * @throws Exception
     */
    public void sendMessage(String content) throws Exception{
        //执行请求操作
        try {
            SendMessage message = new SendMessage()
                    .setChatId(Constant.chat_id)
                    .setText(content);

        } catch (Exception e) {
            System.out.println("sendMessage 这里发生错误: " + e.getMessage());
        }
    }




}
