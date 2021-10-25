package com.zhutou.bot.node;


/**
 * 统计在线节点个数
 */
public class CountNode {
    public CheckNode cn = new CheckNode();

    public String nodeStatus() throws Exception {
        return cn.nodeStatus();
    }

    public String usage() throws Exception {
        return cn.usage();
    }

    public void saveUserInfoMap(){
        cn.saveUserInfoMap();
    }



}
